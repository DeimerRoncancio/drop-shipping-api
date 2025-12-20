package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.drop.shiping.api.drop_shiping_api.common.validation.IfExists;
import com.drop.shiping.api.drop_shiping_api.products.entities.Product;
import com.drop.shiping.api.drop_shiping_api.users.entities.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderDTO(
    @NotBlank(message = "{NotBlank.validation.text}")
    @IfExists(message = "{IfExists.validation}", field = "orderName", entity = "Order")
    String orderName,
    String notes,

    @NotNull(message = "{NotBlank.validation.text}")
    Date orderDate,

    @JsonIgnoreProperties({"id", "categories"})
    List<Product> product,

    @JsonIgnoreProperties({"id", "orders", "roles"})
    User user
) {
}

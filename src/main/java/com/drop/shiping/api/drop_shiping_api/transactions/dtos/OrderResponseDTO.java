package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.drop.shiping.api.drop_shiping_api.products.entities.Product;
import com.drop.shiping.api.drop_shiping_api.users.entities.User;

import java.util.Date;
import java.util.List;

public record OrderResponseDTO(
    String id,
    String orderName,
    String notes,
    Date orderDate,

    @JsonIgnoreProperties({"id", "categories"})
    List<Product> product,

    @JsonIgnoreProperties({"id", "orders", "roles"})
    User user
) {
}

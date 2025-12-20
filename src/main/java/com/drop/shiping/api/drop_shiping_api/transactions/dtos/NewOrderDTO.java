package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.drop.shiping.api.drop_shiping_api.common.validation.IfExists;
import com.drop.shiping.api.drop_shiping_api.users.entities.User;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewOrderDTO(
    @NotBlank(message = "{NotBlank.validation.text}")
    @IfExists(message = "{IfExists.validation}", field = "orderName", entity = "Order")
    String orderName,
    String notes,

    @NotNull(message = "{NotBlank.validation.text}")
    Date orderDate,

    @ManyToOne
    @JsonIgnoreProperties({"id", "orders", "roles"})
    User user
) {
}

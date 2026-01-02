package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductItemDTO(
    @NotEmpty
    String productId,

    @NotNull
    @Min(1)
    Integer quantity
) {}

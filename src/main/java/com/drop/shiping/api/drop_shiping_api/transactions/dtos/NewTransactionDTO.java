package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record NewTransactionDTO(
    String userId,
    String userReference,
    String reference,
    Date transactionDate,

    @Valid
    @NotEmpty
    List<ProductItemDTO> products,

    @NotNull
    @Min(1000)
    Long totalPrice
) {}

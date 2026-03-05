package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import com.drop.shiping.api.drop_shiping_api.transactions.entities.ProductItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.drop.shiping.api.drop_shiping_api.products.entities.Product;

import java.util.Date;
import java.util.List;

public record TransactionResponseDTO(
    String id,
    String reference,
    String status,
    Date transactionDate,
    String userNames,
    String userEmail,
    String userNumber,
    String userAddress,
    Long totalPrice,

    List<ItemResponseDTO> products
) {}

package com.drop.shiping.api.drop_shiping_api.transactions.dtos;

import jakarta.validation.constraints.Email;

public record UserInfoDTO(
    String userNames,

    @Email
    String userEmail,
    Long userNumber,
    String userAddress
) {}

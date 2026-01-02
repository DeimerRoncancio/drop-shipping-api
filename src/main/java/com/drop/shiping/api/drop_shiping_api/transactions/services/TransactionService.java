package com.drop.shiping.api.drop_shiping_api.transactions.services;

import java.util.Optional;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UserInfoDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewTransactionDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<OrderResponseDTO> findAll(Pageable pageable);

    Optional<OrderResponseDTO> findOne(String id);

    NewTransactionDTO addProductsInfo(NewTransactionDTO dto);

    UserInfoDTO addUserInfo(String userId, UserInfoDTO dto);

    Optional<Transaction> update(String id, UpdateOrderDTO order);

    Optional<Transaction> delete(String id);
}

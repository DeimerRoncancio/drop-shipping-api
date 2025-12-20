package com.drop.shiping.api.drop_shiping_api.transactions.services;

import java.util.Optional;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<OrderResponseDTO> findAll(Pageable pageable);

    Optional<OrderResponseDTO> findOne(String id);

    NewOrderDTO save(NewOrderDTO user);

    Optional<Transaction> update(String id, UpdateOrderDTO order);

    Optional<Transaction> delete(String id);
}

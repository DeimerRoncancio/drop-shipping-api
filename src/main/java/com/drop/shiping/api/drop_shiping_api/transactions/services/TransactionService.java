package com.drop.shiping.api.drop_shiping_api.transactions.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UserInfoDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewTransactionDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<OrderResponseDTO> findAll(Pageable pageable);

    Optional<OrderResponseDTO> findOne(String id);

    String createTransaction(NewTransactionDTO dto);

    Map<String, String> addUserInfo(String userReference, HttpServletResponse response, String id, UserInfoDTO dto);

    String updateProducts(String id, List<String> productIds);

    Optional<Transaction> delete(String id);
}

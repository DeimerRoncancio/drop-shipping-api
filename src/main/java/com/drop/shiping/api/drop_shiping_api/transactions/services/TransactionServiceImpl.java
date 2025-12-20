package com.drop.shiping.api.drop_shiping_api.transactions.services;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.mappers.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.repositories.TransactionRepository;
import com.drop.shiping.api.drop_shiping_api.users.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> findAll(Pageable pageable) {
        Page<Transaction> orders = repository.findAll(pageable);
        return orders.map(TransactionMapper.MAPPER::orderToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponseDTO> findOne(String id) {
        Optional<Transaction> order = repository.findById(id);
        return order.map(TransactionMapper.MAPPER::orderToResponseDTO);
    }

    @Override
    @Transactional
    public NewOrderDTO save(NewOrderDTO dto) {
        Transaction order = TransactionMapper.MAPPER.orderCreateDTOtoOrder(dto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findByEmail(principal);

        if (userOptional.isEmpty())
            userOptional = userRepository.findByPhoneNumber(Long.parseLong(principal));
        
        order.setUser(userOptional.orElseThrow());

        repository.save(order);
        return dto;
    }

    @Override
    @Transactional
    public Optional<Transaction> update(String id, UpdateOrderDTO dto) {
        Optional<Transaction> orderDb = repository.findById(id);

        orderDb.ifPresent(order -> {
            TransactionMapper.MAPPER.toUpdateOrder(dto, order);
            repository.save(order);
        });

        return orderDb;
    }

    @Override
    @Transactional
    public Optional<Transaction> delete(String id) {
        Optional<Transaction> orderOptional = repository.findById(id);
        orderOptional.ifPresent(repository::delete);
        return orderOptional;
    }
}

package com.drop.shiping.api.drop_shiping_api.transactions.services;

import com.drop.shiping.api.drop_shiping_api.products.entities.Product;
import com.drop.shiping.api.drop_shiping_api.products.repositories.ProductRepository;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewTransactionDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UserInfoDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.ProductItem;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.mappers.TransactionMapper;
import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.repositories.TransactionRepository;
import com.drop.shiping.api.drop_shiping_api.users.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public TransactionServiceImpl(TransactionRepository repository, UserRepository userRepository,  ProductRepository productRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> findAll(Pageable pageable) {
        Page<Transaction> transactions = repository.findAll(pageable);
        return transactions.map(TransactionMapper.MAPPER::orderToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponseDTO> findOne(String id) {
        Optional<Transaction> transaction = repository.findById(id);
        return transaction.map(TransactionMapper.MAPPER::orderToResponseDTO);
    }

    @Override
    @Transactional
    public NewTransactionDTO addProductsInfo(NewTransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setTotalPrice(dto.totalPrice());

        if (dto.userId() != null)
            userRepository.findById(dto.userId()).ifPresent(transaction::setUser);
        else
            transaction.setUserReference(dto.userReference());

        dto.products().forEach(product -> {
            Optional<Product> productDB = productRepository.findById(product.productId());
            ProductItem item = new ProductItem();

            productDB.ifPresent(productItem -> {
                item.setProduct(productItem);
                item.setTransaction(transaction);
                item.setQuantity(product.quantity());
                transaction.getProducts().add(item);
            });
        });

        repository.save(transaction);
        return dto;
    }

    @Override
    @Transactional
    public UserInfoDTO addUserInfo(String userId, UserInfoDTO userDTO) {
        repository.findByUserId(userId).or(() -> repository.findByUserReference(userId))
        .ifPresent(transaction -> {
            transaction.setUserEmail(userDTO.userEmail());
            transaction.setUserNames(userDTO.userNames());
            transaction.setUserNumber(String.valueOf(userDTO.userNumber()));
            transaction.setUserAddress(userDTO.userAddress());

            repository.save(transaction);
        });

        return userDTO;
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

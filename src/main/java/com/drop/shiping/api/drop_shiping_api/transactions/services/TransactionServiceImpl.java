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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.drop.shiping.api.drop_shiping_api.transactions.repositories.TransactionRepository;
import com.drop.shiping.api.drop_shiping_api.users.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.*;

import static com.drop.shiping.api.drop_shiping_api.security.JwtConfig.SECRET_KEY;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
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
    public String createTransaction(NewTransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setTotalPrice(dto.totalPrice());
        transaction.setTransactionDate(dto.transactionDate());
        transaction.setStatus("PROCESSING");

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
        return transaction.getId();
    }

    @Override
    @Transactional
    public Map<String, String> addUserInfo(String userReference, HttpServletResponse response, String id, UserInfoDTO userDTO) {
        Transaction transaction = repository.findById(id).orElseThrow();
        String identifier = "";

        if (userDTO.token() != null && !userDTO.token().isBlank()) {
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(userDTO.token())
                        .getPayload();

                identifier = claims.getSubject();

            } catch (Exception e) {
                identifier = "";
            }
        }

        if (!identifier.isEmpty()) {
            Optional<User> userOptional = isNumeric(identifier)
                    ? userRepository.findByPhoneNumber(Long.parseLong(identifier))
                    : userRepository.findByEmail(identifier);

            userOptional.ifPresent(transaction::setUser);
        } else {
            if (userReference != null) {
                transaction.setUserReference(userReference);
            } else {
                String reference = UUID.randomUUID().toString();
                Cookie cookie = new Cookie("userReference", reference);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setMaxAge(60 * 60 * 24 * 365);
                cookie.setPath("/");
                response.addCookie(cookie);

                transaction.setUserReference(reference);
            }
        }

        transaction.setUserEmail(userDTO.userEmail());
        transaction.setUserNames(userDTO.userNames());
        transaction.setUserNumber(String.valueOf(userDTO.userNumber()));
        transaction.setUserAddress(userDTO.userAddress());

        repository.save(transaction);
        return Map.of(
                "userReference",
                Boolean.parseBoolean(transaction.getUserReference())
                        ? transaction.getUserReference()
                        : ""
        );
    }

    @Override
    @Transactional
    public String updateProducts(String id, List<String> productIds) {
        Transaction transaction = repository.findById(id).orElseThrow();
        List<Product> productList = productRepository.findAllById(productIds);



//        transaction.getProducts().removeIf(p -> !productIds.contains(p.getProduct().getId()));

//        transaction.getProducts().removeAll(itemsToRemove);

//        transactionOp.ifPresent(transaction -> {
//            transaction.setProducts(productList);
//            repository.save(transaction);
//        });

        return "";
    }

    @Override
    @Transactional
    public Optional<Transaction> delete(String id) {
        Optional<Transaction> transactionOp = repository.findById(id);
        transactionOp.ifPresent(repository::delete);
        return transactionOp;
    }

    public boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}

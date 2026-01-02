package com.drop.shiping.api.drop_shiping_api.transactions.repositories;

import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByUserId(String userId);
    Optional<Transaction> findByUserReference(String userReference);
}

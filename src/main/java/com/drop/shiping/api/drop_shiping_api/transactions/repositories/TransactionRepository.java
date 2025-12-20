package com.drop.shiping.api.drop_shiping_api.transactions.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByReference(String orderName);
}

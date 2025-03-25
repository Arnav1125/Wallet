package com.example.wallet.repository;

import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByDateDesc(User user);
}
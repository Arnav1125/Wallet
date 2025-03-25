package com.example.wallet.service;

import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User createUser(String username);
    User getUserByUsername(String username);
    BigDecimal getBalance(String username);
    void deposit(String username, BigDecimal amount);
    void withdraw(String username, BigDecimal amount);
    List<Transaction> getTransactionHistory(String username);
}
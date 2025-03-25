package com.example.wallet.service;

import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                         TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public User createUser(String username) {
        User newUser = new User(username);
        return userRepository.save(newUser);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public BigDecimal getBalance(String username) {
        return getUserByUsername(username).getBalance();
    }

    @Override
    @Transactional
    public void deposit(String username, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        User user = getUserByUsername(username);
        user.setBalance(user.getBalance().add(amount));
        
        Transaction transaction = new Transaction(
            Transaction.TransactionType.DEPOSIT, 
            amount, 
            user
        );
        user.addTransaction(transaction);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void withdraw(String username, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        User user = getUserByUsername(username);
        BigDecimal currentBalance = user.getBalance();
        
        if (currentBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        
        user.setBalance(currentBalance.subtract(amount));
        
        Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAWAL, 
            amount, 
            user
        );
        user.addTransaction(transaction);
        userRepository.save(user);
    }

    @Override
    public List<Transaction> getTransactionHistory(String username) {
        User user = getUserByUsername(username);
        return transactionRepository.findByUserOrderByDateDesc(user);
    }
}
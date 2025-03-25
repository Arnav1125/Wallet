package com.example.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors
    public User() {}
    public User(String username) {
        this.username = username;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public String getUsername() { return username; }
    public BigDecimal getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setId(Integer id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setUser(this);
    }
}
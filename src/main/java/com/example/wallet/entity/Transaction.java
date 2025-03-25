package com.example.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Transaction() {
        this.date = LocalDateTime.now();
    }

    public Transaction(TransactionType type, BigDecimal amount, User user) {
        this();
        this.type = type;
        this.amount = amount;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
    public User getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setType(TransactionType type) { this.type = type; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setUser(User user) { this.user = user; }
}
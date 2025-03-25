package com.example.wallet.controller;

import com.example.wallet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private final UserService userService;
    private static final String DEFAULT_USER = "defaultUser";

    @Autowired
    public WalletController(UserService userService) {
        this.userService = userService;
        try {
            initializeDefaultUser();
        } catch (Exception e) {
            logger.error("Failed to initialize default user", e);
            throw new RuntimeException("Application initialization failed", e);
        }
    }

    @GetMapping
    public String walletDashboard(Model model) {
        try {
            model.addAttribute("balance", userService.getBalance(DEFAULT_USER));
            model.addAttribute("transactions", 
                userService.getTransactionHistory(DEFAULT_USER));
        } catch (Exception e) {
            logger.error("Error loading wallet dashboard", e);
            model.addAttribute("error", "Failed to load wallet data");
        }
        return "wallet";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount, Model model) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                model.addAttribute("error", "Deposit amount must be positive");
            } else {
                userService.deposit(DEFAULT_USER, amount);
                model.addAttribute("message", "Deposit successful!");
            }
        } catch (Exception e) {
            logger.error("Deposit failed", e);
            model.addAttribute("error", "Deposit failed: " + e.getMessage());
        }
        return "redirect:/wallet";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount, Model model) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                model.addAttribute("error", "Withdrawal amount must be positive");
            } else {
                userService.withdraw(DEFAULT_USER, amount);
                model.addAttribute("message", "Withdrawal successful!");
            }
        } catch (Exception e) {
            logger.error("Withdrawal failed", e);
            model.addAttribute("error", "Withdrawal failed: " + e.getMessage());
        }
        return "redirect:/wallet";
    }

    private void initializeDefaultUser() {
        try {
            userService.getUserByUsername(DEFAULT_USER);
            logger.info("Default user already exists");
        } catch (Exception e) {
            logger.info("Creating default user");
            userService.createUser(DEFAULT_USER);
            // Initial deposit for the default user
            userService.deposit(DEFAULT_USER, new BigDecimal("100.00"));
        }
    }
}
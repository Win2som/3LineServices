package com.example.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionMailRequest {
    private String firstName;
    private String email;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal currentBalance;
    private String status;
    private String date;
}
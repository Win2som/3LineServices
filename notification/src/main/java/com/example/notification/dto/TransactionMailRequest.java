package com.example.notification.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMailRequest {

    private String firstName;
    private String email;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal currentBalance;
    private String status;
    private String date;
}
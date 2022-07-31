package com.example.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TransactionResponse {

    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private String narration;
    private String status;
    private LocalDateTime createdAt;

}
package com.example.transaction.utils;


import com.example.transaction.dto.TransactionMailRequest;
import com.example.transaction.dto.User;
import com.example.transaction.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Utility {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public TransactionMailRequest fromTransactionToMailRequest(Transaction transaction, User user) {

        return TransactionMailRequest.builder()
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .currentBalance(user.getWallet().getBalance())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .date(String.valueOf(transaction.getCreatedAt()))
                .build();
    }



    public BigDecimal percentage(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(ONE_HUNDRED);
    }
}

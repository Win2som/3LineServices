package com.example.notification.service;

import com.example.notification.dto.TransactionMailRequest;
import com.example.notification.dto.UserMailRequest;

public interface NotificationService {
    void sendVerificationEmail(UserMailRequest userMailRequest);

    String sendTransactionEmail(TransactionMailRequest transactionMailRequest);

    String confirmToken(String token, Long user_id);
}

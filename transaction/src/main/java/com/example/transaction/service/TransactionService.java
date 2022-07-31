package com.example.transaction.service;

import com.example.transaction.dto.BuyRequest;
import com.example.transaction.dto.FundRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface TransactionService {
    ResponseEntity<String> fundAccount(FundRequest fundRequest, HttpServletRequest request);

    ResponseEntity<String> buyContent(BuyRequest buyRequest, HttpServletRequest request);
}

package com.example.transaction.controller;


import com.example.transaction.dto.BuyRequest;
import com.example.transaction.dto.FundRequest;
import com.example.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    //fund
    //withdraw
    //buy
    //get transactions
    private final TransactionService transactionService;

    @PostMapping("/fund")
    public ResponseEntity<String> fundAccount(@RequestBody FundRequest fundRequest, HttpServletRequest request){
        return transactionService.fundAccount(fundRequest, request);
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyContent(@Valid @RequestBody BuyRequest buyRequest, HttpServletRequest request){
        return transactionService.buyContent(buyRequest, request);
    }

//    @PostMapping("/withdraw")
//    public ResponseEntity<String> withdraw(@RequestBody FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request){
//        return transactionService.withdrawFromAccount(fundWithdrawRequest, request);
//    }
}

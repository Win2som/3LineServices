package com.example.transaction.service;

import com.example.amqp.RabbitMQMessageProducer;
import com.example.transaction.dto.*;
import com.example.transaction.entity.Transaction;
import com.example.transaction.enums.TransactionType;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.security.JwtUtils;
import com.example.transaction.utils.UserService;
import com.example.transaction.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private  final JwtUtils jwtUtils;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final Utility utility;
    @Value("${admin_account}")
    private String adminAccount;
   @Value("${subadmin_account}")
    private String sub_AdminAccount;

    @Transactional
    @Override
    public ResponseEntity<String> fundAccount(FundRequest fundRequest, HttpServletRequest request) {

            User user;
            Transaction savedTransaction;
            try{
                user = userService.readUser(fundRequest.getAccountNum(), request);
                assert user != null;

                if(!fundRequest.getPin().equals(user.getWallet().getPin())){
                    throw new RuntimeException("incorrect pin");
                }
                if(fundRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0){
                    throw new RuntimeException("bad request");
                }
                user.getWallet().setBalance(user.getWallet().getBalance().add(fundRequest.getAmount()));
                userService.updateAccount(user, request);

                log.info("account {} successfully funded with amount: {}", user, fundRequest.getAmount());
                //save transaction
                Transaction transaction = Transaction.builder()
                        .transactionType(TransactionType.CREDIT)
                        .amount(fundRequest.getAmount())
                        .status("Completed")
                        .buyerId(0L)
                        .contentId(0L)
                        .createdAt(LocalDateTime.now())
                        .build();

                savedTransaction = transactionRepository.save(transaction);

            }catch(RuntimeException e){
                throw new RuntimeException(e.getMessage());
            }
            //publish mail
            TransactionMailRequest mailRequest = utility.fromTransactionToMailRequest(savedTransaction, user);
            mailRequest.setTransactionType("CREDIT");
            rabbitMQMessageProducer.publish(mailRequest, "internal.notification.routing-key", "internal.exchange");

            return new ResponseEntity<>("Account funded successfully", HttpStatus.OK);
        }


    @Transactional
    @Override
    public ResponseEntity<String> buyContent(BuyRequest buyRequest, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJWTFromRequest(request));

        String requestParam1 = buyRequest.getAccountNumber();
        User consumer = userService.readUser(requestParam1, request);
        log.info("first call to user service {}", consumer);

        if(!userId.equals(consumer.getId())){
            throw new RuntimeException("Please input your correct account number");
        }

        if(!buyRequest.getPin().equals(consumer.getWallet().getPin())){
            throw new RuntimeException("incorrect pin");
        }

        //call user service to get content
        Content content = userService.readContent(buyRequest.getTitle(), request);

        if(content.getPrice().compareTo(consumer.getWallet().getBalance()) > 0){
            throw new RuntimeException("insufficient balance");
        }
        consumer.getWallet().setBalance(consumer.getWallet().getBalance().subtract(content.getPrice()));
        userService.updateAccount(consumer, request);

        log.info("User {} debited by amount: {}",consumer, content.getPrice());


        //get admin
        User admin = userService.readUser(adminAccount, request);
        BigDecimal adminCut = utility.percentage(content.getPrice(), BigDecimal.TEN);
        admin.getWallet().setBalance(admin.getWallet().getBalance().add(adminCut));
        userService.updateAccount(admin, request);


        //get subadmin
        User subadmin = userService.readUser(sub_AdminAccount, request);
        BigDecimal subadminCut = utility.percentage(content.getPrice(), BigDecimal.valueOf(5));
        subadmin.getWallet().setBalance(subadmin.getWallet().getBalance().add(subadminCut));
        userService.updateAccount(subadmin, request);

        BigDecimal contentCreatorCut = content.getPrice().subtract(adminCut.add(subadminCut));

        //get content creator account
        String requestParam2 = content.getUser().getWallet().getAccountNumber();
        User contentCreator = userService.readUser(requestParam2, request);
        contentCreator.getWallet().setBalance(contentCreator.getWallet().getBalance().add(contentCreatorCut));
        userService.updateAccount(contentCreator, request);
        log.info("Content creator {} credited by amount: {}",contentCreator, content.getPrice());

        //build transaction
        Transaction transaction = Transaction.builder()
                .contentId(content.getId())
                .buyerId(userId)
                .amount(content.getPrice())
                .status("Completed")
                .createdAt(LocalDateTime.now())
                .transactionType(TransactionType.SALES)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        //send mails
        TransactionMailRequest consumerMailRequest = utility.fromTransactionToMailRequest(savedTransaction, consumer);
        consumerMailRequest.setTransactionType("DEBIT");
        rabbitMQMessageProducer.publish(consumerMailRequest, "internal.notification.routing-key", "internal.exchange");

        TransactionMailRequest adminMailRequest = utility.fromTransactionToMailRequest(savedTransaction, consumer);
        adminMailRequest.setTransactionType("CREDIT");
        rabbitMQMessageProducer.publish(adminMailRequest, "internal.notification.routing-key", "internal.exchange");

        TransactionMailRequest subAdminMailRequest = utility.fromTransactionToMailRequest(savedTransaction, consumer);
        subAdminMailRequest.setTransactionType("CREDIT");
        rabbitMQMessageProducer.publish(subAdminMailRequest, "internal.notification.routing-key", "internal.exchange");

        TransactionMailRequest contentCreatorMailRequest = utility.fromTransactionToMailRequest(savedTransaction, consumer);
        contentCreatorMailRequest.setTransactionType("CREDIT");
        rabbitMQMessageProducer.publish(contentCreatorMailRequest, "internal.notification.routing-key", "internal.exchange");




        return null;

    }


}

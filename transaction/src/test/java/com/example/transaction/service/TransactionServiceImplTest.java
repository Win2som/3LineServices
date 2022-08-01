package com.example.transaction.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.amqp.RabbitMQMessageProducer;
import com.example.transaction.dto.*;
import com.example.transaction.entity.Transaction;
import com.example.transaction.enums.TransactionType;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.security.JwtUtils;
import com.example.transaction.utils.UserService;
import com.example.transaction.utils.Utility;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ContextConfiguration(classes = {TransactionServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private Utility utility;

    private User user;
    private Transaction transaction;
    private FundRequest fundRequest;
    private TransactionMailRequest mailRequest;

    @BeforeEach
    public void setup(){

        Wallet wallet = Wallet.builder()
                        .accountNumber("1234567890")
                        .balance(BigDecimal.ZERO)
                        .pin("1234")
                        .build();

         user =  User.builder()
                        .id(1L)
                        .firstName("Kalvin")
                        .lastName("Clan")
                        .email("winijay2@gmail.com")
                        .wallet(wallet)
                        .build();
         fundRequest = FundRequest.builder()
                .accountNum("1234987650")
                .amount(BigDecimal.valueOf(200))
                .pin("1234")
                .build();
         transaction = Transaction.builder()
                 .contentId(0L)
                 .buyerId(0L)
                 .createdAt(LocalDateTime.now())
                .transactionType(TransactionType.CREDIT)
                .amount(fundRequest.getAmount())
                .status("Completed")
                .build();
        mailRequest = TransactionMailRequest.builder()
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .amount(fundRequest.getAmount())
                .transactionType("CREDIT")
                .currentBalance(user.getWallet().getBalance())
                .status("Completed")
                .date("")
                .build();
    }
    /**
     * Method under test: {@link TransactionServiceImpl#fundAccount(FundRequest, HttpServletRequest)}
     */
    @Test
    void givenFundRequest_whenFundAccount_thenUpdateBalance() {
        when(userService.readUser((String) any(), (HttpServletRequest) any())).thenReturn(user);
        when(utility.fromTransactionToMailRequest((Transaction) any(), (User)any())).thenReturn(mailRequest);

        transactionServiceImpl.fundAccount(fundRequest,new MockHttpServletRequest());
        org.assertj.core.api.Assertions.assertThat(user.getWallet().getBalance()).isEqualTo(BigDecimal.valueOf(200));
        verify(userService).readUser((String) any(), (HttpServletRequest) any());
    }


    /**
     * Method under test: {@link TransactionServiceImpl#fundAccount(FundRequest, HttpServletRequest)}
     */
    @Test
    void givenEmptyFundRequest_whenFundAccount_throwRuntimeException() {

        assertThrows(RuntimeException.class, () -> transactionServiceImpl.fundAccount(null, new MockHttpServletRequest()));
    }


    /**
     * Method under test: {@link TransactionServiceImpl#buyContent(BuyRequest, HttpServletRequest)}
     */
    @Test
    void givenUnAuthenticatedUser_whenBuyContent_thenThrowException() {

        when(jwtUtils.getJWTFromRequest((HttpServletRequest) any())).thenThrow(new RuntimeException("An error occurred"));
        BuyRequest buyRequest = new BuyRequest("Content", "4267890324", "Pin");

        assertThrows(RuntimeException.class,
                () -> transactionServiceImpl.buyContent(buyRequest, new MockHttpServletRequest()));
        verify(jwtUtils).getJWTFromRequest((HttpServletRequest) any());
    }


}


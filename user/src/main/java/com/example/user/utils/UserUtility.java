package com.example.user.utils;

import com.example.user.dto.CreateUserRequest;
import com.example.user.entity.User;
import com.example.user.entity.Wallet;
import com.example.user.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;


@Component
@RequiredArgsConstructor
@Slf4j
public class UserUtility {
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;

    public User from(CreateUserRequest userRequest){

        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .wallet(buildWallet(userRequest))
                .createdTime(LocalDateTime.now())
                .modifiedTime(LocalDateTime.now())
                .build();
    }



    public Wallet buildWallet(CreateUserRequest userRequest) {

        Wallet wallet = Wallet.builder()
                .accountNumber(getUniqueAccountNumber())
                .balance(BigDecimal.ZERO)
                .pin(userRequest.getPin())
                .build();

        wallet.setCreatedTime(LocalDateTime.now());
        wallet.setModifiedTime(LocalDateTime.now());

        log.info("Account number generated {}",wallet.getAccountNumber());
        return walletRepository.save(wallet);
    }


    //account number generator
    public String generateAccountNumber() {
        Random rn = new Random();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            number.append(rn.nextInt(10));
        }
        return number.toString();
    }

    public String getUniqueAccountNumber(){
        String accountNumber = generateAccountNumber();

        Wallet wallet = walletRepository.findByAccountNumber(accountNumber);
        if (wallet == null){
            return accountNumber;
        }else{
            accountNumber = generateAccountNumber();
        }
        log.info("Account number generated {}", accountNumber);
        return accountNumber;
    }


}

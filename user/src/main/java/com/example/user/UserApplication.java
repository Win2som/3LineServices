package com.example.user;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.entity.Wallet;
import com.example.user.enums.RoleType;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication(
        scanBasePackages = {
                "com.example.user",
                "com.example.amqp"
        }
)
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}

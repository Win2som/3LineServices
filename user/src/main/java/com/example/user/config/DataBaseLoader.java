package com.example.user.config;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.entity.Wallet;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataBaseLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataBaseLoader(RoleRepository roleRepository, WalletRepository walletRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<Role> checkeRole = roleRepository.findByTitle("ADMIN");
        if(checkeRole.isEmpty()){
            Role role = roleRepository.save(Role.builder()
                    .title("ADMIN")
                    .build());
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            Wallet wallet = walletRepository.save(
                    Wallet.builder()
                            .accountNumber("1234567890")
                            .balance(BigDecimal.ZERO)
                            .pin("1234")
                            .build()
            );
            userRepository.save(
                    User.builder()
                            .firstName("Client")
                            .lastName("Institution")
                            .email("winijay2@gmail.com")
                            .enabled(true)
                            .password(passwordEncoder.encode("asdfghjkl"))
                            .createdTime(LocalDateTime.now())
                            .modifiedTime(LocalDateTime.now())
                            .roles(roles)
                            .wallet(wallet)
                            .build()
            );


        }

        Optional<Role> checkeRole1 = roleRepository.findByTitle("SUB_ADMIN");

        if(checkeRole1.isEmpty()){

            Role role1 = roleRepository.save(Role.builder()
                    .title("SUB_ADMIN")
                    .build());
            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);

            Wallet wallet1 = walletRepository.save(
                    Wallet.builder()
                            .accountNumber("0987654321")
                            .balance(BigDecimal.ZERO)
                            .pin("4321")
                            .build()
            );
            userRepository.save(
                    User.builder()
                            .firstName("Contracting")
                            .lastName("Company")
                            .email("obidikewinifred@yahoo.com")
                            .enabled(true)
                            .password(passwordEncoder.encode("lkjhgfdsa"))
                            .createdTime(LocalDateTime.now())
                            .modifiedTime(LocalDateTime.now())
                            .roles(roles1)
                            .wallet(wallet1)
                            .build()
            );

        }
    }
}

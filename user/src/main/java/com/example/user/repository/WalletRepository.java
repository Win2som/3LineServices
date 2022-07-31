package com.example.user.repository;

import com.example.user.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByAccountNumber(String accountNumber);

    @Modifying
    @Transactional
    @Query(value = "update wallet set balance = :balance, version = version + 1 where id = :id and version = :version",
            nativeQuery = true)
    int updateWalletBalance(@Param("id") Long id, @Param("version") Long version, @Param("balance")BigDecimal balance);
}

package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value = "select count(*)>0 from users where lower(email) = lower(:email)", nativeQuery = true)
    boolean existsByEmail(String email);

    @Query(value = "select * from users where lower(email) = lower(:email)", nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "select * from users where wallet_id = :id", nativeQuery = true)
    User findByWalletId(Long id);
}

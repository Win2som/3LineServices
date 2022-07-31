package com.example.user.repository;

import com.example.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select * from role where title = :title", nativeQuery = true)
    Optional<Role> findByTitle(String title);
}

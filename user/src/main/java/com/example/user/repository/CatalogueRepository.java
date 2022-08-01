package com.example.user.repository;

import com.example.user.entity.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {
    @Query(value = "select * from catalogue where user_id = :id", nativeQuery = true)
    Catalogue findByUserId(Long id);
}

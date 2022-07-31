package com.example.user.repository;

import com.example.user.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query(value = "select * from content where title = :title", nativeQuery = true)
    Content findByTitle(String title);
}

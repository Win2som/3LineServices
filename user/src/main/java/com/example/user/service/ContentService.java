package com.example.user.service;

import com.example.user.dto.CreateContentRequest;
import com.example.user.entity.Content;
import com.example.user.exception.ResourceAlreadyExistException;
import org.springframework.http.ResponseEntity;

public interface ContentService {
    ResponseEntity<String> createContent(CreateContentRequest contentRequest) throws ResourceAlreadyExistException;

    ResponseEntity<Content> getContent(String title);
}

package com.example.user.service;

import com.example.user.dto.CreateUserRequest;
import com.example.user.entity.User;
import com.example.user.exception.CustomException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.exception.ResourceAlreadyExistException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<String> createUser(CreateUserRequest userRequest) throws ResourceAlreadyExistException;
    void enableUser(Long id) throws ResourceNotFoundException;

    ResponseEntity<String> createContentCreator(CreateUserRequest userRequest) throws ResourceAlreadyExistException;

    ResponseEntity<User> getUserByAccountNumber(String accountNum);

    ResponseEntity<String> walletBalanceUpdate(User user, Long id) throws CustomException;
}

package com.example.user.controller;


import com.example.user.dto.CreateUserRequest;
import com.example.user.entity.User;
import com.example.user.exception.CustomException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.exception.ResourceAlreadyExistException;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping("")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest userRequest) throws ResourceAlreadyExistException {
        return userService.createUser(userRequest);
    }


    @PostMapping("/create_content_creator")
    public ResponseEntity<String> createContentCreator(@Valid @RequestBody CreateUserRequest userRequest) throws ResourceAlreadyExistException {
        return userService.createContentCreator(userRequest);
    }


    @PutMapping("/enable/{account_id}")
    public void enableUser(@PathVariable("account_id") Long id) throws ResourceNotFoundException {
        userService.enableUser(id);
    }


    @GetMapping("/get")
    public ResponseEntity<User> getUserByAccountNumber(@RequestParam(name = "accountNum")String accountNum){
        return userService.getUserByAccountNumber(accountNum);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> walletBalanceUpdate(@RequestBody User user, @PathVariable("id")Long id) throws CustomException, CustomException {
        return userService.walletBalanceUpdate(user, id);
    }


}

package com.example.notification.controller;


import com.example.notification.dto.TransactionMailRequest;
import com.example.notification.dto.UserMailRequest;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/verify")
    public void verifyEmail(@RequestBody UserMailRequest mailRequest){
        notificationService.sendVerificationEmail(mailRequest);
    }


    @GetMapping(path = "/confirm/{id}")
    public String confirm(@RequestParam("token")String token, @PathVariable("id") Long user_id) {
        return notificationService.confirmToken(token, user_id);
    }


    @PostMapping("/notify")
    public String sendTransactionEmail(@RequestBody TransactionMailRequest mailRequest){
        return notificationService.sendTransactionEmail(mailRequest);
    }
}

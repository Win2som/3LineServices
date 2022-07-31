package com.example.notification.email;


public interface EmailSender {
    void send(String to, String email, String subject);
}

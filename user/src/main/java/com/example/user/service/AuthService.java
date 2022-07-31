package com.example.user.service;

import com.example.user.dto.AuthRequest;

public interface AuthService {
    String login(AuthRequest authRequest);
}

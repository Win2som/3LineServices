package com.example.user.dto;

import com.example.user.entity.Wallet;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
}


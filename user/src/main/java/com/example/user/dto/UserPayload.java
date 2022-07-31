package com.example.user.dto;

import lombok.*;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload {

        private String firstName;
        private String email;
        private Long id;
}

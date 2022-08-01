package com.example.user.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponse {
    private String title;
    private String body;
    private BigDecimal price;
}

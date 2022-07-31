package com.example.transaction.dto;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Content {
    private Long id;
    private String title;
    private String body;
    private BigDecimal price;
    private User user;

}

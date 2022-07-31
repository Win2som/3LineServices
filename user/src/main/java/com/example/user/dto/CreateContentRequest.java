package com.example.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CreateContentRequest {
    @NotBlank(message = "Title should not be blank")
    private String title;
    @NotBlank(message = "Body should not be blank")
    private String body;
    @NotNull(message = "Price should not be blank")
    private BigDecimal price;
}

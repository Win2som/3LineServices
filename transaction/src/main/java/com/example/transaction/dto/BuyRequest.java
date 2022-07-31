package com.example.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyRequest {
    @NotBlank(message = "Title account must be provided")
    private String title;
    @NotBlank(message = "Account number account must be provided")
    private String accountNumber;
    @NotBlank(message = "Pin must be provided")
    private String pin;
}

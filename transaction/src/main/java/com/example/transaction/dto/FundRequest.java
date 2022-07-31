package com.example.transaction.dto;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundRequest {

    @NotNull
    @Min(value = 1, message = "the least amount you can deposit is 1")
    private BigDecimal amount;

    @NotBlank(message = "Destination account must be provided")
    @Size(min = 10, message = "Account number should have at least 10 characters")
    private String accountNum;

    @NotBlank(message = "pin must be provided")
    @Size(min = 4,max = 4, message = "Pin should have 4 numbers")
    private String pin;
}

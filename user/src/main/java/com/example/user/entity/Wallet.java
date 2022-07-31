package com.example.user.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet extends Base{

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @PositiveOrZero
    private BigDecimal balance;

    @Column(nullable = false)
    private String pin;

    @Version
    @Column(nullable = false)
    private long version;

}

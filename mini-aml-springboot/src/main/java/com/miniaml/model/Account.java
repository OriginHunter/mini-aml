package com.miniaml.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;
    @NotBlank(message = "帐号不能为空")
    private String accountNo;
    @NotNull(message = "帐户 ID 不能为空")
    private Long customerId;

    private BigDecimal balance;

    private LocalDateTime createdAt;
}

package org.project.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
public class PaymentSession {

    @NotNull
    private UUID paymentId;

    @NotNull
    private String checkoutSessionUrl;

    @Positive
    private BigDecimal totalSum;
}

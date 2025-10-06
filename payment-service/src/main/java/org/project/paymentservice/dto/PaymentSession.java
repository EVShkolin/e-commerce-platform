package org.project.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
public class PaymentSession {

    private UUID paymentId;

    private String checkoutSessionUrl;

    private BigDecimal totalSum;
}

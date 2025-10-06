package org.project.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    @Positive
    private Long id;

    @NotNull
    private String userId;

    @Positive
    private BigDecimal totalAmount;

    @NotNull
    private List<OrderItemDto> items;

}



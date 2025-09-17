package org.project.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDto {

    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    @PositiveOrZero
    private BigDecimal price;

    @Positive
    private Integer quantity;

}

package org.project.shoppingcartservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    @NotBlank
    private Long productId;

    @NotBlank
    private String productName;

    @Positive
    private BigDecimal price;

    @Positive
    private Integer quantity;

}

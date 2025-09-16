package org.project.shoppingcartservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    @NotBlank
    private String userId;

    private BigDecimal totalSum;

    private List<CartItemDto> items;

    public CartDto(String userId) {
        this.userId = userId;
        items = new ArrayList<>();
        totalSum = BigDecimal.ZERO;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
        calculateTotal();
    }

    public void calculateTotal() {
        totalSum = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package org.project.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.project.orderservice.model.OrderItem;
import org.project.orderservice.model.OrderStatus;
import org.project.orderservice.model.ShippingDetails;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotBlank
    private String userId;

    private BigDecimal totalAmount;

    private List<OrderItemDto> items;

    @NotBlank
    private OrderStatus status;

    private ShippingDetails shippingDetails;

}

package org.project.orderservice.mapper;

import org.project.orderservice.dto.OrderDto;
import org.project.orderservice.dto.OrderItemDto;
import org.project.orderservice.dto.OrderRequest;
import org.project.orderservice.model.Order;
import org.project.orderservice.model.OrderItem;
import org.project.orderservice.model.ShippingDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public ShippingDetails toShippingDetails(OrderRequest orderRequest) {
        return new ShippingDetails(orderRequest.getRecipientName(),
                                    orderRequest.getPhoneNumber(),
                                    orderRequest.getShippingAddress());
    }

    public Order toOrder(OrderDto orderDto) {
        Order order = new Order(orderDto.getUserId(), orderDto.getTotalAmount());
        List<OrderItem> items = toOrderItemList(orderDto.getItems());
        order.setItems(items);

        for (OrderItem item : items)
            item.setOrder(order);

        return order;
    }

    private List<OrderItem> toOrderItemList(List<OrderItemDto> items) {
        return items.stream()
                .map(this::toOrderItem)
                .toList();
    }

    private OrderItem toOrderItem(OrderItemDto orderItemDto) {
        return new OrderItem(orderItemDto.getProductId(),
                orderItemDto.getProductName(),
                orderItemDto.getPrice(),
                orderItemDto.getQuantity());
    }

    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto(order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                new ArrayList<>(),
                order.getStatus(),
                order.getShippingDetails());

        List<OrderItemDto> orderItemDtos = toOrderItemDtoList(order.getItems());
        orderDto.setItems(orderItemDtos);
        return orderDto;
    }

    private List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemDto)
                .toList();
    }

    private OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getPriceSnapshot(),
                orderItem.getQuantity());
    }
}

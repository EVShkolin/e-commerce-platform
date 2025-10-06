package org.project.orderservice.service;

import org.project.orderservice.dto.OrderDto;
import org.project.orderservice.dto.OrderRequest;
import org.project.orderservice.dto.PaymentSession;
import org.project.orderservice.dto.StatusUpdateRequest;

import java.util.List;

public interface OrderService {

    PaymentSession createOrder(OrderRequest orderRequest);

    OrderDto findOrderById(Long id);

    List<OrderDto> getUserOrders(String userId);

    OrderDto updateOrderStatus(Long orderId, StatusUpdateRequest statusUpdateRequest);

}

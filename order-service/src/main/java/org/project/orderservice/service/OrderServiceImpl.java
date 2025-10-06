package org.project.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.orderservice.client.CartClient;
import org.project.orderservice.client.PaymentClient;
import org.project.orderservice.dto.OrderDto;
import org.project.orderservice.dto.OrderRequest;
import org.project.orderservice.dto.PaymentSession;
import org.project.orderservice.dto.StatusUpdateRequest;
import org.project.orderservice.exceptionhandler.exception.EmptyOrderException;
import org.project.orderservice.exceptionhandler.exception.OrderNotFoundException;
import org.project.orderservice.mapper.OrderMapper;
import org.project.orderservice.model.Order;
import org.project.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final PaymentClient paymentClient;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public PaymentSession createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.toOrder(cartClient.getCartByUserId(orderRequest.getUserId()));
        if (order.getItems().isEmpty()) {
            throw new EmptyOrderException(order.getUserId());
        }
        order.setShippingDetails(orderMapper.toShippingDetails(orderRequest));
        order = orderRepository.save(order);
        log.info("Created new order for user {}", orderRequest.getUserId());
        OrderDto orderDto = orderMapper.toDto(order);
        // todo clear cart
        return paymentClient.createPayment(orderDto);
    }

    @Override
    public OrderDto findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, StatusUpdateRequest statusUpdateRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(statusUpdateRequest.getStatus());

        log.info("Order {} paid", orderId);
        return orderMapper.toDto(order);
    }
}

package org.project.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.orderservice.client.CartClient;
import org.project.orderservice.client.PaymentClient;
import org.project.orderservice.client.UserClient;
import org.project.orderservice.dto.OrderDto;
import org.project.orderservice.dto.OrderRequest;
import org.project.orderservice.dto.PaymentSession;
import org.project.orderservice.dto.StatusUpdateRequest;
import org.project.orderservice.event.CartClearEvent;
import org.project.orderservice.event.OrderConfirmedEvent;
import org.project.orderservice.event.OrderPaidEvent;
import org.project.orderservice.exceptionhandler.exception.EmptyOrderException;
import org.project.orderservice.exceptionhandler.exception.OrderNotFoundException;
import org.project.orderservice.mapper.OrderMapper;
import org.project.orderservice.model.Order;
import org.project.orderservice.model.OrderStatus;
import org.project.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${kafka.topics.carts-cleared}")
    private String cartsClearedTopic;
    @Value("${kafka.topics.orders-confirmed}")
    private String ordersConfirmedTopic;

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final PaymentClient paymentClient;
    private final UserClient userClient;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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

    @KafkaListener(topics = "${kafka.topics.orders-paid}")
    public void handleOrderPaidEvent(OrderPaidEvent event) {
        log.debug("Received order paid event for order {}", event.orderId());
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new OrderNotFoundException(event.orderId()));
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        CartClearEvent cartClearEvent = new CartClearEvent(event.userId());
        kafkaTemplate.send(cartsClearedTopic, cartClearEvent);

        String userEmail = userClient.getUserEmail(event.userId());
        OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(event.userId(), userEmail, event.orderId());
        kafkaTemplate.send(ordersConfirmedTopic, orderConfirmedEvent);
    }
}

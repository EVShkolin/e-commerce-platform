package org.project.orderservice.event;

public record OrderPaidEvent(String userId, Long orderId) {}

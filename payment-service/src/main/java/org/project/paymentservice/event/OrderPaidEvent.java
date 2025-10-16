package org.project.paymentservice.event;

public record OrderPaidEvent(String userId, Long orderId) {}

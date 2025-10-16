package org.project.orderservice.event;

public record OrderConfirmedEvent(String userId, String email, Long orderId) {
}

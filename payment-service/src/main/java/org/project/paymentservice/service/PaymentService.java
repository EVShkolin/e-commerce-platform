package org.project.paymentservice.service;

import org.project.paymentservice.dto.OrderDto;
import org.project.paymentservice.dto.PaymentSession;

import java.util.UUID;

public interface PaymentService {

    PaymentSession createPayment(OrderDto orderDto);

    void processStripeEvent(String payload, String sigHeader);

    void processTestEvent(UUID paymentId);
}

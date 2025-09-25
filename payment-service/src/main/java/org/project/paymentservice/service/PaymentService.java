package org.project.paymentservice.service;

public interface PaymentService {

    void handleOrderCreatedEvent();

    void handlePaymentSuccess();

    void handlePaymentFailure();
}

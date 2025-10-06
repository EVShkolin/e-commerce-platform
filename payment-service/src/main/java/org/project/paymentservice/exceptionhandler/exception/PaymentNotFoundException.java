package org.project.paymentservice.exceptionhandler.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String id) {
        super("Payment not found: " + id);
    }

}

package org.project.paymentservice.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.paymentservice.dto.OrderDto;
import org.project.paymentservice.dto.PaymentSession;
import org.project.paymentservice.exceptionhandler.exception.PaymentNotFoundException;
import org.project.paymentservice.model.Payment;
import org.project.paymentservice.model.PaymentStatus;
import org.project.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    @Override
    public PaymentSession createPayment(@Valid OrderDto orderDto) {
        Payment payment = paymentRepository.save(new Payment(
                orderDto.getId(),
                orderDto.getUserId(),
                orderDto.getTotalAmount(),
                PaymentStatus.CREATED
        ));

        Session session = stripeService.createCheckoutSession(orderDto.getItems());
        log.warn(session.getPaymentIntent());
        payment.setStripePaymentIntentId(session.getPaymentIntent());
        payment = paymentRepository.save(payment);
        log.info("Payment {} created with intent id {}", payment.getId(), payment.getStripePaymentIntentId());
        return new PaymentSession(payment.getId(), session.getUrl(), payment.getTotalPrice());
    }

    @Override
    public void processStripeEvent(String payload, String sigHeader) {
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new SecurityException("Webhook signature verification failed " + e);
        }

        handleStripeEvent(event);
    }

    @Override
    public void processTestEvent(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId.toString()));
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        log.info("Updated payment status to paid id:{}", payment.getId());
    }

    private void handleStripeEvent(Event event) {
        log.info("Received stripe event {} id: {}", event.getType(), event.getId());

        switch (event.getType()) {
            case "payment_intent.succeeded" -> handleSuccessPaymentEvent(event);

            default -> log.warn("Unhandled stripe event type {}", event.getType());
        }
    }

    private void handleSuccessPaymentEvent(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
        Payment payment = paymentRepository.findPaymentByStripePaymentIntentId(paymentIntent.getId())
                .orElseThrow(() -> new PaymentNotFoundException(paymentIntent.getId()));
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
    }


}

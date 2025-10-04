package org.project.paymentservice.controller;

import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.project.paymentservice.dto.OrderDto;
import org.project.paymentservice.dto.PaymentSession;
import org.project.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentSession> createPayment(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(paymentService.createPayment(orderDto), HttpStatus.CREATED);
    }

    @PostMapping("/webhook/stripe")
    public ResponseEntity<Void> processStripeEvent(@RequestBody String payload,
                                                   @RequestHeader("Stripe-Signature") String sigHeader) {
        paymentService.processStripeEvent(payload, sigHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/webhook/test")
    public ResponseEntity<Void> processTestEvent(@RequestBody UUID paymentId) {
        paymentService.processTestEvent(paymentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

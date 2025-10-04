package org.project.paymentservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.project.paymentservice.dto.OrderItemDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StripeService {

    public Session createCheckoutSession(List<OrderItemDto> items) {
        var params = SessionCreateParams.builder()
                .setSuccessUrl("https://example.com/success")
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addAllLineItem(items.stream()
                        .map(item -> SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("USD")
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProductName())
                                                            .build()
                                            )
                                            .setUnitAmount(item.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                            .build()
                                )
                                .setQuantity((long) item.getQuantity())
                                .build())
                        .toList())
                .build();


        try {
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}

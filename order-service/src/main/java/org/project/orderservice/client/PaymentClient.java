package org.project.orderservice.client;

import org.project.orderservice.dto.OrderDto;
import org.project.orderservice.dto.PaymentSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(@Value("${service.payment.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public PaymentSession createPayment(OrderDto orderDto) {
        return restClient.post()
                .body(orderDto)
                .retrieve()
                .body(PaymentSession.class);
    }
}

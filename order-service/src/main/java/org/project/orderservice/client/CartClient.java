package org.project.orderservice.client;

import org.project.orderservice.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CartClient {

    private final RestClient restClient;

    public CartClient(@Value("${service.shopping-cart.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public OrderDto getCartByUserId(String userId) {
        return restClient.get()
                .uri("/{userId}", userId)
                .retrieve()
                .body(OrderDto.class);
    }

}

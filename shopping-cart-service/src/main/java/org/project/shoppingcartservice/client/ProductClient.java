package org.project.shoppingcartservice.client;

import org.project.shoppingcartservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductClient {

    private final RestClient restClient;


    public ProductClient(@Value("${service.product-catalog.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<ProductDto> findProductsByIds(List<Long> productIds) {
        return restClient.post()
                .uri("/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(productIds)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}

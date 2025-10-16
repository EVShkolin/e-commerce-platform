package org.project.orderservice.client;

import org.project.orderservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${service.user.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String getUserEmail(String userId) {
        UserDto userDto =  restClient.get()
                .uri("/{userId}", userId)
                .retrieve()
                .body(UserDto.class);

        return userDto.getEmail();
    }
}

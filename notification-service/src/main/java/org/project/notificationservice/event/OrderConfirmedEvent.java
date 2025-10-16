package org.project.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderConfirmedEvent {

    private String userId;

    private String email;

    private Long orderId;

}

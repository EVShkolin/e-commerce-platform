package org.project.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.project.orderservice.model.OrderStatus;

@Data
@AllArgsConstructor
public class StatusUpdateRequest {

    @NotNull
    private OrderStatus status;

}

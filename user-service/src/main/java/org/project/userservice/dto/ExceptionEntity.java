package org.project.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ExceptionEntity {
    private Integer code;
    private String message;
    private LocalDateTime timestamp;
}

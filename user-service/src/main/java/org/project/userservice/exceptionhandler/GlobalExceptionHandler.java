package org.project.userservice.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.project.userservice.dto.ExceptionEntity;
import org.project.userservice.exceptionhandler.exception.UserNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ExceptionEntity> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn(ex.getMessage());
        var exceptionEntity = new ExceptionEntity(404, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionEntity, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<ExceptionEntity> handleDuplicateKeyException(DuplicateKeyException ex) {
        var exceptionEntity = new ExceptionEntity(400, "Email already exists", LocalDateTime.now());
        return new ResponseEntity<>(exceptionEntity, HttpStatus.BAD_REQUEST);
    }

}

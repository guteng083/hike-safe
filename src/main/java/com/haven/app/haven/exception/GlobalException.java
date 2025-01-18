package com.haven.app.haven.exception;

import com.haven.app.haven.dto.response.ErrorResponse;
import com.haven.app.haven.dto.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        Map<String, List<String>> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.computeIfAbsent(fieldName, k -> new ArrayList<>())
                    .add(errorMessage);
        });

        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .message("Register failed")
                .errors(Collections.singletonList(errors))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            ValidationException exception
    ) {
        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .message(exception.getMessage())
                .errors(Collections.singletonList(exception.getErrors()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("credentials error")
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}

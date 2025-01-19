package com.haven.app.haven.exception;

import com.haven.app.haven.dto.response.ErrorResponse;
import com.haven.app.haven.dto.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(
            PaymentException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("payment error")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("not found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(
            jakarta.validation.ConstraintViolationException exception
    ) {
        Map<String, List<String>> errors = new HashMap<>();

        exception.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            String errorMessage = violation.getMessage();

            errors.computeIfAbsent(fieldName, k -> new ArrayList<>())
                    .add(errorMessage);
        });

        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .message("Validation failed")
                .errors(Collections.singletonList(errors))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ValidationErrorResponse> handleMissingParams(
            MissingServletRequestParameterException exception
    ) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(exception.getParameterName(),
                Collections.singletonList(exception.getParameterName() + " parameter is required"));

        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .message("Missing Required Parameters")
                .errors(Collections.singletonList(errors))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("internal server error")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

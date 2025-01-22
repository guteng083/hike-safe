package com.haven.app.haven.exception;

import com.haven.app.haven.dto.response.ErrorResponse;
import com.haven.app.haven.dto.response.ValidationErrorResponse;
import com.haven.app.haven.utils.LogUtils;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import java.util.*;

@ControllerAdvice
@Hidden
//@Order(Ordered.HIGHEST_PRECEDENCE)
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

    @ExceptionHandler(CoordinateException.class)
    public ResponseEntity<ErrorResponse> handleCoordinateException(
            CoordinateException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("coordinate error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PriceException.class)
    public ResponseEntity<ErrorResponse> handlePriceException(
            PriceException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("price error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TrackerDeviceException.class)
    public ResponseEntity<ErrorResponse> handleTrackerDeviceException(
            TrackerDeviceException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("tracker device error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TransactionsException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(
            TransactionsException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("transactions error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
            MissingRequestHeaderException exception
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("Register Error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException exception
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("Cannot Access Because your Role")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException exception
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("Validation Error")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(
            MultipartException exception
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("Failed Upload Image")
                .build();
        LogUtils.getError("Not Multipart File",exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception
    ){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error("Failed to Get Parameter")
                .build();
        LogUtils.getError("Param Not Found",exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}

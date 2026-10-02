package com.gilead.orders.handler;

import com.gilead.orders.api.ErrorResponse;
import com.gilead.orders.exception.InsufficientStockException;
import com.gilead.orders.exception.InvalidOrderRequestException;
import com.gilead.orders.exception.OrderException;
import com.gilead.orders.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * One handler for the whole sealed hierarchy.
 *
 * <p>The switch is exhaustive: every subtype permitted by {@link OrderException}
 * must appear. That is the practical payoff of a sealed exception. A new domain
 * failure cannot ship with a generic 500 because the compiler rejects the handler
 * until this method maps it.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException exception) {
        return switch (exception) {
            case OrderNotFoundException notFound -> respond(
                    HttpStatus.NOT_FOUND,
                    notFound,
                    Map.of("orderId", notFound.orderId()));
            case InsufficientStockException stock -> respond(
                    HttpStatus.CONFLICT,
                    stock,
                    Map.of(
                            "sku", stock.sku(),
                            "requested", stock.requested(),
                            "available", stock.available()));
            case InvalidOrderRequestException invalid -> respond(
                    HttpStatus.BAD_REQUEST,
                    invalid,
                    Map.of("field", invalid.field()));
        };
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String field = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField())
                .orElse("request");
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Request is invalid");
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("INVALID_ORDER", message, Map.of("field", field)));
    }

    private static ResponseEntity<ErrorResponse> respond(
            HttpStatus status,
            OrderException exception,
            Map<String, Object> details) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(exception.errorCode(), exception.getMessage(), details));
    }
}

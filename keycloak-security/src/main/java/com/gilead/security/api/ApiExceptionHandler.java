package com.gilead.security.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiProblem> invalid(MethodArgumentNotValidException exception) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return problem(HttpStatus.BAD_REQUEST, detail);
    }

    /**
     * Method security throws inside the controller, after the filter chain.
     * Keep the body identical to {@code ApiAccessDeniedHandler}.
     */
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiProblem> denied(AccessDeniedException exception) {
        return problem(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
    }

    private static ResponseEntity<ApiProblem> problem(HttpStatus status, String detail) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ApiProblem.of(status, detail));
    }
}

package com.gilead.security.api;

import org.springframework.http.HttpStatus;

/**
 * Small RFC 7807 body shared by the security filter and the MVC error handler.
 */
public record ApiProblem(String type, String title, int status, String detail) {

    public static ApiProblem of(HttpStatus status, String detail) {
        return new ApiProblem("about:blank", status.getReasonPhrase(), status.value(), detail);
    }
}

package com.gilead.security.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNoticeRequest(
        @NotBlank @Size(max = 280) String text) {
}

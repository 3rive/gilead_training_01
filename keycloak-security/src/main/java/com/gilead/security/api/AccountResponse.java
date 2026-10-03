package com.gilead.security.api;

import java.util.List;

public record AccountResponse(String subject, String username, List<String> roles, List<String> scopes) {
}

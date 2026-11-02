package com.karina.onlinestore.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {}

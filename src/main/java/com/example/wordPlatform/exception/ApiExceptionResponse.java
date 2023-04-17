package com.example.wordPlatform.exception;

import java.time.LocalDateTime;

public record ApiExceptionResponse (
        LocalDateTime timestamp,
        int status,
        Object error
) {}

package com.example.word_platform.exception;

import java.time.LocalDateTime;

public record ApiExceptionResponse (
        LocalDateTime timestamp,
        int status,
        Object error
) {}

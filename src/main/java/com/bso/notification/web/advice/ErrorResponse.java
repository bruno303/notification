package com.bso.notification.web.advice;

import java.time.LocalDateTime;

public record ErrorResponse(
    String message,
    LocalDateTime timestamp,
    String path
) { }

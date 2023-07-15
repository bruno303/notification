package com.bso.notification.commons.exception;

public final class ExceptionUtils {
    private ExceptionUtils() {}

    public static IllegalStateException error(String messageFormat, Object... args) {
        return new IllegalStateException(String.format(messageFormat, args));
    }
}

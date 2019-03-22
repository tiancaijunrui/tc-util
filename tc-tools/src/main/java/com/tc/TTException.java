package com.tc;

public class TTException extends RuntimeException {
    private final TTErrorCode errorCode;

    public TTException(TTErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public TTException(TTErrorCode errorCode, String message, Throwable cause) {
        super("=================>TTException error" + message, cause);
        this.errorCode = errorCode;
    }

    public TTErrorCode getErrorCode() {
        return errorCode;
    }
}



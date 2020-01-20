package com.tc;

public class TcException extends RuntimeException {
    private final TcErrorCode errorCode;

    public TcException(TcErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public TcException(TcErrorCode errorCode, String message, Throwable cause) {
        super("=================>TcException error" + message, cause);
        this.errorCode = errorCode;
    }

    public TcErrorCode getErrorCode() {
        return errorCode;
    }
}



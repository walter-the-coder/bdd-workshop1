package com.bdd.workshop.exceptionHandling;

import org.springframework.http.HttpStatus;

public class CustomRuntimeException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    private final Throwable cause;
    private final HttpStatus httpStatus;

    public CustomRuntimeException(
        String errorCode,
        String errorMessage,
        Throwable cause,
        HttpStatus httpStatus
    ) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.cause = cause;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

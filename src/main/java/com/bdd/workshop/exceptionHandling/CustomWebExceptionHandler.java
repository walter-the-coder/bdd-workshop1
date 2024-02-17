package com.bdd.workshop.exceptionHandling;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.WebUtils;

@RestControllerAdvice
public class CustomWebExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebExceptionHandler.class);

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
        CustomRuntimeException ex,
        HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getErrorMessage()
        );

        return createResponseEntity(errorResponse, ex.getHttpStatus(), ex, request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEndpointNotFoundException(
        NoHandlerFoundException ex,
        HttpServletRequest request
    ) {
        String formattedMessage =
            String.format("Endpoint %s %s does not exist", ex.getHttpMethod(), ex.getRequestURL());
        ErrorResponse errorResponse = new ErrorResponse("UNKNOWN_ENDPOINT", formattedMessage);

        return createResponseEntity(errorResponse, HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Internal error",
            ex.getMessage()
        );

        return createResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(
        ErrorResponse errorResponse,
        HttpStatus statuskode,
        Exception exception,
        HttpServletRequest request
    ) {
        if (statuskode == HttpStatus.INTERNAL_SERVER_ERROR) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception);
        }

        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String remoteHost = request.getRemoteHost();

        if (statuskode.is5xxServerError()) {
            LOGGER.error(
                "Unhandeled error thrown in controller: " + method + " " + requestURI + " - (client: " + remoteHost
                    + ")"
            );
        } else {
            LOGGER.warn(
                "Unhandeled error thrown in controller: " + method + " " + requestURI + " - (client: " + remoteHost
                    + ")"
            );
        }

        return ResponseEntity
            .status(statuskode)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

}

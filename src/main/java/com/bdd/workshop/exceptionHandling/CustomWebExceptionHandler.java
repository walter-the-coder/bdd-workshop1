package com.bdd.workshop.exceptionHandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.WebUtils;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomWebExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebExceptionHandler.class);

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomRuntimeException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getErrorMessage()
        );

        return createResponseEntity(errorResponse, ex.getHttpStatus(), ex, request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(
        responseCode = "404",
        description = "The endpoint you are trying to reach does not exist"
    )
    public ResponseEntity<ErrorResponse> handleEndpointNotFoundException(
        NoResourceFoundException ex,
        HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
            "UNKNOWN_ENDPOINT",
            "Endpoint %s %s does not exist".formatted(ex.getHttpMethod(), ex.getResourcePath())
        );

        return createResponseEntity(errorResponse, HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(responseCode = "500", description = "Internal error in the service")
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

package com.orbsec.licensingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LicenseErrorHandler {

    private CustomError errorGenerator(Exception e, HttpStatus statusCode) {
        CustomError error = new CustomError();
        error.setErrorMessage(e.getMessage());
        error.setStatusCode(statusCode.value());
        error.setTimestamp(System.currentTimeMillis());
        return error;
    }

    @ExceptionHandler(MissingLicenseException.class)
    public ResponseEntity<CustomError> registrationErrorHandler(MissingLicenseException exception) {
        var error = errorGenerator(exception, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CustomError> unauthorizedExceptionHandler(UnauthorizedException exception) {
        var error = errorGenerator(exception, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RedisConnectionException.class)
    public ResponseEntity<CustomError> registrationErrorHandler(RedisConnectionException exception) {
        var error = errorGenerator(exception, HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvalidLicenseException.class)
    public ResponseEntity<CustomError> invalidLicenseErrorHandler(InvalidLicenseException exception) {
        var error = errorGenerator(exception, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

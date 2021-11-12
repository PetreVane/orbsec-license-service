package com.orbsec.licensingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LicenseErrorHandler {

    private LicenseError errorGenerator(Exception e, HttpStatus statusCode) {
        LicenseError error = new LicenseError();
        error.setErrorMessage(e.getMessage());
        error.setStatusCode(statusCode.value());
        error.setTimestamp(System.currentTimeMillis());
        return error;
    }

    @ExceptionHandler(MissingLicenseException.class)
    public ResponseEntity<LicenseError> registrationErrorHandler(MissingLicenseException exception) {
        var error = errorGenerator(exception, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

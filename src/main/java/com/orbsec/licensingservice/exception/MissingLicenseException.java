package com.orbsec.licensingservice.exception;

public class MissingLicenseException extends RuntimeException {
    public MissingLicenseException(String message) {
        super(message);
    }
}

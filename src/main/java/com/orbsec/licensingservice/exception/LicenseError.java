package com.orbsec.licensingservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseError {
    private String errorMessage;
    private int statusCode;
    private Long timestamp;
}

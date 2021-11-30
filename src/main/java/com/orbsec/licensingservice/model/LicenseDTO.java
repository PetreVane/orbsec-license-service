package com.orbsec.licensingservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class LicenseDTO {

    private String licenseId;

    private String description;

    private String organizationId;

    private String productName;

    private String licenseType;

    private String comment;

    private String organizationName;

    private String contactName;

    private String contactPhone;

    private String contactEmail;
}

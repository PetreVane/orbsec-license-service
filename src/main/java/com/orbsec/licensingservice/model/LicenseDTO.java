package com.orbsec.licensingservice.model;


import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
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

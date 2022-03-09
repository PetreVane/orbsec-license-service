package com.orbsec.licensingservice.model;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class LicenseDTO {

    private String licenseId;

    @NotEmpty
    private String description;

    @NotEmpty
    private String organizationId;

    @NotEmpty
    private String productName;

    @NotEmpty
    private String licenseType;

    @NotEmpty
    private String comment;

    private String organizationName;

    private String contactName;

    private String contactPhone;

    private String contactEmail;
}

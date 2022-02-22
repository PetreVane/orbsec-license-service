package com.orbsec.licensingservice.model;


import lombok.*;

import javax.validation.constraints.Email;
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

    @NotEmpty
    private String organizationName;

    @NotEmpty
    private String contactName;

    @NotEmpty
    private String contactPhone;

    @Email
    private String contactEmail;
}

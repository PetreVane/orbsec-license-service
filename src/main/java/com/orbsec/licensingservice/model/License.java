package com.orbsec.licensingservice.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;


@Entity
@Getter @Setter @ToString @NoArgsConstructor
public class License {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;

    public License(String licenseId, String description, String organizationId, String productName, String licenseType) {
        this.licenseId = licenseId;
        this.description = description;
        this.organizationId = organizationId;
        this.productName = productName;
        this.licenseType = licenseType;
    }
}

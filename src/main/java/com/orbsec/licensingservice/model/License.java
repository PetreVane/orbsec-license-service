package com.orbsec.licensingservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "licenses")
@Getter @Setter @ToString @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class License extends RepresentationModel<License> {

    /*
    By using an auto-generated uuid, will make testing repository findById method difficult

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
     */
    @Id
    @Column(name= "license_id", nullable = false, unique = true)
    private String licenseId;

    @Column(nullable = false)
    private String description;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "license_type")
    private String licenseType;

    @Column(name="comment")
    private String comment;

    @Transient
    private String organizationName;

    @Transient
    private String contactName;

    @Transient
    private String contactPhone;

    @Transient
    private String contactEmail;

    public License withComment(String comment){
        this.setComment(comment);
        return this;
    }

    public License(String licenseId, String description, String organizationId, String productName, String licenseType) {
        this.licenseId = licenseId;
        this.description = description;
        this.organizationId = organizationId;
        this.productName = productName;
        this.licenseType = licenseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        License license = (License) o;
        return licenseId != null && Objects.equals(licenseId, license.licenseId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

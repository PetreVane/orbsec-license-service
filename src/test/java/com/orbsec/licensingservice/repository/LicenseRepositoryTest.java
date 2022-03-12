package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.model.License;

import java.util.Collection;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
class LicenseRepositoryTest {

    @Autowired
    private LicenseRepository licenseRepository;

    @Test
    @Description("It should find License by id")
    void itShouldFindLicenseByLicenseId() {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("Description");
        license.setLicenseId("12342");
        license.setLicenseType("License Type");
        license.setOrganizationId("12342");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");

        this.licenseRepository.save(license);
        assertTrue(this.licenseRepository.findLicenseByLicenseId("12342").isPresent());
    }


    @Test
    @Description("It should find License by Organization id")
    void itShouldFindAllByOrganizationId() {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("Description");
        license.setLicenseId("12342");
        license.setLicenseType("License Type");
        license.setOrganizationId("12342");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");

        this.licenseRepository.save(license);

        List<License> actualResult = this.licenseRepository.findAllByOrganizationId("12342");
        assertTrue(actualResult.contains(license));
    }


    @Test
    @Description("It should find all Licenses")
    void testFindAll() {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("Description");
        license.setLicenseId("12342");
        license.setLicenseType("License Type");
        license.setOrganizationId("12342");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");

        this.licenseRepository.save(license);
        assertEquals(1, ((Collection<License>) this.licenseRepository.findAll()).size());
        assertTrue(((Collection<License>) this.licenseRepository.findAll()).contains(license));
    }
}
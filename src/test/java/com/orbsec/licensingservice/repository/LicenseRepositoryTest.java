package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.model.License;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
class LicenseRepositoryTest {

    @Autowired
    private LicenseRepository underTest;

    @Test
    void itShouldFindLicenseByLicenseId() {
        // Given
        String testLicenseId = "testLicense id";
        License testLicense = new License(testLicenseId, "Test description", "Test Corp", "Test Product", "Test type");
        // When
        underTest.save(testLicense);

        // Then
        assertThat(underTest.findLicenseByLicenseId("Incorrect id")).isNotPresent();
        assertThat(underTest.findLicenseByLicenseId(testLicenseId).isPresent());
        assertThat(underTest.findLicenseByLicenseId(testLicenseId).get()).isInstanceOf(License.class);
        assertThat(underTest.findLicenseByLicenseId(testLicenseId).get().getLicenseId().equals(testLicenseId));
    }
}
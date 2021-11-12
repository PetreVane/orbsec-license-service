package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.repository.LicenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;

@DataJpaTest
class LicenseServiceTest {

    @Autowired
    private LicenseRepository underTest;


    @Test
    void itShouldCreateLicense() {
        // Given
        License testLicense = new License("0000", "Under test license", "Under test Organization", "Under test product", "Under test license type");
        // When
        underTest.save(testLicense);
        // Then
        Optional<License> fetchedLicense = underTest.findLicenseByLicenseId(testLicense.getLicenseId());

        assertThat(fetchedLicense).get()
                .isEqualTo(testLicense);

    }


    @Test
    void itShouldDeleteLicense() {
        // Given
        License testLicense = new License("0000", "Under test license", "Under test Organization", "Under test product", "Under test license type");
        // When
        underTest.save(testLicense);
        License toBeDeletedLicense = underTest.findLicenseByLicenseId(testLicense.getLicenseId()).get();
        // Then
         underTest.delete(toBeDeletedLicense);
        Optional<License> emptyOptional = underTest.findLicenseByLicenseId(testLicense.getLicenseId());
        shouldBeEmpty(emptyOptional);
    }
}
package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.repository.LicenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.then;

import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
class LicenseServiceTest {

    @Captor
    ArgumentCaptor<License> licenseArgumentCaptor;

    private LicenseRepository licenseRepository = mock(LicenseRepository.class);

    @InjectMocks
    private LicenseService underTest;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itShouldCreateLicense() {
        // Given
        String organizationId = "Under test Organization";
        String licenseId = "0001";
        License testLicense = new License(licenseId, "Under test license", organizationId, "Under test product", "Under test license type");

        // When
        underTest.createLicense(testLicense, organizationId);
        // Then
        then(licenseRepository).should().save(licenseArgumentCaptor.capture());
        License fetchedLicense = licenseArgumentCaptor.getValue();
        assertThat(fetchedLicense).isEqualTo(testLicense).isInstanceOf(License.class);
    }


    @Test
    void itShouldThrowException() {
        // Given
        String organizationId = "Under test Organization";
        String licenseId = "0001";
        // Then
        assertThatThrownBy(() -> underTest.deleteLicense(licenseId, organizationId)).isInstanceOf(MissingLicenseException.class);
    }

    @Test
    void itShouldDeleteLicense() {
        // Given
        String organizationId = "Under test Organization";
        String licenseId = "0001";
        License testLicense = new License(licenseId, "Under test license", organizationId, "Under test product", "Under test license type");

        // When
        when(licenseRepository.findLicenseByLicenseId(licenseId)).thenReturn(Optional.of(testLicense));
        underTest.deleteLicense(licenseId, organizationId);
        // Then
        verify(licenseRepository).delete(testLicense);
    }

    @Test
    void itShouldUpdateLicense() {
        // Given
        String organizationId = "Under test Organization";
        String licenseId = "0001";
        License testLicense = new License(licenseId, "Under test license", organizationId, "Under test product", "Under test license type");

        // When
        when(licenseRepository.findLicenseByLicenseId(licenseId)).thenReturn(Optional.of(testLicense));
        // Then
        underTest.updateLicense(testLicense, organizationId);
        verify(licenseRepository).save(testLicense);
        verify(licenseRepository).findLicenseByLicenseId(licenseId);
    }
}


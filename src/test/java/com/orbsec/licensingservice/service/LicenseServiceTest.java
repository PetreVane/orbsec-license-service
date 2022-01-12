package com.orbsec.licensingservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.repository.LicenseRepository;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LicenseService.class})
@ExtendWith(SpringExtension.class)
class LicenseServiceTest {
    @MockBean
    private LicenseRepository licenseRepository;

    @Autowired
    private LicenseService licenseService;

    @Test
    void itShouldMapDto() {
        // Given
        LicenseDTO licenseDTO = new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org");

        // When
        License actualMapDtoResult = this.licenseService.mapDto(licenseDTO);

        // Then
        assertEquals("Comment", actualMapDtoResult.getComment());
        assertEquals("Product Name", actualMapDtoResult.getProductName());
        assertEquals("Organization Name", actualMapDtoResult.getOrganizationName());
        assertEquals("42", actualMapDtoResult.getOrganizationId());
        assertEquals("License Type", actualMapDtoResult.getLicenseType());
        assertEquals("42", actualMapDtoResult.getLicenseId());
        assertEquals("The characteristics of someone or something", actualMapDtoResult.getDescription());
        assertEquals("4105551212", actualMapDtoResult.getContactPhone());
        assertEquals("Contact Name", actualMapDtoResult.getContactName());
        assertEquals("jane.doe@example.org", actualMapDtoResult.getContactEmail());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }


    @Test
    void itShouldMapLicense() {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("The characteristics of someone or something");
        license.setLicenseId("42");
        license.setLicenseType("License Type");
        license.setOrganizationId("42");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");

        LicenseDTO actualMapLicenseResult = this.licenseService.mapLicense(license);

        assertEquals("Comment", actualMapLicenseResult.getComment());
        assertEquals("Product Name", actualMapLicenseResult.getProductName());
        assertEquals("Organization Name", actualMapLicenseResult.getOrganizationName());
        assertEquals("42", actualMapLicenseResult.getOrganizationId());
        assertEquals("License Type", actualMapLicenseResult.getLicenseType());
        assertEquals("42", actualMapLicenseResult.getLicenseId());
        assertEquals("The characteristics of someone or something", actualMapLicenseResult.getDescription());
        assertEquals("4105551212", actualMapLicenseResult.getContactPhone());
        assertEquals("Contact Name", actualMapLicenseResult.getContactName());
        assertEquals("jane.doe@example.org", actualMapLicenseResult.getContactEmail());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    void itShouldCreateLicense() {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("The characteristics of someone or something");
        license.setLicenseId("42");
        license.setLicenseType("License Type");
        license.setOrganizationId("42");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");
        when(this.licenseRepository.save(any())).thenReturn(license);
        LicenseDTO licenseDTO = new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org");

        String organizationId = "42";
        String actualCreateLicenseResult = this.licenseService.createLicense(licenseDTO, organizationId);

        assertEquals("License has been saved for organizationId: [42]", actualCreateLicenseResult);
        verify(this.licenseRepository).save(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }


    @Test
    void itShouldGetLicenseByLicenseId() throws MissingLicenseException {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("The characteristics of someone or something");
        license.setLicenseId("42");
        license.setLicenseType("License Type");
        license.setOrganizationId("42");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");
        Optional<License> ofResult = Optional.of(license);
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(ofResult);
        String licenseId = "42";

        LicenseDTO actualLicenseByLicenseId = this.licenseService.getLicenseByLicenseId(licenseId);

        assertEquals("Comment", actualLicenseByLicenseId.getComment());
        assertEquals("Product Name", actualLicenseByLicenseId.getProductName());
        assertEquals("Organization Name", actualLicenseByLicenseId.getOrganizationName());
        assertEquals("42", actualLicenseByLicenseId.getOrganizationId());
        assertEquals("License Type", actualLicenseByLicenseId.getLicenseType());
        assertEquals("42", actualLicenseByLicenseId.getLicenseId());
        assertEquals("The characteristics of someone or something", actualLicenseByLicenseId.getDescription());
        assertEquals("4105551212", actualLicenseByLicenseId.getContactPhone());
        assertEquals("Contact Name", actualLicenseByLicenseId.getContactName());
        assertEquals("jane.doe@example.org", actualLicenseByLicenseId.getContactEmail());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }


    @Test
    void itShouldGetLicensesByOrganizationId() {
        when(this.licenseRepository.findAllByOrganizationId(any())).thenReturn(new ArrayList<>());
        String organizationId = "42";

        List<LicenseDTO> actualLicensesByOrganizationId = this.licenseService.getLicensesByOrganizationId(organizationId);

        assertTrue(actualLicensesByOrganizationId.isEmpty());
        verify(this.licenseRepository).findAllByOrganizationId(any());
        assertEquals(actualLicensesByOrganizationId, this.licenseService.getAllLicenses());
    }


    @Test
    void itShouldGetAllLicenses() {
        when(this.licenseRepository.findAllLicenses()).thenReturn(new ArrayList<>());

        List<LicenseDTO> actualAllLicenses = this.licenseService.getAllLicenses();

        assertTrue(actualAllLicenses.isEmpty());
        verify(this.licenseRepository).findAllLicenses();
        assertEquals(actualAllLicenses, this.licenseService.getAllLicenses());
    }


    @Test
    void itShouldUpdateLicense() throws MissingLicenseException {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("The characteristics of someone or something");
        license.setLicenseId("42");
        license.setLicenseType("License Type");
        license.setOrganizationId("42");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");
        Optional<License> ofResult = Optional.of(license);

        License license1 = new License();
        license1.setComment("Comment");
        license1.setContactEmail("jane.doe@example.org");
        license1.setContactName("Contact Name");
        license1.setContactPhone("4105551212");
        license1.setDescription("The characteristics of someone or something");
        license1.setLicenseId("42");
        license1.setLicenseType("License Type");
        license1.setOrganizationId("42");
        license1.setOrganizationName("Organization Name");
        license1.setProductName("Product Name");
        when(this.licenseRepository.save(any())).thenReturn(license1);
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(ofResult);
        LicenseDTO licenseDTO = new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org");

        String newOrganizationId = "42";

        String actualUpdateLicenseResult = this.licenseService.updateLicense(licenseDTO, newOrganizationId);

        assertEquals("License [42] has been updated with organization id [42].", actualUpdateLicenseResult);
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        verify(this.licenseRepository).save(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    void itShouldDeleteLicense() throws MissingLicenseException {
        License license = new License();
        license.setComment("Comment");
        license.setContactEmail("jane.doe@example.org");
        license.setContactName("Contact Name");
        license.setContactPhone("4105551212");
        license.setDescription("The characteristics of someone or something");
        license.setLicenseId("42");
        license.setLicenseType("License Type");
        license.setOrganizationId("42");
        license.setOrganizationName("Organization Name");
        license.setProductName("Product Name");
        Optional<License> ofResult = Optional.of(license);
        doNothing().when(this.licenseRepository).delete(any());
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(ofResult);
        String licenseId = "42";

        String actualDeleteLicenseResult = this.licenseService.deleteLicense(licenseId);

        assertEquals("License with id [42] has been deleted successfully.", actualDeleteLicenseResult);
        verify(this.licenseRepository).delete(any());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

}


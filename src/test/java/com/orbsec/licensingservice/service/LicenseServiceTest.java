package com.orbsec.licensingservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.model.OrganizationDto;
import com.orbsec.licensingservice.repository.LicenseRepository;
import com.orbsec.licensingservice.service.client.OrganizationFeignClient;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
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

    @MockBean
    private OrganizationFeignClient organizationFeignClient;

    @MockBean
    private RedisService redisService;

    @Test
    @DisplayName("It should get updated Organization record")
    void itShouldGetUpdatedOrganizationRecord() {

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setContactEmail("jane.doe@example.org");
        organizationDto.setContactName("Contact Name");
        organizationDto.setContactPhone("4105551212");
        organizationDto.setId("12342");
        organizationDto.setName("Name");

        when(this.organizationFeignClient.getOrganization(any())).thenReturn(organizationDto);

        assertSame(organizationDto, this.licenseService.getUpdatedOrganizationRecord("12342"));
        verify(this.redisService).saveRecordIntoCache(any());
        verify(this.organizationFeignClient).getOrganization(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should throw MissingException  ")
    void itShouldNotGetUpdatedOrganizationRecord() {
        doNothing().when(this.redisService).saveRecordIntoCache(any());
        when(this.organizationFeignClient.getOrganization(any()))
                .thenThrow(new MissingLicenseException("An error occurred"));
        assertThrows(MissingLicenseException.class, () -> this.licenseService.getUpdatedOrganizationRecord("42"));
        verify(this.organizationFeignClient).getOrganization(any());
    }

    @Test
    @DisplayName("It should create License")
    void itShouldCreateLicense() {

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setContactEmail("jane.doe@example.org");
        organizationDto.setContactName("Contact Name");
        organizationDto.setContactPhone("4105551212");
        organizationDto.setId("12342");
        organizationDto.setName("Name");
        Optional<OrganizationDto> optionalResult = Optional.of(organizationDto);

        when(this.redisService.checkCacheFor(any())).thenReturn(optionalResult);
        doNothing().when(this.redisService).saveRecordIntoCache(any());

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

        when(this.licenseRepository.save(any())).thenReturn(license);
        assertEquals("A new license has been saved for organizationId 12342 ",
                this.licenseService.createLicense(
                        new LicenseDTO("12342", "Description", "42", "Product Name", "License Type",
                                "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org"),
                        "12342"));
        verify(this.redisService).checkCacheFor(any());
        verify(this.licenseRepository).save(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should throw MissingLicenseException")
    void itShouldNotCreateLicense() {

        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setLicenseId("12342");
        licenseDTO.setDescription("Description");
        licenseDTO.setProductName("Product Name");
        licenseDTO.setLicenseType("License Type");
        licenseDTO.setComment("Comment");
        licenseDTO.setOrganizationName("Organization Name");
        licenseDTO.setContactName("Contact Name");
        licenseDTO.setContactPhone("4105551212");
        licenseDTO.setContactEmail("jane.doe@example.org");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setContactEmail("jane.doe@example.org");
        organizationDto.setContactName("Contact Name");
        organizationDto.setContactPhone("4105551212");
        organizationDto.setId("12342");
        organizationDto.setName("Name");
        Optional<OrganizationDto> optionalResult = Optional.of(organizationDto);
        when(this.redisService.checkCacheFor(any())).thenReturn(optionalResult);

        doNothing().when(this.redisService).saveRecordIntoCache(any());

        when(this.licenseRepository.save(any())).thenThrow(new MissingLicenseException("An error occurred"));
        assertThrows(MissingLicenseException.class, () -> this.licenseService.createLicense(licenseDTO, "12342"));
        verify(this.redisService).checkCacheFor(any());
        verify(this.licenseRepository).save(any());
    }

    @Test
    @DisplayName("It should get License by id")
    void itShouldGetLicenseByLicenseId() throws MissingLicenseException {

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
        Optional<License> optionalResult = Optional.of(license);
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);

        LicenseDTO actualLicenseByLicenseId = this.licenseService.getLicenseByLicenseId("12342");
        assertEquals("Comment", actualLicenseByLicenseId.getComment());
        assertEquals("Product Name", actualLicenseByLicenseId.getProductName());
        assertEquals("Organization Name", actualLicenseByLicenseId.getOrganizationName());
        assertEquals("12342", actualLicenseByLicenseId.getOrganizationId());
        assertEquals("License Type", actualLicenseByLicenseId.getLicenseType());
        assertEquals("12342", actualLicenseByLicenseId.getLicenseId());
        assertEquals("Description", actualLicenseByLicenseId.getDescription());
        assertEquals("4105551212", actualLicenseByLicenseId.getContactPhone());
        assertEquals("Contact Name", actualLicenseByLicenseId.getContactName());
        assertEquals("jane.doe@example.org", actualLicenseByLicenseId.getContactEmail());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should throw MissingLicenseException")
    void itShouldNotGetLicenseByLicenseId() throws MissingLicenseException {
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(Optional.empty());
        assertThrows(MissingLicenseException.class, () -> this.licenseService.getLicenseByLicenseId(any()));
        verify(this.licenseRepository).findLicenseByLicenseId(any());
    }

    @Test
    @DisplayName("It should get all Licenses by Organization id")
    void itShouldGetLicensesByOrganizationId() {
        when(this.licenseRepository.findAllByOrganizationId(any())).thenReturn(new ArrayList<>());
        List<LicenseDTO> actualLicensesByOrganizationId = this.licenseService.getLicensesByOrganizationId(any());
        assertTrue(actualLicensesByOrganizationId.isEmpty());
        verify(this.licenseRepository).findAllByOrganizationId(any());
        assertEquals(actualLicensesByOrganizationId, this.licenseService.getAllLicenses());
    }

    @Test
    @DisplayName("It should get one License by Organization id")
    void itShouldGetOneLicensesByOrganizationId() {
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

        ArrayList<License> licenseList = new ArrayList<>();
        licenseList.add(license);
        when(this.licenseRepository.findAllByOrganizationId(any())).thenReturn(licenseList);
        assertEquals(1, this.licenseService.getLicensesByOrganizationId("12342").size());
        verify(this.licenseRepository).findAllByOrganizationId(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should throw MissingLicenseException when asking for one License")
    void itShouldNotGetLicensesByOrganizationId() {
        when(this.licenseRepository.findAllByOrganizationId(any()))
                .thenThrow(new MissingLicenseException("An error occurred"));
        assertThrows(MissingLicenseException.class, () -> this.licenseService.getLicensesByOrganizationId(any()));
        verify(this.licenseRepository).findAllByOrganizationId(any());
    }

    @Test
    @DisplayName("It should throw MissingLicenseException when asking for all Licenses")
    void itShouldGetAllLicenses() {
        when(this.licenseRepository.findAll()).thenThrow(new MissingLicenseException("An error occurred"));
        assertThrows(MissingLicenseException.class, () -> this.licenseService.getAllLicenses());
        verify(this.licenseRepository).findAll();
    }

    @Test
    @DisplayName("It should update License")
    void itShouldUpdateLicenseForOrganization() throws MissingLicenseException {
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

        LicenseDTO licenseDTO = new LicenseDTO();
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

        Optional<License> optionalResult = Optional.of(license);

        when(this.licenseRepository.save(any())).thenReturn(license);
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);
        assertEquals(licenseDTO, this.licenseService.updateLicenseForOrganization("12342", "12342", licenseDTO));
        verify(this.licenseRepository).save(any());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should update License and validate updated License")
    void itShouldUpdateLicenseForOrganization2() {

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

        Optional<License> optionalResult = Optional.of(license);

        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setComment("Comment");
        licenseDTO.setContactEmail("jane.doe@example.org");
        licenseDTO.setContactName("Contact Name");
        licenseDTO.setContactPhone("4105551212");
        licenseDTO.setDescription("Description");
        licenseDTO.setLicenseId("12342");
        licenseDTO.setLicenseType("License Type");
        licenseDTO.setOrganizationId("12342");
        licenseDTO.setOrganizationName("Organization Name");
        licenseDTO.setProductName("Product Name");

        when(this.licenseRepository.save(any())).thenReturn(license);
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);
        LicenseDTO actualUpdateLicenseForOrganizationResult = this.licenseService.updateLicenseForOrganization("12342", "12342", licenseDTO);

        assertEquals("Comment", actualUpdateLicenseForOrganizationResult.getComment());
        assertEquals("Product Name", actualUpdateLicenseForOrganizationResult.getProductName());
        assertEquals("Organization Name", actualUpdateLicenseForOrganizationResult.getOrganizationName());
        assertEquals("12342", actualUpdateLicenseForOrganizationResult.getOrganizationId());
        assertEquals("License Type", actualUpdateLicenseForOrganizationResult.getLicenseType());
        assertEquals("12342", actualUpdateLicenseForOrganizationResult.getLicenseId());
        assertEquals("Description", actualUpdateLicenseForOrganizationResult.getDescription());
        assertEquals("4105551212", actualUpdateLicenseForOrganizationResult.getContactPhone());
        assertEquals("Contact Name", actualUpdateLicenseForOrganizationResult.getContactName());
        assertEquals("jane.doe@example.org", actualUpdateLicenseForOrganizationResult.getContactEmail());

        verify(this.licenseRepository).save(any());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should throw MissingLicenseException when trying to update License")
    void itShouldNotUpdateLicense() throws MissingLicenseException {

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
        Optional<License> optionalResult = Optional.of(license);

        LicenseDTO licenseDTO = new LicenseDTO();
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

        when(this.licenseRepository.save(any())).thenThrow(new MissingLicenseException("An error occurred"));
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);
        assertThrows(MissingLicenseException.class, () -> this.licenseService.updateLicenseForOrganization("12342", "12342", licenseDTO));
        verify(this.licenseRepository).save(any());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
    }

    @Test
    @DisplayName("It should delete License by id")
    void itShouldDeleteLicenseById() throws MissingLicenseException {

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
        Optional<License> optionalResult = Optional.of(license);

        doNothing().when(this.licenseRepository).delete(any());
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);
        assertEquals("License with id [12342] has been deleted successfully.", this.licenseService.deleteLicenseById("12342"));
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        verify(this.licenseRepository).delete(any());
        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

    @Test
    @DisplayName("It should throw MissingLicenseException when deleting License")
    void itShouldNotDeleteLicenseById() throws MissingLicenseException {
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
        Optional<License> optionalResult = Optional.of(license);

        doThrow(new MissingLicenseException("An error occurred")).when(this.licenseRepository).delete(any());
        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);
        assertThrows(MissingLicenseException.class, () -> this.licenseService.deleteLicenseById("12342"));
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        verify(this.licenseRepository).delete(any());
    }

    @Test
    @DisplayName("It should delete all Licenses for Organization by id")
    void itShouldDeleteAllLicensesForOrganization() {
        ArrayList<License> licenseList = new ArrayList<>();
        when(this.licenseRepository.findAllByOrganizationId(any())).thenReturn(licenseList);
        this.licenseService.deleteLicenseForOrganization("12342");
        verify(this.licenseRepository).findAllByOrganizationId(any());
        assertEquals(licenseList, this.licenseService.getAllLicenses());
    }

    @Test
    @DisplayName("It should delete all Licenses for Organization by id")
    void itShouldDeleteLicenseForOrganization() {

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

        ArrayList<License> licenseList = new ArrayList<>();
        licenseList.add(license);

        Optional<License> optionalResult = Optional.of(license);

        when(this.licenseRepository.findLicenseByLicenseId(any())).thenReturn(optionalResult);
        when(this.licenseRepository.findAllByOrganizationId(any())).thenReturn(licenseList);

        this.licenseService.deleteLicenseForOrganization("12342");

        verify(this.licenseRepository).findAllByOrganizationId(any());
        verify(this.licenseRepository).findLicenseByLicenseId(any());
        verify(this.licenseRepository).delete(any());

        assertTrue(this.licenseService.getAllLicenses().isEmpty());
    }

}


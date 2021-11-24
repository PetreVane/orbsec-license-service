package com.orbsec.licensingservice.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.repository.LicenseRepository;
import com.orbsec.licensingservice.service.LicenseService;

import java.util.Optional;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LicenseController.class})
@ExtendWith(SpringExtension.class)
class LicenseControllerTest {
    @Autowired
    private LicenseController licenseController;

    @MockBean
    private LicenseService licenseService;

    @Test
    void itShouldGetLicense() throws MissingLicenseException {
        // Given
        License license = new License();
        license.setId(UUID.randomUUID());
        license.setLicenseType("License Type");
        license.setOrganizationName("Organization Name");
        license.setContactName("Contact Name");
        license.setProductName("Product Name");
        license.setContactEmail("jane.doe@example.org");
        license.setDescription("The characteristics of someone or something");
        license.setOrganizationId("42");
        license.add(Link.of("Href"));
        license.setComment("Comment");
        license.setLicenseId("42");
        license.setContactPhone("4105551212");
        LicenseRepository licenseRepository = mock(LicenseRepository.class);
        // When
        when(licenseRepository.findLicenseByLicenseId(any())).thenReturn(Optional.of(license));
        ResponseEntity<License> actualLicense = (new LicenseController(new LicenseService(licenseRepository))).getLicense("42", "42");

        // Then
        assertThat(actualLicense.getHeaders().isEmpty()).isTrue();
        assertThat(actualLicense.hasBody()).isTrue();
        assertThat(actualLicense.getStatusCode()).isEqualTo(HttpStatus.OK);

        // When
        License body = actualLicense.getBody();
        // Then
        assertThat(body.getOrganizationId()).isEqualTo("42");
        assertThat(body.getLinks().toList().size()).isEqualTo(5);
        assertThat(body.hasLinks()).isTrue();
        verify(licenseRepository).findLicenseByLicenseId(any());
    }


    @Test
    void itShouldCreateLicense() throws Exception {
        // Given
        License license = new License();
        license.setId(UUID.randomUUID());
        license.setLicenseType("License Type");
        license.setOrganizationName("Organization Name");
        license.setContactName("Contact Name");
        license.setProductName("Product Name");
        license.setContactEmail("jane.doe@example.org");
        license.setDescription("The characteristics of someone or something");
        license.setOrganizationId("42");
        license.add(Link.of("Href"));
        license.setComment("Comment");
        license.setLicenseId("42");
        license.setContactPhone("4105551212");
        String content = (new ObjectMapper()).writeValueAsString(license);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/organization/{organizationId}/license", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder);
        // Then
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    @Test
    void itShouldDeleteLicense() throws Exception {
        // When
        when(this.licenseService.deleteLicense(any(), any())).thenReturn("Delete License");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/organization/{organizationId}/license/{licenseId}", "42", "42");
        // Then
        MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete License"));
    }


    @Test
    void itShouldUpdateLicense() throws Exception {
        // Given
        License license = new License();
        license.setId(UUID.randomUUID());
        license.setLicenseType("License Type");
        license.setOrganizationName("Organization Name");
        license.setContactName("Contact Name");
        license.setProductName("Product Name");
        license.setContactEmail("jane.doe@example.org");
        license.setDescription("The characteristics of someone or something");
        license.setOrganizationId("42");
        license.add(Link.of("Href"));
        license.setComment("Comment");
        license.setLicenseId("42");
        license.setContactPhone("4105551212");
        String content = (new ObjectMapper()).writeValueAsString(license);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/organization/{organizationId}/license", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder);
        // Then
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }
}


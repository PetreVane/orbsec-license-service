package com.orbsec.licensingservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(licenseRepository.findLicenseByLicenseId(any())).thenReturn(Optional.of(license));
        ResponseEntity<License> actualLicense = (new LicenseController(new LicenseService(licenseRepository)))
                .getLicense("42", "42");
        assertTrue(actualLicense.getHeaders().isEmpty());
        assertTrue(actualLicense.hasBody());
        assertEquals(HttpStatus.OK, actualLicense.getStatusCode());
        License body = actualLicense.getBody();
        assertEquals("42", body.getOrganizationId());
        assertEquals(5, body.getLinks().toList().size());
        assertTrue(body.hasLinks());
        verify(licenseRepository).findLicenseByLicenseId(any());
    }

    @Test
    void itShouldGetLicense2() throws MissingLicenseException {
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
        LicenseService licenseService = mock(LicenseService.class);
        when(licenseService.getLicense((String) any(), (String) any())).thenReturn(license);
        ResponseEntity<License> actualLicense = (new LicenseController(licenseService)).getLicense("42", "42");
        assertTrue(actualLicense.getHeaders().isEmpty());
        assertTrue(actualLicense.hasBody());
        assertEquals(HttpStatus.OK, actualLicense.getStatusCode());
        License body = actualLicense.getBody();
        assertEquals(5, body.getLinks().toList().size());
        assertTrue(body.hasLinks());
        verify(licenseService).getLicense((String) any(), (String) any());
    }

    @Test
    void itShouldCreateLicense() throws Exception {
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/organization/{organizationId}/license", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    @Test
    void itShouldDeleteLicense() throws Exception {
        when(this.licenseService.deleteLicense((String) any(), (String) any())).thenReturn("Delete License");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/organization/{organizationId}/license/{licenseId}", "42", "42");
        MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete License"));
    }

    @Test
    void itShouldDeleteLicense2() throws Exception {
        when(this.licenseService.deleteLicense((String) any(), (String) any())).thenReturn("Delete License");
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders
                .delete("/api/v1/organization/{organizationId}/license/{licenseId}", "42", "42");
        deleteResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete License"));
    }

    @Test
    void itShouldUpdateLicense() throws Exception {
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/organization/{organizationId}/license", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }
}


package com.orbsec.licensingservice.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.service.LicenseService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
    void itShouldCreateLicense() throws Exception {
        // Given
        when(this.licenseService.createLicense(any(), any())).thenReturn("Create License");

        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setComment("Comment");
        licenseDTO.setContactEmail("jane.doe@example.org");
        licenseDTO.setContactName("Contact Name");
        licenseDTO.setContactPhone("4105551212");
        licenseDTO.setDescription("The characteristics of someone or something");
        licenseDTO.setLicenseId("42");
        licenseDTO.setLicenseType("License Type");
        licenseDTO.setOrganizationId("42");
        licenseDTO.setOrganizationName("Organization Name");
        licenseDTO.setProductName("Product Name");
        String content = (new ObjectMapper()).writeValueAsString(licenseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/license/organization/{organizationId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        // When
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Then
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Create License"));
    }

    @Test
    void itShouldUpdateLicense() throws Exception {
        // When
        when(this.licenseService.updateLicense(any(), any())).thenReturn("2020-03-01");

        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setComment("Comment");
        licenseDTO.setContactEmail("jane.doe@example.org");
        licenseDTO.setContactName("Contact Name");
        licenseDTO.setContactPhone("4105551212");
        licenseDTO.setDescription("The characteristics of someone or something");
        licenseDTO.setLicenseId("42");
        licenseDTO.setLicenseType("License Type");
        licenseDTO.setOrganizationId("42");
        licenseDTO.setOrganizationName("Organization Name");
        licenseDTO.setProductName("Product Name");
        String content = (new ObjectMapper()).writeValueAsString(licenseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/license/organization/{organizationId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Then
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("2020-03-01"));
    }

    @Test
    void itShouldDeleteLicense() throws Exception {
        // When
        when(this.licenseService.deleteLicenseById(any())).thenReturn("Delete License");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/license/{licenseId}", "42");
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Then
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete License"));
    }

    @Test
    void itShouldFetchAllLicenses() throws Exception {
        // When
        when(this.licenseService.getAllLicenses()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/license/all");
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Then
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void itShouldGetAllLicensesByOrganizationId() throws Exception {
        when(this.licenseService.getLicensesByOrganizationId(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/license/organization/{organizationId}", "42");
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void itShouldGetAllLicensesByOrganizationId2() throws Exception {
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
        when(this.licenseService.mapLicense(any()))
                .thenReturn(new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                        "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org"));
        when(this.licenseService.mapDto(any())).thenReturn(license);
        when(this.licenseService.getLicenseByLicenseId(any()))
                .thenReturn(new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                        "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org"));
        when(this.licenseService.getLicensesByOrganizationId(any()))
                .thenThrow(new MissingLicenseException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/license/organization/{organizationId}", "", "Uri Vars");
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"licenseId\":\"42\",\"description\":\"The characteristics of someone or something\",\"organizationId\":\"42\","
                                        + "\"productName\":\"Product Name\",\"licenseType\":\"License Type\",\"comment\":\"Comment\",\"organizationName\":"
                                        + "\"Organization Name\",\"contactName\":\"Contact Name\",\"contactPhone\":\"4105551212\",\"contactEmail\":\"jane.doe"
                                        + "@example.org\"}"));
    }

    @Test
    void itShouldGetLicense() throws Exception {
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
        when(this.licenseService.mapLicense(any()))
                .thenReturn(new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                        "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org"));
        when(this.licenseService.mapDto(any())).thenReturn(license);
        when(this.licenseService.getLicenseByLicenseId(any()))
                .thenReturn(new LicenseDTO("42", "The characteristics of someone or something", "42", "Product Name",
                        "License Type", "Comment", "Organization Name", "Contact Name", "4105551212", "jane.doe@example.org"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/license/{licenseId}", "42");
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"licenseId\":\"42\",\"description\":\"The characteristics of someone or something\",\"organizationId\":\"42\","
                                        + "\"productName\":\"Product Name\",\"licenseType\":\"License Type\",\"comment\":\"Comment\",\"organizationName\":"
                                        + "\"Organization Name\",\"contactName\":\"Contact Name\",\"contactPhone\":\"4105551212\",\"contactEmail\":\"jane.doe"
                                        + "@example.org\"}"));
    }
}


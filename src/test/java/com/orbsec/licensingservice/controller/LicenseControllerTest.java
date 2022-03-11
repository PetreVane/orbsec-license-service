package com.orbsec.licensingservice.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbsec.licensingservice.exception.InvalidLicenseException;
import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.service.LicenseService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

@ContextConfiguration(classes = {LicenseController.class})
@ExtendWith(SpringExtension.class)
class LicenseControllerTest {
    @Autowired
    private LicenseController licenseController;

    @MockBean
    private LicenseService licenseService;

    @Mock
    private BindingResult bindingResult;

    @Test
    @Description("It should create License")
    void itShouldCreateLicense() throws Exception {
        // Given
        when(this.licenseService.createLicense(any(), any())).thenReturn("Create License");

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
        String content = (new ObjectMapper()).writeValueAsString(licenseDTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/license/organization/{organizationId}", "12342")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Create License"));
    }

    @Test
    @Description("It should not create license -> client error --> invalid input")
    void itShouldNotCreateLicense() throws Exception {

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
        String content = (new ObjectMapper()).writeValueAsString(licenseDTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/license/organization/{organizationId}", "12342")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    @Description("It should update License")
    void itShouldUpdateLicense() throws Exception {
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

        when(this.licenseService.updateLicenseForOrganization(any(), any(), any())).thenReturn(licenseDTO);

        String content = (new ObjectMapper()).writeValueAsString(licenseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/license/organization/{organizationId}/licenseId/{licenseId}", "12342", "12342")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(this.licenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"licenseId\":\"12342\",\"description\":\"Description\",\"organizationId\":\"12342\","
                                        + "\"productName\":\"Product Name\",\"licenseType\":\"License Type\",\"comment\":\"Comment\",\"organizationName\":"
                                        + "\"Organization Name\",\"contactName\":\"Contact Name\",\"contactPhone\":\"4105551212\",\"contactEmail\":\"jane.doe"
                                        + "@example.org\"}"));
    }

    @Test
    @Description("It should not update -> client error --> invalid input")
    void itShouldNotUpdateLicense() throws Exception {
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

        when(this.licenseService.updateLicenseForOrganization(any(), any(), any())).thenReturn(licenseDTO);

        String content = (new ObjectMapper()).writeValueAsString(licenseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/license/organization/{organizationId}", "12342")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.licenseController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is4xxClientError());
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
}


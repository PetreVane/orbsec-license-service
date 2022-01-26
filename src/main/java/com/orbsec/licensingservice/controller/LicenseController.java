package com.orbsec.licensingservice.controller;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/license")
public class LicenseController {

    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RolesAllowed({ "ADMIN", "USER" })
    @GetMapping(value = "/{licenseId}", produces = "application/json")
    public ResponseEntity<LicenseDTO> getLicense(@PathVariable("licenseId") String licenseId) throws MissingLicenseException {
        return ResponseEntity.ok(licenseService.getLicenseByLicenseId(licenseId));
    }

    @RolesAllowed({ "ADMIN", "USER" })
    @GetMapping(value = "/organization/{organizationId}")
    public ResponseEntity<List<LicenseDTO>> getAllLicensesByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licenseService.getLicensesByOrganizationId(organizationId));
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value = "/all")
    public ResponseEntity<List<LicenseDTO>> fetchAllLicenses() {
        return ResponseEntity.ok(licenseService.getAllLicenses());
    }

    @RolesAllowed({ "ADMIN", "USER" })
    @PostMapping(value = "/organization/{organizationId}")
    public ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId, @RequestBody LicenseDTO licenseDTO) {
        return ResponseEntity.ok(licenseService.createLicense(licenseDTO, organizationId));
    }

    @RolesAllowed("ADMIN")
    @PutMapping(value = "/organization/{organizationId}")
    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId, @RequestBody LicenseDTO licenseDTO) {
        return ResponseEntity.ok(licenseService.updateLicense(licenseDTO, organizationId));
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}

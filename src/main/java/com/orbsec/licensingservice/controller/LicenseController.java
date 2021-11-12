package com.orbsec.licensingservice.controller;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/organization/{organizationId}/license")
@RestController
public class LicenseController {


    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }


    @GetMapping(value = "/{licenseId}", produces = "application/json")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId, @PathVariable("licenseId") String licenseId) throws MissingLicenseException {
        var existingLicense = licenseService.getLicense(licenseId, organizationId);
        return ResponseEntity.ok(existingLicense);

    }

    @PostMapping
    public ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId, @RequestBody License license) {
        return ResponseEntity.ok(licenseService.createLicense(license, organizationId));
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId, @RequestBody License license) {
        return ResponseEntity.ok(licenseService.updateLicense(license, organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId, @PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }
}

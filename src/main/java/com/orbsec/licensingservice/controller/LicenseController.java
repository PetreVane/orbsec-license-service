package com.orbsec.licensingservice.controller;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/organization/{organizationId}/license")
public class LicenseController {


    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }


    @GetMapping(value = "/{licenseId}", produces = "application/json")
    public ResponseEntity<LicenseDTO> getLicense(@PathVariable("organizationId") String organizationId, @PathVariable("licenseId") String licenseId) throws MissingLicenseException {
        var licenseDTO = licenseService.getLicense(licenseId, organizationId);
        var existingLicense = licenseService.mapDto(licenseDTO);
        existingLicense.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, existingLicense.getLicenseId())).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(organizationId, licenseService.mapLicense(existingLicense))).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(organizationId, licenseService.mapLicense(existingLicense))).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, existingLicense.getLicenseId())).withRel("deleteLicense")
        );
        return ResponseEntity.ok(licenseService.mapLicense(existingLicense));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId, @RequestBody LicenseDTO licenseDTO) {
        return ResponseEntity.ok(licenseService.createLicense(licenseDTO, organizationId));
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId, @RequestBody LicenseDTO licenseDTO) {
        return ResponseEntity.ok(licenseService.updateLicense(licenseDTO, organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId, @PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }
}

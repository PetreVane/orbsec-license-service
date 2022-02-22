package com.orbsec.licensingservice.controller;

import com.orbsec.licensingservice.exception.InvalidLicenseException;
import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/license")
public class LicenseController {

    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    //TODO: Reactivate commented-out security rule
//    @RolesAllowed({ "ADMIN", "USER" })
    @GetMapping(value = "/{licenseId}", produces = "application/json")
    public ResponseEntity<LicenseDTO> getLicense(@PathVariable("licenseId") String licenseId) throws MissingLicenseException {
        return ResponseEntity.ok(licenseService.getLicenseByLicenseId(licenseId));
    }

    //TODO: Reactivate commented-out security rule
//    @RolesAllowed({ "ADMIN", "USER" })
    @GetMapping(value = "/organization/{organizationId}")
    public ResponseEntity<List<LicenseDTO>> getAllLicensesByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licenseService.getLicensesByOrganizationId(organizationId));
    }

    //TODO: Reactivate commented-out security rule
//    @RolesAllowed("ADMIN")
    @GetMapping(value = "/all")
    public ResponseEntity<List<LicenseDTO>> fetchAllLicenses() {
        return ResponseEntity.ok(licenseService.getAllLicenses());
    }

    //TODO: Reactivate commented-out security rule
//    @RolesAllowed({ "ADMIN", "USER" })
    @PostMapping(value = "/organization/{organizationId}")
    public ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId,
                                                @Valid @RequestBody LicenseDTO licenseDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Input validation failed in createLicense method: {}", bindingResult.getFieldError().getField());
            throw new InvalidLicenseException("Input validation failed");
        }
        return ResponseEntity.ok(licenseService.createLicense(licenseDTO, organizationId));
    }

    //TODO: Reactivate commented-out security rule
//    @RolesAllowed("ADMIN")
    @PutMapping(value = "/organization/{organizationId}")
    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId, @Valid @RequestBody LicenseDTO licenseDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Input validation failed in updateLicense method: {}", bindingResult.getFieldError().getField());
            throw new InvalidLicenseException("Input validation failed");
        }
        return ResponseEntity.ok(licenseService.updateLicense(licenseDTO, organizationId));
    }

    //TODO: Reactivate commented-out security rule
//    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicenseById(licenseId));
    }
}

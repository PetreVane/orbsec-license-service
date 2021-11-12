package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public String createLicense(License license, String organizationId) {
        String responseMessage;
        if (license != null) {
            license.setOrganizationId(organizationId);
            licenseRepository.save(license);
            responseMessage = String.format("License has been saved for organizationId: [%s]", organizationId);
        } else {
            responseMessage = "Invalid license!";
            return responseMessage;
        }
        return responseMessage;
    }

    public License getLicense(String licenseId, String organizationId) throws MissingLicenseException {
        var existingLicense = licenseRepository.findLicenseByLicenseId(licenseId);
        if (existingLicense.isPresent()) {
            License license = existingLicense.get();
            license.setOrganizationId(organizationId);
            return license;
        } else {
            throw new MissingLicenseException("No license found");
        }
    }

}

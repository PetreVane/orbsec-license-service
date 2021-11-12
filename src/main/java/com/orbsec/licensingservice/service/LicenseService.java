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

    public String updateLicense(License license, String organizationId) throws MissingLicenseException {
        String responseMessage = null;
        if (license != null) {
            var existingLicense = getLicense(license.getLicenseId(), organizationId);
            existingLicense.setOrganizationId(organizationId);
            licenseRepository.save(existingLicense);
            responseMessage = String.format("License [%s] has been updated with organization id [%s].", existingLicense.getLicenseId(), organizationId);
        }
        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) throws MissingLicenseException {
        String responseMessage;
        var licenseToBeDeleted = getLicense(licenseId, organizationId);
        licenseRepository.delete(licenseToBeDeleted);
        responseMessage = String.format("License with id [%s] has been deleted successfully.", licenseId);
        return responseMessage;
    }

}

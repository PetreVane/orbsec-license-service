package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.repository.LicenseRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private ModelMapper mapper;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
        configureMapper();
    }

    private void configureMapper() {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // Maps dto to object
    public License mapDto(LicenseDTO licenseDTO) {
        return  mapper.map(licenseDTO, License.class);
    }

    // Maps object to Dto
    public LicenseDTO mapLicense(License license) {
        return mapper.map(license, LicenseDTO.class);
    }

    public String createLicense(LicenseDTO licenseDTO, String organizationId) {
        var license = mapDto(licenseDTO);
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

    public LicenseDTO getLicense(String licenseId, String organizationId) throws MissingLicenseException {
        var existingLicense = licenseRepository.findLicenseByLicenseId(licenseId);
        if (existingLicense.isPresent()) {
            License license = existingLicense.get();
            license.setOrganizationId(organizationId);
            return mapLicense(license);
        } else {
            throw new MissingLicenseException("No license found");
        }
    }

    public String updateLicense(LicenseDTO licenseDTO, String newOrganizationId) throws MissingLicenseException {
        String responseMessage = null;
        if (licenseDTO != null) {
            var existingLicense = getLicense(licenseDTO.getLicenseId(), licenseDTO.getOrganizationId());
            existingLicense.setOrganizationId(newOrganizationId);
            licenseRepository.save(mapDto(existingLicense));
            responseMessage = String.format("License [%s] has been updated with organization id [%s].", existingLicense.getLicenseId(), newOrganizationId);
        }
        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) throws MissingLicenseException {
        String responseMessage;
        var licenseToBeDeleted = getLicense(licenseId, organizationId);
        licenseRepository.delete(mapDto(licenseToBeDeleted));
        responseMessage = String.format("License with id [%s] has been deleted successfully.", licenseId);
        return responseMessage;
    }

}

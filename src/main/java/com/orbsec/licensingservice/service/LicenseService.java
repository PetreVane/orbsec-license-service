package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.model.OrganizationDto;
import com.orbsec.licensingservice.repository.LicenseRepository;
import com.orbsec.licensingservice.service.client.OrganizationFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final OrganizationFeignClient feignClient;
    private final RedisService redisService;

    private static final String NOT_AVAILABLE = "not available";
    private static final String DESCRIPTION = "License database service is not available. Try again later!";
    private ModelMapper modelMapper;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, OrganizationFeignClient feignClient, RedisService redisService) {
        this.licenseRepository = licenseRepository;
        this.feignClient = feignClient;
        this.redisService = redisService;
        configureMapper();
    }

    private void configureMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // Maps dto to object
    private License mapDto(LicenseDTO licenseDTO) {
        return modelMapper.map(licenseDTO, License.class);
    }

    // Maps object to Dto
    private LicenseDTO mapLicense(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

    // checks redis cache for record; if not present, asks remote service for a record
    private OrganizationDto checkRedisCacheFor(String organizationId) {
        var cachedRecord = redisService.checkCacheFor(organizationId);
        return cachedRecord.isPresent() ? cachedRecord.get() : getUpdatedOrganizationRecord(organizationId);
    }


    // asks remote service for an updated record & saves the record into redis cache
    public OrganizationDto getUpdatedOrganizationRecord(String organizationId) {
        log.info("Asking organization-service for an updated record for id: {}", organizationId);
        var updatedRecord = feignClient.getOrganization(organizationId);
        redisService.saveRecordIntoCache(updatedRecord);
        return updatedRecord;
    }

    // Database calls
    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "crudLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "crudLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "crudLicenseFallback")
    public String createLicense(LicenseDTO licenseDTO, String organizationId) {
        log.info("Creating a new license for organization id: {}", organizationId);
        // check if there is a cached organization for the provided id
        var organization = checkRedisCacheFor(organizationId);
        // maps the incoming license dto
        var license = mapDto(licenseDTO);
        license.setLicenseId(String.valueOf(UUID.randomUUID()));
        license.setOrganizationId(organizationId);
        license.setOrganizationName(organization.getName());
        license.setContactPhone(organization.getContactPhone());
        license.setContactEmail(organization.getContactEmail());
        licenseRepository.save(license);
        String responseMessage = String.format("A new license has been saved for organizationId %s ", organizationId);
        log.info("A new license has been saved for organizationId: {}", organizationId);
        return responseMessage;
    }


    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "getLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "getLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "getLicenseFallback")
    public LicenseDTO getLicenseByLicenseId(String licenseId) throws MissingLicenseException {
        var existingLicense = licenseRepository.findLicenseByLicenseId(licenseId);
        if (existingLicense.isPresent()) {
            return mapLicense(existingLicense.get());
        } else {
            throw new MissingLicenseException("No license found");
        }
    }

    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "getAllLicensesFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "getAllLicensesFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "getAllLicensesFallback")
    public List<LicenseDTO> getLicensesByOrganizationId(String organizationId) {
        List<License> licenses = licenseRepository.findAllByOrganizationId(organizationId);
        return licenses.parallelStream().map(this::mapLicense).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "getAllLicensesFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "getAllLicensesFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "getAllLicensesFallback")
    public List<LicenseDTO> getAllLicenses() {
        List<License> licenses = (List<License>) licenseRepository.findAll();
        return licenses.parallelStream()
                .map(this::updateLicenseWithOrganizationData)
                .map(this::mapLicense)
                .collect(Collectors.toList());
    }

    // Updates license with Organization data, for which the license belongs
    // Organization data is not saved within license database
    private License updateLicenseWithOrganizationData(License license) {
        var organization = checkRedisCacheFor(license.getOrganizationId());
        license.setOrganizationName(organization.getName());
        license.setContactName(organization.getContactName());
        license.setContactPhone(organization.getContactPhone());
        license.setContactEmail(organization.getContactEmail());
        return license;
    }

    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "updateLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "updateLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "updateLicenseFallback")
    public LicenseDTO updateLicenseForOrganization(String organizationId, String licenseId, LicenseDTO update) {
        // finds the license to be updated for the given organization id (there might be several licenses for one organization)
        log.info("Attempting to find license with id {} for organization with id {}", licenseId, organizationId);
        var existingLicense = licenseRepository.findLicenseByLicenseId(licenseId);

        if (existingLicense.isPresent()) {
            log.info("Found license with id {} for organization id {}", licenseId, organizationId);
            var license = existingLicense.get();
            modelMapper.map(update, license);
            licenseRepository.save(license);
            log.info("Successfully saved update for license id {}", licenseId);
            return mapLicense(license);
        } else {
            log.error("Could not find any license for id {}", licenseId);
            throw new MissingLicenseException(String.format("Could not find any license for id %s ", licenseId));
        }
    }

    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "deleteLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "deleteLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "deleteLicenseFallback")
    public String deleteLicenseById(String licenseId) throws MissingLicenseException {
        String responseMessage;
        var licenseToBeDeleted = getLicenseByLicenseId(licenseId);
        licenseRepository.delete(mapDto(licenseToBeDeleted));
        responseMessage = String.format("License with id [%s] has been deleted successfully.", licenseId);
        return responseMessage;
    }

    public void deleteAllLicensesForOrganization(String organizationId) {
        var licensesToBeDeleted = getLicensesByOrganizationId(organizationId);
        if (licensesToBeDeleted.isEmpty()) {
            log.info("No license records were found for organization id: {}", organizationId);
        } else {
            licensesToBeDeleted.parallelStream().forEach(licenseDTO -> deleteLicenseById(licenseDTO.getLicenseId()));
            log.info("Successfully deleted all license records for organization id: {}", organizationId);
        }
    }

//     Fallbacks
    @SuppressWarnings("unused")
    private String crudLicenseFallback(LicenseDTO licenseDTO, String organizationId, Throwable exception) {
        log.warn("@CircuitBreaker: called 'crudLicenseFallback()' method ");
        return DESCRIPTION;
    }

    @SuppressWarnings("unused")
    private LicenseDTO updateLicenseFallback(String organizationId, String licenseId, LicenseDTO licenseDTO, Throwable exception) {
        log.warn("@CircuitBreaker: called 'updateLicenseFallback()' method ");
        if (exception instanceof MissingLicenseException) {
            log.error("updateLicenseFallback: Could not find any license for id {}", licenseId);
            throw new MissingLicenseException("Could not find any license for id ");
        }
        return new LicenseDTO(DESCRIPTION, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE);
    }

    @SuppressWarnings("unused")
    private LicenseDTO getLicenseFallback(String licenseId, Throwable exception) {

        if (exception instanceof MissingLicenseException) {
            throw new MissingLicenseException("No license found");
        }
        log.warn("@CircuitBreaker: called 'getLicenseFallback()' method ");
        return new LicenseDTO(DESCRIPTION, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE);
    }

    @SuppressWarnings("unused")
    private String deleteLicenseFallback(String licenseId, Throwable exception) {
        log.warn("@CircuitBreaker: called 'getLicenseFallback()' method ");
        return DESCRIPTION;
    }

    @SuppressWarnings("unused")
    private List<LicenseDTO> getAllLicensesFallback(Throwable exception) {
        log.warn("@CircuitBreaker: called 'getAllLicensesFallback()' method ");
        List<LicenseDTO> licenseDTOS = new ArrayList<>();
        LicenseDTO licenseDTO = new LicenseDTO(DESCRIPTION, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE);
        licenseDTOS.add(licenseDTO);
        return licenseDTOS;
    }

}

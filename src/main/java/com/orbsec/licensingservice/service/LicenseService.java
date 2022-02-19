package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.exception.MissingLicenseException;
import com.orbsec.licensingservice.model.License;
import com.orbsec.licensingservice.model.LicenseDTO;
import com.orbsec.licensingservice.model.OrganizationDto;
import com.orbsec.licensingservice.repository.LicenseRepository;
import com.orbsec.licensingservice.repository.OrganizationRedisRepository;
import com.orbsec.licensingservice.service.client.OrganizationFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final OrganizationFeignClient feignClient;
    private final OrganizationRedisRepository redisRepository;

    private static final String FAKE_DATA = "not available";
    private static final String NOT_AVAILABLE = "License database service is not available. Try again later!";
    private ModelMapper modelMapper;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, OrganizationRedisRepository redisRepository, OrganizationFeignClient feignClient) {
        this.licenseRepository = licenseRepository;
        this.redisRepository = redisRepository;
        this.feignClient = feignClient;
        configureMapper();
    }

    private void configureMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // Maps dto to object
    public License mapDto(LicenseDTO licenseDTO) {
        return modelMapper.map(licenseDTO, License.class);
    }

    // Maps object to Dto
    public LicenseDTO mapLicense(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

    // Redis
    // checks redis cache for record
    public OrganizationDto checkRedisCacheFor(String organizationId) {
        try {
            return redisRepository.findById(organizationId).orElse(null);
        } catch (Exception e) {
            log.error("Errors while trying to get record from Redis cache: {}", e.getMessage());
            return null;
        }
    }
    // removes a record from redis cache
    public void evictRedisCacheForOrganizationRecord(String organizationId) {
        log.info("Attempting to evict redis record for id: {}", organizationId);
        var cachedRecord = checkRedisCacheFor(organizationId);
        if (cachedRecord != null) {
            redisRepository.delete(cachedRecord);
            log.info("Cache emptied for record with id {}", organizationId);
        } else {
            log.info("No cached record was found for id: {}", organizationId);
        }
    }

    // Remote service
    // asks remote service for an updated record & saves the record to redis cache
    public OrganizationDto getUpdatedOrganizationRecord(String organizationId) {
        log.info("Asking organization-service for an updated record for id: {}", organizationId);
        var updatedRecord = feignClient.getOrganization(organizationId);
        redisRepository.save(updatedRecord);
        log.info("Saved updated record in redis cache for id: {}", organizationId);
        return updatedRecord;
    }

    // Database calls
    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "crudLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "crudLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "crudLicenseFallback")
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

    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "getLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "getLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "getLicenseFallback")
    public LicenseDTO getLicenseByLicenseId(String licenseId) throws MissingLicenseException {
        var existingLicense = licenseRepository.findLicenseByLicenseId(licenseId);
        if (existingLicense.isPresent()) {
            License license = existingLicense.get();
            return mapLicense(license);
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
        return licenses.parallelStream().map(this::mapLicense).collect(Collectors.toList());
    }


    //TODO: update this method to accept a LicenseId as search parameter & organizationId as the value to be updated
    @CircuitBreaker(name = "licenseDatabase", fallbackMethod = "crudLicenseFallback")
    @Retry(name ="retryLicenseDatabase", fallbackMethod = "crudLicenseFallback")
    @Bulkhead(name = "bulkheadLicenseDatabase", fallbackMethod = "crudLicenseFallback")
    public String updateLicense(LicenseDTO licenseDTO, String newOrganizationId) throws MissingLicenseException {
        String responseMessage = null;
        if (licenseDTO != null) {
            var existingLicenseDto = getLicenseByLicenseId(licenseDTO.getLicenseId());
            License licenseToBeUpdated = mapDto(existingLicenseDto);
            licenseToBeUpdated.setOrganizationId(newOrganizationId);
            licenseRepository.save(licenseToBeUpdated);
            responseMessage = String.format("License [%s] has been updated with organization id [%s].", licenseToBeUpdated.getLicenseId(), newOrganizationId);
        }
        return responseMessage;
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

    public void deleteLicenseForOrganization(String organizationId) {
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
        return NOT_AVAILABLE;
    }

    @SuppressWarnings("unused")
    private LicenseDTO getLicenseFallback(String licenseId, Throwable exception) {

        if (exception instanceof MissingLicenseException) {
            throw new MissingLicenseException("No license found");
        }
        log.warn("@CircuitBreaker: called 'getLicenseFallback()' method ");
        return new LicenseDTO(NOT_AVAILABLE, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA);
    }

    @SuppressWarnings("unused")
    private String deleteLicenseFallback(String licenseId, Throwable exception) {
        log.warn("@CircuitBreaker: called 'getLicenseFallback()' method ");
        return NOT_AVAILABLE;
    }

    @SuppressWarnings("unused")
    private List<LicenseDTO> getAllLicensesFallback(Throwable exception) {
        log.warn("@CircuitBreaker: called 'getAllLicensesFallback()' method ");
        List<LicenseDTO> licenseDTOS = new ArrayList<>();
        LicenseDTO licenseDTO = new LicenseDTO(NOT_AVAILABLE, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA, FAKE_DATA);
        licenseDTOS.add(licenseDTO);
        return licenseDTOS;
    }

}

package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.avro.OrganizationDto;
import com.orbsec.licensingservice.repository.OrganizationRedisRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RedisService {

    private final OrganizationRedisRepository redisRepository;
    private static final String NOT_AVAILABLE = "Data not available";
    private static final String DESCRIPTION = "Redis database service is not available. Try again later!";
    private static final String EXCEPTION_MESSAGE = "Exception message: {}";

    @Autowired
    public RedisService(OrganizationRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @CircuitBreaker(name = "redisDatabase", fallbackMethod = "saveRecordIntoCacheFallback")
    @Retry(name ="retryRedisDatabase", fallbackMethod = "saveRecordIntoCacheFallback")
    @Bulkhead(name = "bulkheadRedisDatabase", fallbackMethod = "saveRecordIntoCacheFallback")
    public void saveRecordIntoCache(OrganizationDto organization) {
        log.info("Attempting to save organization record into Redis cache. Id: {}", organization.getId());
        redisRepository.save(organization);
        log.info("Successfully cached organization record with id {}", organization.getId());
    }

    @CircuitBreaker(name = "redisDatabase", fallbackMethod = "checkCacheFallback")
    @Retry(name ="retryRedisDatabase", fallbackMethod = "checkCacheFallback")
    @Bulkhead(name = "bulkheadRedisDatabase", fallbackMethod = "checkCacheFallback")
    public Optional<OrganizationDto> checkCacheFor(String organizationId) {
        log.info("Attempting to get organization record from Redis cache, for id {}", organizationId);
        var cachedRecord =  redisRepository.findById(organizationId);
        log.info("Successfully fetched Redis cached record with id {}", organizationId);
        return cachedRecord;
    }


    @CircuitBreaker(name = "redisDatabase", fallbackMethod = "evictCacheFallback")
    @Retry(name ="retryRedisDatabase", fallbackMethod = "evictCacheFallback")
    @Bulkhead(name = "bulkheadRedisDatabase", fallbackMethod = "evictCacheFallback")
    public void evictCacheFor(String organizationId) {
        log.info("Attempting to evict redis record for id: {}", organizationId);
        var cachedRecord = checkCacheFor(organizationId);
        if (cachedRecord.isPresent()) {
            redisRepository.delete(cachedRecord.get());
            log.info("Cache emptied for record with id {}", organizationId);
        } else {
            log.info("No cached record was found for id: {}", organizationId);
        }
    }

    // Fallbacks
    @SuppressWarnings("unused")
    private void saveRecordIntoCacheFallback(OrganizationDto organizationDto, Throwable exception) {
        log.error("@CircuitBreaker: saveRecordIntoCacheFallback called for Organization id: {}", organizationDto.getId());
        log.error(EXCEPTION_MESSAGE,  exception.getMessage());
    }

    @SuppressWarnings("unused")
    private Optional<OrganizationDto> checkCacheFallback(String organizationId, Throwable exception) {
        log.error("@CircuitBreaker: checkCacheFallback called for Organization id: {}", organizationId);
        log.error(EXCEPTION_MESSAGE, exception.getMessage());
        return Optional.of(generateFallbackObject());
    }

    @SuppressWarnings("unused")
    private void evictCacheFallback(String organizationId, Throwable exception) {
        log.error("@CircuitBreaker: evictCacheFallback called for Organization id: {}", organizationId);
        log.error(EXCEPTION_MESSAGE, exception.getMessage());
    }

    private OrganizationDto generateFallbackObject() {
        var fallbackObject = new OrganizationDto();
        fallbackObject.setName(DESCRIPTION);
        fallbackObject.setContactEmail(NOT_AVAILABLE);
        fallbackObject.setContactName(NOT_AVAILABLE);
        fallbackObject.setContactPhone(NOT_AVAILABLE);
        return fallbackObject;
    }
}

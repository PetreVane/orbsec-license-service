package com.orbsec.licensingservice.service;

import com.orbsec.licensingservice.exception.RedisConnectionException;
import com.orbsec.licensingservice.model.OrganizationDto;
import com.orbsec.licensingservice.repository.OrganizationRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RedisService {

    private final OrganizationRedisRepository redisRepository;

    @Autowired
    public RedisService(OrganizationRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }


    public void saveRecordIntoCache(OrganizationDto organization) {
        log.info("Attempting to save organization record into Redis cache. Id: {}", organization.getId());
        try {
            redisRepository.save(organization);
            log.info("Successfully cached organization record with id {}", organization.getId());
        } catch (Exception e) {
            log.error("Errors while attempting to cache organization record: {}", e.getMessage());
        }
    }

    // checks redis cache for record
    public Optional<OrganizationDto> checkCacheFor(String organizationId) {
        log.info("Attempting to get organization record from Redis cache, for id {}", organizationId);
        try {
            var cachedRecord =  redisRepository.findById(organizationId);
            log.info("Successfully fetched Redis cached record with id {}", organizationId);
            return cachedRecord;
        } catch (Exception e) {
            log.error("Errors while trying to get record from Redis cache: {}", e.getMessage());
            throw new RedisConnectionException(e.getMessage());
        }
    }

    // removes a record from redis cache
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
}

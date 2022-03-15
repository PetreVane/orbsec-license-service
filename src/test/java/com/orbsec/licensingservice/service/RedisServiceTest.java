package com.orbsec.licensingservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.orbsec.licensingservice.avro.OrganizationDto;
import com.orbsec.licensingservice.repository.OrganizationRedisRepository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RedisService.class})
@ExtendWith(SpringExtension.class)
class RedisServiceTest {
    @MockBean
    private OrganizationRedisRepository organizationRedisRepository;

    @Autowired
    private RedisService redisService;

    @Test
    @Description("It should save Organization record into Redis database")
    void itShouldSaveRecordIntoCache() {
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setContactEmail("jane.doe@example.org");
        organizationDto.setContactName("Contact Name");
        organizationDto.setContactPhone("4105551212");
        organizationDto.setId("12342");
        organizationDto.setName("Name");

        when(this.organizationRedisRepository.save(any())).thenReturn(organizationDto);

        this.redisService.saveRecordIntoCache(organizationDto);
        verify(this.organizationRedisRepository).save(any());

        assertEquals("jane.doe@example.org", organizationDto.getContactEmail());
        assertEquals("Name", organizationDto.getName());
        assertEquals("12342", organizationDto.getId());
        assertEquals("4105551212", organizationDto.getContactPhone());
        assertEquals("Contact Name", organizationDto.getContactName());
    }

    @Test
    @Description("It should check cache for Organization record")
    void itShouldCheckCacheFor() {

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setContactEmail("jane.doe@example.org");
        organizationDto.setContactName("Contact Name");
        organizationDto.setContactPhone("4105551212");
        organizationDto.setId("12342");
        organizationDto.setName("Name");
        Optional<OrganizationDto> optionalResult = Optional.of(organizationDto);

        when(this.organizationRedisRepository.findById(any())).thenReturn(optionalResult);
        Optional<OrganizationDto> actualResult = this.redisService.checkCacheFor("12342");
        assertSame(optionalResult, actualResult);
        assertTrue(actualResult.isPresent());
        verify(this.organizationRedisRepository).findById(any());
    }

    @Test
    @Description("It should delete cache for Organization record")
    void itShouldEvictCacheFor() {

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setContactEmail("jane.doe@example.org");
        organizationDto.setContactName("Contact Name");
        organizationDto.setContactPhone("4105551212");
        organizationDto.setId("12342");
        organizationDto.setName("Name");

        Optional<OrganizationDto> optionalResult = Optional.of(organizationDto);
        doNothing().when(this.organizationRedisRepository).delete(any());

        when(this.organizationRedisRepository.findById(any())).thenReturn(optionalResult);
        this.redisService.evictCacheFor("12342");
        verify(this.organizationRedisRepository).findById(any());
        verify(this.organizationRedisRepository).delete(any());
    }

    @Test
    @Description("It should not delete cache for Organization record")
    void itShouldNotEvictCacheFor() {
        doNothing().when(this.organizationRedisRepository).delete(any());
        when(this.organizationRedisRepository.findById(any())).thenReturn(Optional.empty());
        this.redisService.evictCacheFor("12342");
        verify(this.organizationRedisRepository).findById(any());
    }
}


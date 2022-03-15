package com.orbsec.licensingservice.service.client;


import com.orbsec.licensingservice.avro.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "organization-service/api/v1/organization")
public interface OrganizationFeignClient {

    @GetMapping(value="/{organizationId}")
    OrganizationDto getOrganization(@PathVariable("organizationId") String organizationId);
}

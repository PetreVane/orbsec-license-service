package com.orbsec.licensingservice.service.client;

import com.orbsec.licensingservice.model.OrganizationDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "organization-service", value = "/api/v1/organization")
public interface OrganizationFeignClient {

    @GetMapping(value="/{organizationId}", consumes = "application/json")
    ResponseEntity<OrganizationDto> getOrganization(@PathVariable("organizationId") String organizationId);

    @PostMapping(consumes = "application/json")
    ResponseEntity<OrganizationDto>  saveOrganization(@RequestBody OrganizationDto organizationDto);

    @PutMapping
    void updateOrganization( @PathVariable("organizationId") String id, @RequestBody OrganizationDto organizationDto);

    @DeleteMapping(value = "/{organizationId}")
    void deleteOrganization( @PathVariable("organizationId") String id,  @RequestBody OrganizationDto organizationDto);
}

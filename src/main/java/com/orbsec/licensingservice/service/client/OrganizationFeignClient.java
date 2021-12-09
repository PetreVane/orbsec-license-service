package com.orbsec.licensingservice.service.client;

import com.orbsec.licensingservice.model.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "organization-service/api/v1/organization")
public interface OrganizationFeignClient {

    @GetMapping(value = "/all", produces = "application/json")
    List<OrganizationDto> getAllOrganizations();

    @GetMapping(value="/{organizationId}", consumes = "application/json")
    ResponseEntity<OrganizationDto> getOrganization(@PathVariable("organizationId") String organizationId);

    @PostMapping(consumes = "application/json")
    ResponseEntity<OrganizationDto>  saveOrganization(@RequestBody OrganizationDto organizationDto);

    @PutMapping(value="/{organizationId}")
    void updateOrganization(@PathVariable("organizationId") String id, @RequestBody OrganizationDto organizationDto);

    @DeleteMapping(value = "/{organizationId}")
    void deleteOrganization( @PathVariable("organizationId") String id,  @RequestBody OrganizationDto organizationDto);
}

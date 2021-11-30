package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends CrudRepository<License, String> {
    Optional<License> findLicenseByLicenseId(String licenseId);

    List<License> findLicenseByOrganizationId(String organizationId);

    Optional<License> findLicenseByOrganizationIdAndLicenseId(String organizationId, String licenseId);


}

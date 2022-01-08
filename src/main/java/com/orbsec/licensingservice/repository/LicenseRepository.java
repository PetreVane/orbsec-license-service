package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends CrudRepository<License, String> {
    Optional<License> findLicenseByLicenseId(String licenseId);

    List<License> findAllByOrganizationId(String organizationId);

    List<License> findAllLicenses();

}

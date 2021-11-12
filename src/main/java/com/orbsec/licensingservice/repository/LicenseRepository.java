package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface LicenseRepository extends CrudRepository<License, UUID> {
    Optional<License> findLicenseByLicenseId(String licenseId);
}

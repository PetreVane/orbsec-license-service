package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LicenseRepository extends CrudRepository<License, Long> {
    Optional<License> findLicenseByLicenseId(String licenseId);
}

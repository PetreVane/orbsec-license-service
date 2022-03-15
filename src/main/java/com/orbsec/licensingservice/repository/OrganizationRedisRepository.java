package com.orbsec.licensingservice.repository;

import com.orbsec.licensingservice.avro.OrganizationDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRedisRepository  extends CrudRepository<OrganizationDto, String> { }

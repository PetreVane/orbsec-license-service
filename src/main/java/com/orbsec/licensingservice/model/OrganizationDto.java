package com.orbsec.licensingservice.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@RedisHash("organization")
public class OrganizationDto {
    @Id
    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;
}

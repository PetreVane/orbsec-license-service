package com.orbsec.licensingservice.kafka;


import com.orbsec.licensingservice.avro.model.ChangeType;
import com.orbsec.licensingservice.avro.model.OrganizationChangeEvent;
import com.orbsec.licensingservice.service.LicenseService;
import com.orbsec.licensingservice.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationEventConsumer {

    private final LicenseService licenseService;
    private final RedisService redisService;

    @Autowired
    public OrganizationEventConsumer(LicenseService licenseService, RedisService redisService) {
        this.licenseService = licenseService;
        this.redisService = redisService;
    }

    @KafkaListener(id = "LicensingService-listener", topics = "organization_events", groupId = "licensingGroup")
    public void readMessagesFromKafkaTopic(ConsumerRecord<Integer, OrganizationChangeEvent> kafkaRecord) {
        var event = kafkaRecord.value();
        if (event != null) {
            log.warn("Received event from Kafka topic: {} ", event.getDescription().toString());
            updateLicenseDatabase(String.valueOf(event.getOrganizationId()), event.getChangeType());
        } else {
            log.error("Unable to receive events from Kafka topic");
        }
    }



    private void updateLicenseDatabase(String organizationId, ChangeType changeType) {

        switch (changeType) {
            case CREATION:
                log.info("New organization added with ID: {}", organizationId);
                licenseService.getUpdatedOrganizationRecord(organizationId);
                break;
            case UPDATE:
                log.info("Attempting to update license information for organization ID: {}", organizationId );
                redisService.evictCacheFor(organizationId);
                licenseService.getUpdatedOrganizationRecord(organizationId);
                break;
            case DELETION:
                log.info("Attempting to delete license records for organization ID: {}", organizationId );
                licenseService.deleteLicenseForOrganization(organizationId);
                redisService.evictCacheFor(organizationId);
                break;
        }
    }
}

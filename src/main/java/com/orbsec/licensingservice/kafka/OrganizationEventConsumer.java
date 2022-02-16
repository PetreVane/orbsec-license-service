package com.orbsec.licensingservice.kafka;


import com.orbsec.licensingservice.avro.model.ChangeType;
import com.orbsec.licensingservice.avro.model.OrganizationChangeEvent;
import com.orbsec.licensingservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationEventConsumer {


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

    //TODO: Implement distributed caching
    private void updateLicenseDatabase(String organizationId, ChangeType changeType) {

        switch (changeType) {
            case CREATION:
                log.info("New organization added with ID: {}", organizationId);
                break;
            case UPDATE:
                log.info("Attempting to update license information for organization ID: {}", organizationId );
                break;
            case DELETION:
                log.info("Attempting to delete license information for organization ID: {}", organizationId );
                break;
        }
    }
}

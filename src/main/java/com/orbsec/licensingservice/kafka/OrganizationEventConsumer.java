package com.orbsec.licensingservice.kafka;


import com.orbsec.licensingservice.avro.model.OrganizationChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationEventConsumer {

    @KafkaListener(id = "LicensingService-listener", topics = "org_topic", groupId = "licensing-service-group")
    public void readMessagesFromKafkaTopic(ConsumerRecord<Integer, OrganizationChangeEvent> kafkaRecord) {
        var event = kafkaRecord.value();
        if (event != null) {
            log.warn("Received event from Kafka Topic {} ", event.getDescription().toString());
        } else {
            log.error("Unable to receive events from Kafka topic");
        }

    }
}

package com.orbsec.licensingservice.kafka.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbsec.licensingservice.avro.model.OrganizationChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class EventDeserializer implements Deserializer<OrganizationChangeEvent> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public OrganizationChangeEvent deserialize(String s, byte[] bytes) {
        return null;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public OrganizationChangeEvent deserialize(String topic, Headers headers, byte[] data) {
        log.debug("Attempting to Deserialize OrganizationChangeEvent");
         try {
             if (data == null) {
                 log.error("Error while trying to deserialize OrganizationChangeEvent: data is null ");
                 return null;
             }
             mapper.addMixIn(OrganizationChangeEvent.class, IgnoreSchemaProperty.class);
             return mapper.readValue(new String(data, StandardCharsets.UTF_8), OrganizationChangeEvent.class);
         } catch (IOException exception ) {
             log.error("Failed deserializing OrganizationChangeEvent: {}", exception.getMessage());
         }

        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}

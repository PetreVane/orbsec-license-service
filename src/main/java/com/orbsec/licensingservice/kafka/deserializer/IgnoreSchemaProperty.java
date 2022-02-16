package com.orbsec.licensingservice.kafka.deserializer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Type;

public abstract class IgnoreSchemaProperty {
    @JsonIgnore
    abstract void getSchema();

    @JsonIgnore
    abstract void getSpecificData();

}

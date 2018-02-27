package com.futuristlabs.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CustomObjectMapper extends ObjectMapper {

    private static final String JSON_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public CustomObjectMapper() {
        super();
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

        setLocale(Locale.ENGLISH);
        registerModule(
                getLocalDateConfiguration()
        );
        registerModule(
                getTrimStringsModule()
        );

        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private SimpleModule getLocalDateConfiguration() {
        return new JavaTimeModule()
                .addSerializer(
                        LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(JSON_TIMESTAMP_PATTERN))
                )
                .addDeserializer(
                        LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(JSON_TIMESTAMP_PATTERN))
                );
    }

    private SimpleModule getTrimStringsModule() {
        return new SimpleModule().addDeserializer(
                String.class,
                new StdScalarDeserializer<String>(String.class) {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsString().trim();
                    }
                });
    }
}

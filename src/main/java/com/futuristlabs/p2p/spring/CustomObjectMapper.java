package com.futuristlabs.p2p.spring;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Component
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        super();
        configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        CustomSerializerFactory factory = new CustomSerializerFactory();
        factory.addSpecificMapping(DateTime.class, new JodaDateTimeJsonSerializer());
        factory.addSpecificMapping(LocalDate.class, new JodaLocalDateJsonSerializer());
        setSerializerFactory(factory);

        setDateFormat(new SimpleDateFormat("yyyy-mm-dd"));
    }

    @Override
    public ObjectWriter writer() {
        return super.writer();
    }

    private static class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {
        @Override
        public void serialize(DateTime value, JsonGenerator json, SerializerProvider provider) throws IOException {
            String formattedDate = ISODateTimeFormat.dateTime().print(value);
            json.writeString(formattedDate);
        }
    }

    private static class JodaLocalDateJsonSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator json, SerializerProvider provider) throws IOException {
            String formattedDate = ISODateTimeFormat.date().print(value);
            json.writeString(formattedDate);
        }
    }
}

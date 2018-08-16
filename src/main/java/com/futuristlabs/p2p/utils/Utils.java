package com.futuristlabs.p2p.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Base64;
import java.util.UUID;

public class Utils {

    public static String toString(UUID aValue) {
        return aValue == null ? null : aValue.toString();
    }

    public static DateTime parseDate(String dateStr) {
        try {
            final DateTimeFormatter isoDateTimeFormatter = ISODateTimeFormat.dateTime();
            return isoDateTimeFormatter.parseDateTime(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toString(DateTime date) {
        try {
            final DateTimeFormatter isoDateTimeFormatter = ISODateTimeFormat.dateTime();
            return isoDateTimeFormatter.print(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toString(LocalDate date) {
        return toString(date.toDateTime(new LocalTime(0, 0)));
    }

    public static String base64decode(final String str) {
        return new String(Base64.getDecoder().decode(str));
    }
}

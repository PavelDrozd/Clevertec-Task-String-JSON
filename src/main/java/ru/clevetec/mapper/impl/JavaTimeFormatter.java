package ru.clevetec.mapper.impl;

import ru.clevetec.exception.UnsupportedTimeException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JavaTimeFormatter {

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    DateTimeFormatter offsetTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ssX");

    DateTimeFormatter offsetDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    DateTimeFormatter zonedDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public Object formatObjectDate(Object object) {
        return formatObjectDate(object, object.getClass());
    }

    public Object formatObjectDate(Object object, Type type) {
        if (type.equals(LocalTime.class)) {
            return LocalTime.parse(object.toString())
                    .format(timeFormatter);
        } else if (type.equals(LocalDate.class)) {
            return LocalDate.parse(object.toString(), dateFormatter);
        } else if (type.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(object.toString())
                    .format(dateTimeFormatter);
        } else if (type.equals(OffsetTime.class)) {
            return OffsetTime.parse(object.toString())
                    .format(offsetTimeFormatter);
        } else if (type.equals(OffsetDateTime.class)) {
            return OffsetDateTime.parse(object.toString()).format(offsetDateTimeFormatter);
        } else if (type.equals(ZonedDateTime.class)) {
            return ZonedDateTime.parse(object.toString())
                    .format(zonedDateTimeFormatter);
        } else {
            throw new UnsupportedTimeException("Date of type: " + type + " is not supported.");
        }
    }

    public Object getObjectDate(Object object, Type type) {
        if (type.equals(LocalTime.class)) {
            return LocalTime.parse(object.toString(), timeFormatter);
        } else if (type.equals(LocalDate.class)) {
            return LocalDate.parse(object.toString(), dateFormatter);
        } else if (type.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(object.toString(), dateTimeFormatter);
        } else if (type.equals(OffsetTime.class)) {
            return OffsetTime.parse(object.toString(), offsetTimeFormatter);
        } else if (type.equals(OffsetDateTime.class)) {
            return OffsetDateTime.parse(object.toString(), offsetDateTimeFormatter);
        } else if (type.equals(ZonedDateTime.class)) {
            return ZonedDateTime.parse(object.toString(), zonedDateTimeFormatter);
        } else {
            throw new UnsupportedTimeException("Date of type: " + type + " is not supported.");
        }
    }

    public void setTimeFormatter(DateTimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public void setDateFormatter(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void setOffsetTimeFormatter(DateTimeFormatter offsetTimeFormatter) {
        this.offsetTimeFormatter = offsetTimeFormatter;
    }

    public void setOffsetDateTimeFormatter(DateTimeFormatter offsetDateTimeFormatter) {
        this.offsetDateTimeFormatter = offsetDateTimeFormatter;
    }

    public void setZonedDateTimeFormatter(DateTimeFormatter zonedDateTimeFormatter) {
        this.zonedDateTimeFormatter = zonedDateTimeFormatter;
    }
}

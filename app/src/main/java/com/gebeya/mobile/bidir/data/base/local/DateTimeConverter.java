package com.gebeya.mobile.bidir.data.base.local;

import org.joda.time.DateTime;

import io.objectbox.converter.PropertyConverter;

/**
 * Converter class used to parseResponse from the API and ObjectBox equivalent
 */
public class DateTimeConverter implements PropertyConverter<DateTime, String> {

    @Override
    public DateTime convertToEntityProperty(String databaseValue) {
        return new DateTime(databaseValue); // TODO: Ensure it is returned into local format
    }

    @Override
    public String convertToDatabaseValue(DateTime entityProperty) {
        return entityProperty.toString();   // TODO: Ensure it is stored int local format
    }
}

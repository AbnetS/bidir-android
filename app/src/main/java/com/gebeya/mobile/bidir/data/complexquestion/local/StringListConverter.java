package com.gebeya.mobile.bidir.data.complexquestion.local;

import org.greenrobot.essentials.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

/**
 * Class used to map List of string elements to a String value, stored on the database.
 */
public class StringListConverter implements PropertyConverter<List<String>, String> {

    private static final String SEPARATOR = "/,/,/";

    @Override
    public List<String> convertToEntityProperty(String value) {
        final List<String> strings = new ArrayList<>();
        final String[] parts = value.split(SEPARATOR);
        for (int i = 0; i < parts.length; i++) {
            final String part = parts[i];
            if (!part.trim().isEmpty()) {
                strings.add(part);
            }
        }

        return strings;
    }

    @Override
    public String convertToDatabaseValue(List<String> values) {
        return StringUtils.join(values, SEPARATOR);
    }
}
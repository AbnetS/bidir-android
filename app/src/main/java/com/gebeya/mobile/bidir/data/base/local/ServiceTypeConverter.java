package com.gebeya.mobile.bidir.data.base.local;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;

import io.objectbox.converter.PropertyConverter;

public class ServiceTypeConverter implements PropertyConverter<Service, String> {

    @Override
    public Service convertToEntityProperty(String databaseValue) {
        try {
            return Service.valueOf(databaseValue);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String convertToDatabaseValue(Service entityProperty) {
        return entityProperty.toString();
    }
}

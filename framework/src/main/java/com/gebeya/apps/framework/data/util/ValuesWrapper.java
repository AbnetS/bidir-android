package com.gebeya.apps.framework.data.util;

import android.content.ContentValues;

import com.gebeya.apps.framework.data.column.Column;
import com.gebeya.apps.framework.data.model.BaseModel;

public class ValuesWrapper {

    private ContentValues values;

    public ValuesWrapper() {
        values = new ContentValues();
    }

    public void setValues(ContentValues values) {
        this.values = values;
    }

    public ContentValues getValues() {
        return values;
    }

    public ValuesWrapper put(String key, String value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, String value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, BaseModel value) {
        return put(key, value.id);
    }

    public ValuesWrapper put(Column key, BaseModel value) {
        values.put(key.name, value.id);
        return this;
    }

    public ValuesWrapper put(String key, Byte value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Byte value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Short value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Short value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Integer value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Integer value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Long value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Long value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Float value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Float value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Double value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Double value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Boolean value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, Boolean value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, byte[] value) {
        values.put(key, value);
        return this;
    }

    public ValuesWrapper put(Column key, byte[] value) {
        return put(key.name, value);
    }

    public ValuesWrapper put(String key, Object value) {
        values.put(key, value.toString());
        return this;
    }

    public ValuesWrapper put(Column key, Object value) {
        return put(key.name, value);
    }

    public ValuesWrapper putNull(String key) {
        values.putNull(key);
        return this;
    }

    public ValuesWrapper putNull(Column key) {
        return putNull(key.name);
    }
}

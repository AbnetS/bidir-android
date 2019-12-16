package com.gebeya.apps.framework.data.prefs;

public interface Prefs {
    Prefs open();
    Prefs clear();
    Prefs put(String key, int value);
    int getInt(String key);
    Prefs put(String key, boolean value);
    boolean getBool(String key);
    Prefs put(String key, String value);
    String getString(String key);
    Prefs put(String key, float value);
    float getFloat(String key);
    Prefs put(String key, long value);
    long getLong(String key);
    void close();
}
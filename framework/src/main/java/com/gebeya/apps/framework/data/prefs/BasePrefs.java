package com.gebeya.apps.framework.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class BasePrefs implements Prefs {

    private static Prefs instance;
    private static final String NAME_SETTINGS = "SETTINGS";

    public static Prefs getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BasePrefs.initialize(Context) was never called");
        }
        return instance;
    }

    public static void initialize(Context context) {
        instance = new BasePrefs(context);
    }

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private BasePrefs(Context context) {
        preferences = context.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE);
    }

    @Override
    public Prefs open() {
        editor = preferences.edit();
        return this;
    }

    @Override
    public Prefs clear() {
        editor.clear();
        return this;
    }

    @Override
    public Prefs put(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    @Override
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    @Override
    public Prefs put(String key, boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    @Override
    public boolean getBool(String key) {
        return preferences.getBoolean(key, false);
    }

    @Override
    public Prefs put(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    @Override
    public String getString(String key) {
        return preferences.getString(key, null);
    }

    @Override
    public Prefs put(String key, float value) {
        editor.putFloat(key, value);
        return this;
    }

    @Override
    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    @Override
    public Prefs put(String key, long value) {
        editor.putLong(key, value);
        return this;
    }

    @Override
    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    @Override
    public void close() {
        editor.commit();
    }
}

package com.gebeya.mobile.bidir.impl.util.location.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

public class AppPreference implements PreferenceHelper {

    private final SharedPreferences preferences;
    private final String BOOLEAN_KEY = "boolean_key";
    private final String IS_SELECTED = "is_selected";
    private final String STRING_KEY = "string_key";

    public AppPreference(@NonNull Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Tooth.inject(this, Scopes.SCOPE_ROOT);
    }

    @Override
    public boolean getIndividual() {
        return preferences.getBoolean(BOOLEAN_KEY, true);
    }

    @Override
    public boolean isGroupSelected() {
        return preferences.getBoolean(IS_SELECTED, false);
    }

    @Override
    public String getKey() {
        return preferences.getString(STRING_KEY, "");
    }

    @Override
    public void setIndividual(@NonNull boolean prefKey) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(BOOLEAN_KEY, prefKey);
        editor.apply();
    }

    @Override
    public void setKey(@NonNull String prefKey) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(STRING_KEY, prefKey);
        editor.apply();
    }

    @Override
    public void setGroupedSelected(@NonNull boolean prefKey) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SELECTED, prefKey);
        editor.apply();
    }

    @Override
    public void clearSharedPreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}

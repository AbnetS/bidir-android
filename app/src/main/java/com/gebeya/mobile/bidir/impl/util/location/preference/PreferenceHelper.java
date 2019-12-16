package com.gebeya.mobile.bidir.impl.util.location.preference;

import android.support.annotation.NonNull;

public interface PreferenceHelper {

    boolean getIndividual();

    boolean isGroupSelected();

    String getKey();

    void setIndividual(boolean prefKey);

    void setKey(@NonNull String prefKey);

    void setGroupedSelected(@NonNull boolean prefKey);

    void clearSharedPreference();
}

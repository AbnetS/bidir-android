package com.gebeya.mobile.bidir.ui.home.main;

import android.support.annotation.NonNull;

/**
 * Base interface for the item view on the main screen.
 * This item could be an item showing the client, screening, loan application or A-CAT form
 */
public interface BaseMainItemView {
    void setName(@NonNull String name);
    void setStatus(@NonNull String status);
    void setImage(@NonNull String pictureUrl);
    void toggleCreatedIndicator(boolean show);
}
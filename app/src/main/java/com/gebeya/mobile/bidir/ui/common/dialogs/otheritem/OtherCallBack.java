package com.gebeya.mobile.bidir.ui.common.dialogs.otheritem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface OtherCallBack {
    void onOtherReturned(@NonNull String acatItem, @NonNull String unit, @Nullable String groupName, boolean isChild);
}

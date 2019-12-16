package com.gebeya.mobile.bidir.ui.common.dialogs.creategroup;

import android.support.annotation.NonNull;

public interface CreateGroupCallback {
    void onCreate(@NonNull String groupCode, int memberCount, double loanAmount);
}

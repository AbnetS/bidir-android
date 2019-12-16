package com.gebeya.mobile.bidir.ui.common.dialogs.recordloan;

import android.support.annotation.NonNull;

public interface RecordLoanCallBack {
    void onRecord(@NonNull String loanApproved, @NonNull String clientId);
}

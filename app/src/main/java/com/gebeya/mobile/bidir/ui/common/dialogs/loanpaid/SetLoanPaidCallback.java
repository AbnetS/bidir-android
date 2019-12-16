package com.gebeya.mobile.bidir.ui.common.dialogs.loanpaid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;

public interface SetLoanPaidCallback {
    void onLoanPaidCallBack(@Nullable Client client, @Nullable Group group);
}

package com.gebeya.mobile.bidir.ui.common.dialogs.service;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel K. on 5/31/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ServiceSelectorCallback {
    void onServicesSelected(@NonNull List<String> services, int position);
    void onServiceSelectionCanceled(int position);
}

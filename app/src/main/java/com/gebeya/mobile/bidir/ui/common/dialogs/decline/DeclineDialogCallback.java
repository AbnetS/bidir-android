package com.gebeya.mobile.bidir.ui.common.dialogs.decline;

import android.support.annotation.NonNull;

/**
 * Callback interface for the {@link DeclineDialog}
 *
 * Created by Samuel K. on 12/14/2017.
 * <p>
 * samkura47@gmail.com
 */

public interface DeclineDialogCallback {
    void onDecline(@NonNull String remark, boolean isFinal);
}
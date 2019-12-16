package com.gebeya.mobile.bidir.ui.common.dialogs.formarket;

import android.support.annotation.Nullable;

/**
 * Created by Samuel K. on 8/30/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ForMarketCallback {
    void forMarketUnionCallback(@Nullable String union, @Nullable String unionRemark, boolean isExpected);
    void forMarketFactoryCallback(@Nullable String factoryEstimated, @Nullable String unionRemark, boolean isExpected);
    void forMarketTraderCallback(@Nullable String traderEstimated, @Nullable String traderRemark, boolean isExpected);
}

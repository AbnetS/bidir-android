package com.gebeya.mobile.bidir.ui.summary_yield;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface SummaryYieldContract {

    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull SummaryMenuItem.Label label);
        String getTitle(@NonNull SummaryMenuItem.Label label);
        void setUnit(@NonNull String unit);
        void setUnitPrice(double estimated, double actual);
        void setTotalPrice(double estimated, double actual);

        void setOwnCons(double estimated, double actual);
        void setSeedRes(double estimated, double actual);
        void setForMarket(double estimated, double actual);

        void toggleOwnCons(boolean show);
        void toggleSeedRes(boolean show);
        void toggleForMarket(boolean show);

        void openYieldFragment(@NonNull String clientId, @NonNull String cropACATId);
    }

    interface Presenter extends BasePresenter<View> {

        void onMonitorButtonClicked();
    }


}

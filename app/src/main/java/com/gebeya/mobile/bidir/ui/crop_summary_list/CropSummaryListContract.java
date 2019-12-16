package com.gebeya.mobile.bidir.ui.crop_summary_list;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryCashFlowPresenter;

/**
 * Created by Samuel K. on 8/25/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface CropSummaryListContract {

    interface View extends BaseView<Presenter> {
        void setInputs(@NonNull String estimated, @NonNull String actual);
        void setLabourCosts(@NonNull String estimated, @NonNull String actual);
        void setOtherCosts(@NonNull String estimated, @NonNull String actual);
        void setTotal(@NonNull String estimated, @NonNull String actual);
        void setProbableYield(double value, boolean estimated);
        void setMaximumYield(double value, boolean estimated);
        void setMinimumYield(double value, boolean estimated);
        void setOwnCons(double value, boolean estimated);
        void setSeedRes(double value, boolean estimated);
        void setForMarket(double value, boolean estimated);
        void loadCashFlowData();
    }

    interface Presenter extends CropSummaryCashFlowPresenter<View> {

    }

    interface CashFlowItemView {
        void setName(@NonNull String name);
        void setValue(@NonNull String value);
    }
}

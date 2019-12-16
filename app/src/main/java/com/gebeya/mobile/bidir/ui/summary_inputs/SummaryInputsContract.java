package com.gebeya.mobile.bidir.ui.summary_inputs;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowPresenter;

/**
 * Contract interface for the summary inputs screen.
 */
public interface SummaryInputsContract {

    /**
     * View interface declaration for the summary inputs screen
     */
    interface View extends BaseView<Presenter> {
        void setSeedData(@NonNull String estimated, @NonNull String actual);
        void setFertilizerData(@NonNull String estimated, @NonNull String actual);
        void setChemicalsData(@NonNull String estimated, @NonNull String actual);
        void setSubtotalData(@NonNull String estimated, @NonNull String actual);
        void loadCashFlowData();
    }

    /**
     * Presenter interface declaration for the summary inputs screen
     */
    interface Presenter extends SummaryCashFlowPresenter<View> {

    }

    /**
     * Interface for the cash flow item.
     */
    interface CashFlowItemView {
        void setName(@NonNull String name);
        void setValue(@NonNull String value);
    }
}
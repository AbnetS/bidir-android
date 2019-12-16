package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.mobile.bidir.ui.summary_inputs.SummaryInputsContract;

/**
 * Base interface for the summary presenters.
 */
public interface SummaryCashFlowPresenter<V> extends BasePresenter<V> {
    void onBindItemView(@NonNull SummaryInputsContract.CashFlowItemView holder, int position);
    int getItemCount();
}

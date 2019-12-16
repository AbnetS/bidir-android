package com.gebeya.mobile.bidir.ui.crop_summary;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.mobile.bidir.ui.crop_summary_list.CropSummaryListContract;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface CropSummaryCashFlowPresenter<V> extends BasePresenter<V> {
    void onBindItemView(@NonNull CropSummaryListContract.CashFlowItemView holder, int position);
    int getItemCount();
}

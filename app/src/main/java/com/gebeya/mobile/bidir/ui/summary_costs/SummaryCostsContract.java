package com.gebeya.mobile.bidir.ui.summary_costs;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowPresenter;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;

import java.util.List;

/**
 * Contract interface for the summary costs (labor/other) screens.
 */
public interface SummaryCostsContract {

    /**
     * Interface definition for the view
     */
    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull SummaryMenuItem.Label label);
        String getTitle(@NonNull SummaryMenuItem.Label label);
        void loadEstimatedData();
        void loadActualData();
        void loadCashFlowData();
        void openFragment(@NonNull Bundle arg, @NonNull String clientId,
                          @NonNull String cropACATI,
                          @NonNull SummaryMenuItem.Label label,
                          @NonNull List<ACATCostSection> sections,
                          @NonNull CropACAT cropACAT,
                          @NonNull String sectionId);
    }

    /**
     * Interface definition for the presenter
     */
    interface Presenter extends SummaryCashFlowPresenter<View> {
        void onBindEstimatedItemView(@NonNull SummaryCostsItemView holder, int position);
        int getEstimatedCount();
        void onBindActualItemView(@NonNull SummaryCostsItemView holder, int position);
        int getActualCount();
        void onMonitorButtonClicked(@NonNull Bundle bundle);
    }

    /**
     * Interface for the summary costs item
     */
    interface SummaryCostsItemView {
        void setName(@NonNull String name);
        void setUnit(@NonNull String unit);
        void setQuantity(@NonNull String quantity);
        void setUnitPrice(@NonNull String unitPrice);
        void setTotalPrice(@NonNull String totalPrice);
    }
}
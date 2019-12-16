package com.gebeya.mobile.bidir.ui.questions.acat.estimatedyield;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

public class ACATEstimatedRevenueNetCashFlowItemHolder extends RecyclerView.ViewHolder {

    TextView monthLabel;
    TextView cashLabel;

    public ACATEstimatedRevenueNetCashFlowItemHolder(View itemView) {
        super(itemView);

        monthLabel = BaseActivity.getTv(R.id.cash_flow_month_label, itemView);
        cashLabel = BaseActivity.getTv(R.id.cash_flow_monthly_cash_label, itemView);
    }
}


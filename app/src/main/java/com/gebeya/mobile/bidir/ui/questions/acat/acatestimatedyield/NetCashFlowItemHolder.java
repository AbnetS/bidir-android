package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Separate implementation fo the cumulative net cash flow item holder.
 */
public class NetCashFlowItemHolder extends RecyclerView.ViewHolder implements ACATEstimatedYieldContract.CashFlowItemView {

    private final TextView monthLabel;
    private final TextView valueLabel;

    public NetCashFlowItemHolder(@NonNull View itemView) {
        super(itemView);
        monthLabel = BaseActivity.getTv(R.id.cash_flow_month_label, itemView);
        valueLabel = BaseActivity.getTv(R.id.cash_flow_monthly_cash_label, itemView);
    }

    @Override
    public void setTitle(@NonNull String title) {
        monthLabel.setText(title);
    }

    @Override
    public void setValue(@NonNull String value) {
        int formatted = (int)Double.parseDouble(value);
        valueLabel.setText(String.valueOf(formatted));
    }
}

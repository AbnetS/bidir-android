package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.summary_inputs.SummaryInputsContract;

public class SummaryCashFlowItemHolder extends RecyclerView.ViewHolder implements SummaryInputsContract.CashFlowItemView {

    private final TextView nameLabel;
    private final TextView valueLabel;

    public SummaryCashFlowItemHolder(@NonNull View itemView) {
        super(itemView);

        nameLabel = BaseActivity.getTv(R.id.cash_flow_month_label, itemView);
        valueLabel = BaseActivity.getTv(R.id.cash_flow_monthly_cash_label, itemView);
    }

    @Override
    public void setName(@NonNull String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setValue(@NonNull String value) {
        valueLabel.setText(value);
    }
}

package com.gebeya.mobile.bidir.ui.crop_summary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.crop_summary_list.CropSummaryListContract;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryCashFlowItemHolder extends RecyclerView.ViewHolder implements CropSummaryListContract.CashFlowItemView{

    private final TextView nameLabel;
    private final TextView valueLabel;

    public CropSummaryCashFlowItemHolder(@NonNull View itemView) {
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

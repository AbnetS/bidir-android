package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 3/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATEstimatedYieldCashFlowItemHolder extends RecyclerView.ViewHolder {

    TextView monthLabel;
    EditText cashInput;

    public ACATEstimatedYieldCashFlowItemHolder(View itemView) {
        super(itemView);
        monthLabel = BaseActivity.getTv(R.id.cash_out_flow_item_month_label, itemView);
        cashInput = BaseActivity.getEd(R.id.acat_cash_outflow_input, itemView);
    }
}

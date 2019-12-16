package com.gebeya.mobile.bidir.ui.questions.acat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 3/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsNetCashFlowItemHolder extends RecyclerView.ViewHolder {

    TextView monthLabel;
    TextView cashLabel;

    public ACATQuestionsNetCashFlowItemHolder(View itemView) {
        super(itemView);

        monthLabel = BaseActivity.getTv(R.id.cash_flow_month_label, itemView);
        cashLabel = BaseActivity.getTv(R.id.cash_flow_monthly_cash_label, itemView);
    }
}

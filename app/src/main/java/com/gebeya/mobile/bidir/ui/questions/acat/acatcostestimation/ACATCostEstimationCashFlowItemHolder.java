package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

import java.util.List;

/**
 * Created by Samuel K. on 6/6/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCostEstimationCashFlowItemHolder extends RecyclerView.ViewHolder implements TextWatcher {
    TextView monthLabel;
    EditText cashInput;
    List<String> cashFlowList;
    int position;

    public ACATCostEstimationCashFlowItemHolder(View itemView, List<String> cashFlowList) {
        super(itemView);
        this.cashFlowList = cashFlowList;

        monthLabel = BaseActivity.getTv(R.id.cash_out_flow_item_month_label, itemView);
        cashInput = BaseActivity.getEd(R.id.acat_cash_outflow_input, itemView);
        cashInput.addTextChangedListener(this);
    }

    public void updatePosition(int position) {
        this.position = position;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        cashFlowList.set(position, editable.toString().trim());
    }
}

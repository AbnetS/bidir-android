package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.form.questions.values.InputWatcher;

/**
 * Separate implementation for the cash flow item holder.
 */
public class CashFlowItemHolder extends RecyclerView.ViewHolder implements ACATEstimatedYieldContract.CashFlowItemView {

    private final TextView monthLabel;
    private final EditText cashInput;

    public CashFlowItemHolder(@NonNull View itemView, @NonNull ACATEstimatedYieldContract.Presenter presenter) {
        super(itemView);
        monthLabel = BaseActivity.getTv(R.id.cash_out_flow_item_month_label, itemView);
        cashInput = BaseActivity.getEd(R.id.acat_cash_outflow_input, itemView);
        cashInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                final String text = s.toString().trim();
                presenter.onCashFlowItemUpdated(text, getAdapterPosition());
            }
        });
    }

    @Override
    public void setTitle(@NonNull String title) {
        monthLabel.setText(title);
    }

    @Override
    public void setValue(@NonNull String value) {
        cashInput.setText(value);
    }
}
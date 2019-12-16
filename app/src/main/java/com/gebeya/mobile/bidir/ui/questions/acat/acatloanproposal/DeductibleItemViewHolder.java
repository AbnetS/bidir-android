package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 6/27/2018.
 * <p>
 * samkura47@gmail.com
 */

public class DeductibleItemViewHolder extends RecyclerView.ViewHolder {

    TextView itemLabel;
    EditText itemValue;

    public DeductibleItemViewHolder(View itemView) {
        super(itemView);

        itemLabel = BaseActivity.getTv(R.id.loan_product_item_label, itemView);
        itemValue = BaseActivity.getEd(R.id.loan_product_item_input, itemView);
    }
}

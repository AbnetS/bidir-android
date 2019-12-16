package com.gebeya.mobile.bidir.ui.questions.acat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 3/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATItemCostEstimationViewHolder extends RecyclerView.ViewHolder  {

    TextView itemLabel;
    LinearLayout rowLayout;

    public ACATItemCostEstimationViewHolder(View itemView) {
        super(itemView);

        itemLabel = BaseActivity.getTv(R.id.data_entry_item_button, itemView);
        rowLayout = itemView.findViewById(R.id.data_entry_item_layout);
    }

}

package com.gebeya.mobile.bidir.ui.questions.acat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

;

/**
 * Created by Samuel K. on 3/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsSubTotalItemHolder extends RecyclerView.ViewHolder {

    TextView subTotalItem;

    public ACATQuestionsSubTotalItemHolder(View itemView) {
        super(itemView);

        subTotalItem = BaseActivity.getTv(R.id.subtotal_item_label, itemView);
    }
}

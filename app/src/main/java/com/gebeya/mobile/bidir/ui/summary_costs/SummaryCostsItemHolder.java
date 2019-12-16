package com.gebeya.mobile.bidir.ui.summary_costs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * {@link RecyclerView.ViewHolder} holder implementation for the summary
 * costs item.
 */
public class SummaryCostsItemHolder extends RecyclerView.ViewHolder implements SummaryCostsContract.SummaryCostsItemView {

    private final TextView nameLabel;
    private final TextView unitLabel;
    private final TextView quantityLabel;
    private final TextView unitPriceLabel;
    private final TextView totalPriceLabel;

    public SummaryCostsItemHolder(@NonNull View itemView) {
        super(itemView);

        nameLabel = BaseActivity.getTv(R.id.summaryCostsListItemNameLabel, itemView);
        unitLabel = BaseActivity.getTv(R.id.summaryCostsListItemUnitLabel, itemView);
        quantityLabel = BaseActivity.getTv(R.id.summaryCostsListItemQuantityLabel, itemView);
        unitPriceLabel = BaseActivity.getTv(R.id.summaryCostsListItemUnitPriceLabel, itemView);
        totalPriceLabel = BaseActivity.getTv(R.id.summaryCostsListItemTotalPriceLabel, itemView);
    }

    @Override
    public void setName(@NonNull String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setUnit(@NonNull String unit) {
        unitLabel.setText(unit);
    }

    @Override
    public void setQuantity(@NonNull String quantity) {
        quantityLabel.setText(quantity);
    }

    @Override
    public void setUnitPrice(@NonNull String unitPrice) {
        unitPriceLabel.setText(unitPrice);
    }

    @Override
    public void setTotalPrice(@NonNull String totalPrice) {
        totalPriceLabel.setText(totalPrice);
    }
}
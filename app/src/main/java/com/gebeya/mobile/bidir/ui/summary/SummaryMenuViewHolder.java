package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.util.Fonts;

public class SummaryMenuViewHolder extends RecyclerView.ViewHolder implements SummaryContract.MenuItemView {

    private final SummaryContract.Presenter presenter;
    private final TextView menuLabel;

    public SummaryMenuViewHolder(@NonNull SummaryContract.Presenter presenter, @NonNull View itemView) {
        super(itemView);
        this.presenter = presenter;
        menuLabel = (TextView) ((LinearLayout) itemView).getChildAt(0);
        itemView.setOnClickListener(v -> presenter.onMenuItemSelected(getAdapterPosition()));
    }

    @Override
    public void setLabel(int type, @NonNull SummaryMenuItem.Label label) {
        menuLabel.setText(getText(label));
        menuLabel.setTypeface(type == SummaryMenuItem.TYPE_PARENT ? Fonts.bold : Fonts.normal);
    }

    @Override
    public void setSelected(boolean selected) {
        final int color = selected ? R.color.green_light : R.color.white;
        itemView.setBackgroundColor(
                ContextCompat.getColor(itemView.getContext(), color)
        );
    }

    private String getText(@NonNull SummaryMenuItem.Label label) {
        final int textRes;
        switch (label) {
            case COST_ESTIMATION:
                textRes = R.string.summary_menu_cost_estimation_title_label;
                break;
            case INPUTS_AND_ACTIVITIES_COST:
                textRes = R.string.summary_menu_inputs_and_activities_label;
                break;
            case INPUTS:
                textRes = R.string.summary_menu_inputs_label;
                break;
            case SEEDS:
                textRes = R.string.summary_menu_seeds_label;
                break;
            case FERTILIZERS:
                textRes = R.string.summary_menu_fertilizers_label;
                break;
            case CHEMICALS:
                textRes = R.string.summary_menu_chemicals_label;
                break;
            case LABOR_COST:
                textRes = R.string.summary_menu_labor_costs_label;
                break;
            case OTHER_COSTS:
                textRes = R.string.summary_menu_other_costs_label;
                break;
            case ESTIMATED_YIELD:
                textRes = R.string.summary_menu_estimated_yield_title_label;
                break;
            case PROBABLE_YIELD:
                textRes = R.string.summary_menu_estimated_probable_yield_label;
                break;
            case MAXIMUM_YIELD:
                textRes = R.string.summary_menu_max_yield_label;
                break;
            default:
                textRes = R.string.summary_menu_min_yield_label;
        }

        return itemView.getContext().getString(textRes);
    }
}
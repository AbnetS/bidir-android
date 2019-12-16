package com.gebeya.mobile.bidir.ui.crop_summary;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryMenuViewHolder extends RecyclerView.ViewHolder implements CropSummaryContract.MenuItemView, View.OnClickListener {

    TextView cropLabel;

    private CropSummaryContract.Presenter presenter;

    public CropSummaryMenuViewHolder(View itemView, CropSummaryContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        cropLabel = BaseActivity.getTv(R.id.cropMenuItemNameLabel, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void setCropMenuLabel(@NonNull String cropName) {
        cropLabel.setText(cropName);
    }

    @Override
    public void setSelected(boolean selected) {
        final int color = selected ? R.color.green_light : R.color.white;
        itemView.setBackgroundColor(
                ContextCompat.getColor(itemView.getContext(), color)
        );
    }

    @Override
    public void onClick(View view) {
        presenter.onMenuItemSelected(getAdapterPosition());
    }
}

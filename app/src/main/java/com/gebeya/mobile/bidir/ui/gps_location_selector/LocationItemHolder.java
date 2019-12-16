package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

import javax.annotation.Nonnegative;

/**
 * Location item view holder implementation.
 */
public class LocationItemHolder extends RecyclerView.ViewHolder implements GPSSelectorContract.LocationItemView {

    private final View itemIndicator;
    private final TextView positionLabel;
    private final TextView locationLabel;

    public LocationItemHolder(@Nonnegative View itemView, @NonNull GPSSelectorContract.Presenter presenter) {
        super(itemView);

        itemView.setOnClickListener(v -> presenter.onLocationSelected(getAdapterPosition()));
        itemIndicator = itemView.findViewById(R.id.selectorLocationItemIndicator);
        positionLabel = BaseActivity.getTv(R.id.selectorLocationItemPositionLabel, itemView);
        locationLabel = BaseActivity.getTv(R.id.selectorLocationItemLocationLabel, itemView);
        ImageView removeIcon = itemView.findViewById(R.id.selectorLocationItemRemoveIcon);
        removeIcon.setOnClickListener(v -> presenter.onRemoveLocationPressed(getAdapterPosition()));
    }

    @Override
    public void toggleSelected(boolean selected) {
        itemIndicator.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setPosition(@NonNull String position) {
        positionLabel.setText(position);
    }

    @Override
    public void setLocation(@NonNull String location) {
        locationLabel.setText(location);
    }
}
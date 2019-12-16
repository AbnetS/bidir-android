package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * {@link RecyclerView.Adapter} implementation for the locations list.
 */
public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemHolder> {

    private final GPSSelectorContract.Presenter presenter;
    private final LayoutInflater inflater;

    public LocationItemAdapter(@NonNull GPSSelectorContract.Presenter presenter, @NonNull LayoutInflater inflater) {
        this.presenter = presenter;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public LocationItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.gps_selector_location_item_layout, parent, false);
        return new LocationItemHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationItemHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getLocationCount();
    }
}

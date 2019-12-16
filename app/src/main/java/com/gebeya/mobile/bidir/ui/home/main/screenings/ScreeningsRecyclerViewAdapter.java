package com.gebeya.mobile.bidir.ui.home.main.screenings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Clients RecyclerView adapter for displaying a list of clients (screening fragment)
 */
public class ScreeningsRecyclerViewAdapter extends RecyclerView.Adapter<ComplexScreeningItemViewHolder> {

    private ScreeningsContract.Presenter presenter;

    public ScreeningsRecyclerViewAdapter(ScreeningsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ComplexScreeningItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout, parent, false);
        return new ComplexScreeningItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplexScreeningItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getScreeningCount();
    }
}

package com.gebeya.mobile.bidir.ui.home.main.groupedacat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class GroupedACATRecyclerViewAdapter extends RecyclerView.Adapter<GroupedACATItemViewHolder> {

    private GroupedACATContract.Presenter presenter;

    public GroupedACATRecyclerViewAdapter(@NonNull GroupedACATContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public GroupedACATItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_acat_application_item_layout, parent, false);
        return new GroupedACATItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedACATItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getACATCount();
    }
}

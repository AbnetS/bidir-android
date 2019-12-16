package com.gebeya.mobile.bidir.ui.home.main.groupedscreenings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class GroupedScreeningsRecyclerViewAdapter extends RecyclerView.Adapter<GroupedScreeningsItemViewHolder> {

    private GroupedScreeningsContract.Presenter presenter;

    public GroupedScreeningsRecyclerViewAdapter(@NonNull GroupedScreeningsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public GroupedScreeningsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_applications_item_layout, parent, false);
        return new GroupedScreeningsItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedScreeningsItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getScreeningCount();
    }
}

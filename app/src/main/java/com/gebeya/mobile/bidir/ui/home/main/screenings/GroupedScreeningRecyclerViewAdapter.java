package com.gebeya.mobile.bidir.ui.home.main.screenings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.ui.home.main.clients.GroupItemViewHolder;

public class GroupedScreeningRecyclerViewAdapter extends RecyclerView.Adapter<GroupedComplexScreeningItemViewHolder> {

    private ScreeningsContract.Presenter presenter;

    public GroupedScreeningRecyclerViewAdapter(@NonNull ScreeningsContract.Presenter presenter) {
        this.presenter = presenter;
    }
    @NonNull
    @Override
    public GroupedComplexScreeningItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_layout, parent, false);
        return new GroupedComplexScreeningItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedComplexScreeningItemViewHolder holder, int position) {
        presenter.onGroupBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getGroupCount();
    }
}

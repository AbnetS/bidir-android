package com.gebeya.mobile.bidir.ui.home.main.groupedclient;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class GroupedClientRecyclerViewAdapter extends RecyclerView.Adapter<GroupedClientViewHolder> {

    private GroupedClientContract.Presenter presenter;

    public GroupedClientRecyclerViewAdapter(@NonNull GroupedClientContract.Presenter presenter) {
        this.presenter = presenter;
    }
    @NonNull
    @Override
    public GroupedClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_clients_item_layout, parent, false);
        return new GroupedClientViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedClientViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }


    @Override
    public int getItemCount() {
        return presenter.getClientCount();
    }
}

package com.gebeya.mobile.bidir.ui.home.main.groupedloans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class GroupedLoansRecyclerViewAdapter extends RecyclerView.Adapter<GroupedLoansItemViewHolder> {

    private GroupedLoansContract.Presenter presenter;

    public GroupedLoansRecyclerViewAdapter(@NonNull GroupedLoansContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public GroupedLoansItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_applications_item_layout, parent, false);
        return new GroupedLoansItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedLoansItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getLoanCount();
    }
}

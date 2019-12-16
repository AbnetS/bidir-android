package com.gebeya.mobile.bidir.ui.home.main.clients;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.ui.home.main.screenings.ScreeningsContract;
import com.gebeya.mobile.bidir.ui.home.main.screenings.ScreeningsPresenter;

public class GroupedClientsRecyclerViewAdapter extends RecyclerView.Adapter<GroupItemViewHolder> {

    private ClientsContract.Presenter presenter;

    public GroupedClientsRecyclerViewAdapter(@NonNull ClientsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_client_item_layout, parent, false);
        return new GroupItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
        presenter.onGroupBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getGroupCount();
    }
}

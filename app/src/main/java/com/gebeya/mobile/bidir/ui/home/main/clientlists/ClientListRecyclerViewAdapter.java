package com.gebeya.mobile.bidir.ui.home.main.clientlists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class ClientListRecyclerViewAdapter extends RecyclerView.Adapter<ClientListViewHolder> {

    private ClientListContract.Presenter presenter;

    public ClientListRecyclerViewAdapter(@NonNull ClientListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ClientListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_item_layout, parent, false);
        return new ClientListViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getClientCount();
    }
}

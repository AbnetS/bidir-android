package com.gebeya.mobile.bidir.ui.home.main.clients;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Adapter for the RecyclerView on the {@link ClientsFragment} fragment
 */
public class IndividualClientsRecyclerViewAdapter extends RecyclerView.Adapter<ClientItemViewHolder> {

    private ClientsContract.Presenter presenter;

    public IndividualClientsRecyclerViewAdapter(@NonNull ClientsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ClientItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item_layout, parent, false);
        return new ClientItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getClientCount();
    }
}
package com.gebeya.mobile.bidir.ui.home.main.acatapplications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Created by Samuel K. on 5/9/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATApplicationRecyclerViewAdapter extends RecyclerView.Adapter<ACATApplicationItemViewHolder> {

    private ACATApplicationsContract.Presenter preseenter;

    public ACATApplicationRecyclerViewAdapter(ACATApplicationsContract.Presenter preseenter) {
        this.preseenter = preseenter;
    }


    @NonNull
    @Override
    public ACATApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item_layout, parent, false);
        return new ACATApplicationItemViewHolder(itemView, preseenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ACATApplicationItemViewHolder holder, int position) {
        preseenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return preseenter.getACATApplicationCount();
    }
}

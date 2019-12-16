package com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Created by Samuel K. on 7/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCropItemRecyclerViewAdapter extends RecyclerView.Adapter<ACATCropItemViewHolder>{

    private ACATCropItemContract.Presenter presenter;

    public ACATCropItemRecyclerViewAdapter(ACATCropItemContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ACATCropItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_item_layout, parent, false);
        return new ACATCropItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ACATCropItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getACATCropItemCount();
    }
}

package com.gebeya.mobile.bidir.ui.crop_summary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryMenuAdapter extends RecyclerView.Adapter<CropSummaryMenuViewHolder> {

    private CropSummaryContract.Presenter presenter;

    public CropSummaryMenuAdapter(CropSummaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public CropSummaryMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_summary_menu_label_layout, parent, false);
        return new CropSummaryMenuViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull CropSummaryMenuViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getMenuItemCount();
    }
}

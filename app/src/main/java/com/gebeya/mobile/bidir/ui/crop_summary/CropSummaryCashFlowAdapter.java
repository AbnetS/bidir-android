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

public class CropSummaryCashFlowAdapter extends RecyclerView.Adapter<CropSummaryCashFlowItemHolder> {

    private final CropSummaryCashFlowPresenter presenter;
    private final LayoutInflater inflater;

    public CropSummaryCashFlowAdapter(@NonNull CropSummaryCashFlowPresenter presenter, LayoutInflater inflater) {
        this.presenter = presenter;
        this.inflater = inflater;
    }
    @NonNull
    @Override
    public CropSummaryCashFlowItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.acat_footer_cash_flow_item_layout, parent, false);
        return new CropSummaryCashFlowItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CropSummaryCashFlowItemHolder holder, int position) {
        presenter.onBindItemView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}

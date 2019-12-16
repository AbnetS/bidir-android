package com.gebeya.mobile.bidir.ui.summary_costs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class SummaryEstimatedCostsAdapter extends RecyclerView.Adapter<SummaryCostsItemHolder> {

    private final SummaryCostsContract.Presenter presenter;
    private final LayoutInflater inflater;

    public SummaryEstimatedCostsAdapter(@NonNull SummaryCostsContract.Presenter presenter,
                                        @NonNull LayoutInflater inflater) {
        this.presenter = presenter;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public SummaryCostsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.summary_costs_list_item_layout, parent, false);
        return new SummaryCostsItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryCostsItemHolder holder, int position) {
        presenter.onBindEstimatedItemView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getEstimatedCount();
    }
}

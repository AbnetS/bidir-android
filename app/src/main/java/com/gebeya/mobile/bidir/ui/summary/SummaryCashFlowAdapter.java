package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class SummaryCashFlowAdapter extends RecyclerView.Adapter<SummaryCashFlowItemHolder> {

    private final SummaryCashFlowPresenter presenter;
    private final LayoutInflater inflater;

    public SummaryCashFlowAdapter(@NonNull SummaryCashFlowPresenter presenter, @NonNull LayoutInflater inflater) {
        this.presenter = presenter;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public SummaryCashFlowItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.acat_footer_cash_flow_item_layout, parent, false);
        return new SummaryCashFlowItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryCashFlowItemHolder holder, int position) {
        presenter.onBindItemView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}

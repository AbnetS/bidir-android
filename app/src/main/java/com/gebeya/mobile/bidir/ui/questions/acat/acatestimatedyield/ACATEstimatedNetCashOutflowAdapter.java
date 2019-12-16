package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Adapter implementation for the cumulative net cash outflow data.
 */
public class ACATEstimatedNetCashOutflowAdapter extends RecyclerView.Adapter<NetCashFlowItemHolder> {

    private final ACATEstimatedYieldContract.Presenter presenter;
    private final LayoutInflater inflater;

    public ACATEstimatedNetCashOutflowAdapter(@NonNull LayoutInflater inflater, @NonNull ACATEstimatedYieldContract.Presenter presenter) {
        this.presenter = presenter;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public NetCashFlowItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.acat_footer_cash_flow_item_layout, parent, false);
        return new NetCashFlowItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NetCashFlowItemHolder holder, int position) {
        presenter.onBindNetViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.cashNetFlowCount();
    }
}

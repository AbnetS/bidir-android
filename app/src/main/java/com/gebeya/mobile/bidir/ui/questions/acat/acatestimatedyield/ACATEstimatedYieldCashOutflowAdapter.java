package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Separate adapter to list the months on the yield cash outflow section.
 */
public class ACATEstimatedYieldCashOutflowAdapter extends RecyclerView.Adapter<CashFlowItemHolder> {

    private final ACATEstimatedYieldContract.Presenter presenter;
    private final LayoutInflater inflater;

    public ACATEstimatedYieldCashOutflowAdapter(@NonNull LayoutInflater inflater, @NonNull ACATEstimatedYieldContract.Presenter presenter) {
        this.inflater = inflater;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public CashFlowItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.cash_outflow_item_layout, parent, false);
        return new CashFlowItemHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull CashFlowItemHolder holder, int position) {
        presenter.onBindYieldViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.cashYieldFlowCount();
    }
}

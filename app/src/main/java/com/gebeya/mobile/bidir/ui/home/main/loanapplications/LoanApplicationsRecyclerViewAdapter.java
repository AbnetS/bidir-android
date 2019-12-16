package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * RecyclerView adapter for the LoanApplications, located on the {@link LoanApplicationsFragment}
 * screen
 */
public class LoanApplicationsRecyclerViewAdapter extends RecyclerView.Adapter<LoanApplicationItemViewHolder> {

    private LoanApplicationsContract.Presenter presenter;

    public LoanApplicationsRecyclerViewAdapter(LoanApplicationsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public LoanApplicationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout, parent, false);
        return new LoanApplicationItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(LoanApplicationItemViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getLoanApplicationCount();
    }
}
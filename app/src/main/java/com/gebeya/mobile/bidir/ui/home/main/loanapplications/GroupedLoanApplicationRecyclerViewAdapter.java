package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.ui.form.loanapplication.LoanApplicationContract;

public class GroupedLoanApplicationRecyclerViewAdapter extends RecyclerView.Adapter<GroupedLoanApplicationItemViewHolder> {

    private LoanApplicationsContract.Presenter presenter;

    public GroupedLoanApplicationRecyclerViewAdapter(@NonNull LoanApplicationsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public GroupedLoanApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_layout, parent, false);
        return new GroupedLoanApplicationItemViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedLoanApplicationItemViewHolder holder, int position) {
        presenter.onGroupBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getGroupCount();
    }
}

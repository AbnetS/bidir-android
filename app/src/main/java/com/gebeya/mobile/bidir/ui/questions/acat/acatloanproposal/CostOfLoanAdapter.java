package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;

import java.util.List;

/**
 * Created by Samuel K. on 6/27/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CostOfLoanAdapter extends RecyclerView.Adapter<CostOfLoanItemViewHolder> {

    List<LoanProductItem> loanProductItemList;
    String loanProposed;
    double[] costOfLoanItems;
    double percentageValue;
    private double totalCostOfLoan;
    OnLoanProposalChangedListener listener;
    public CostOfLoanAdapter(OnLoanProposalChangedListener listener, List<LoanProductItem> loanProductItemList, String loanProposed) {
        this.loanProductItemList = loanProductItemList;
        this.loanProposed = loanProposed;
        costOfLoanItems = new double[loanProductItemList.size()];
        this.listener = listener;
    }

    @NonNull
    @Override
    public CostOfLoanItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_product_item_layout, parent, false);

        return new CostOfLoanItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CostOfLoanItemViewHolder holder, int position) {
        LoanProductItem item = loanProductItemList.get(position);
        holder.itemLabel.setText(item.item);
        if (item.percent != 0 && loanProposed != null) {
            if (loanProposed.isEmpty())
                loanProposed = String.valueOf(0);
            percentageValue = Double.parseDouble(loanProposed) * item.percent / 100;
            holder.itemValue.setText(String.valueOf(percentageValue));
            costOfLoanItems[position] = Double.parseDouble(holder.itemValue.getText().toString().trim());

        }
        if (loanProposed == null || loanProposed.isEmpty()) {
            costOfLoanItems[position] = 0;
        }
        if (item.fixedAmount != 0) {
            holder.itemValue.setText(String.valueOf(item.fixedAmount));
            costOfLoanItems[position] = item.fixedAmount;
        }

        totalCostOfLoan = totalCostOfLoan(costOfLoanItems);
        listener.onCostOfLoanChanged(totalCostOfLoan);
        if (loanProposed == null || loanProposed.isEmpty()) {
            listener.onLoanProposedChanged(0);
        } else {
            listener.onLoanProposedChanged(Double.valueOf(loanProposed));
        }
    }

    public void notify(String loanProposed) {
        this.loanProposed = loanProposed;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return loanProductItemList.size();
    }

    private double totalCostOfLoan(double[] deductibleItems) {
        double sum = 0;
        for (int i = 0; i < deductibleItems.length; i++) {
            sum += deductibleItems[i];
        }
        return sum;
    }

    public double getTotalCostOfLoan() {
        return this.totalCostOfLoan;
    }

}

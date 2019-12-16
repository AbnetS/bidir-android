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

public class DeductibleAdapter extends RecyclerView.Adapter<DeductibleItemViewHolder> {

    List<LoanProductItem> loanProductItemList;
    String loanProposed;
    double[] deductibleItems;
    double totalDeductible;
    OnLoanProposalChangedListener listener;
    public DeductibleAdapter(OnLoanProposalChangedListener listener, List<LoanProductItem> loanProductItemList, String loanProposed) {
        this.listener = listener;
        this.loanProductItemList = loanProductItemList;
        this.loanProposed = loanProposed;
        deductibleItems = new double[loanProductItemList.size()];

    }

    @NonNull
    @Override
    public DeductibleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_product_item_layout, parent, false);

        return new DeductibleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeductibleItemViewHolder holder, int position) {
        LoanProductItem item = loanProductItemList.get(position);
        holder.itemLabel.setText(item.item);
        if (item.percent != 0 && loanProposed != null) {
            if (loanProposed.isEmpty())
                loanProposed = String.valueOf(0);
            double value = Double.parseDouble(loanProposed) * item.percent / 100;
            holder.itemValue.setText(String.valueOf(value));
            deductibleItems[position] = value;
        }
        if (loanProposed == null) {
            deductibleItems[position] = 0;
        }
        if (item.fixedAmount != 0) {
            holder.itemValue.setText(String.valueOf(item.fixedAmount));
            deductibleItems[position] = item.fixedAmount;
        }

        totalDeductible = totalDeductible(deductibleItems);
        listener.onDeductibleChanged(totalDeductible);
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

    private double totalDeductible(double[] deductibleItems) {
        double sum = 0;
        for (int i = 0; i < deductibleItems.length; i++) {
            sum += deductibleItems[i];
        }
        return sum;
    }

    public double getTotalDeductible() {
        return this.totalDeductible;
    }

}

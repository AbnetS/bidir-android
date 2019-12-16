package com.gebeya.mobile.bidir.ui.questions.acat.estimatedyield;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATUtility;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ACATEstimatedRevenueNetCashFlowAdapter extends RecyclerView.Adapter<ACATEstimatedRevenueNetCashFlowItemHolder> {

    String[] months;
    List<String> monthList;
    List<String> cashFlowList;
    int index;

    public ACATEstimatedRevenueNetCashFlowAdapter(String firstExpenseMonth, List<String> cashFlowList) {
        this.cashFlowList = new ArrayList<>(cashFlowList);
        months = new DateFormatSymbols().getShortMonths();
        monthList = Arrays.asList(months);
        index = ACATUtility.getMonthIndex(firstExpenseMonth);
        Collections.rotate(monthList, -index);
    }

    @NonNull
    @Override
    public ACATEstimatedRevenueNetCashFlowItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.acat_footer_cash_flow_item_layout, parent, false);

        return new ACATEstimatedRevenueNetCashFlowItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ACATEstimatedRevenueNetCashFlowItemHolder holder, int position) {
        String month = monthList.get(position);
        holder.monthLabel.setText(month);
        holder.cashLabel.setText(cashFlowList.get(position));
    }

    @Override
    public int getItemCount() {
        return cashFlowList.size();
    }

    public void notify(CashFlow cashFlow) {
        cashFlowList.clear();

        cashFlowList.add(String.valueOf(cashFlow.january));
        cashFlowList.add(String.valueOf(cashFlow.february));
        cashFlowList.add(String.valueOf(cashFlow.march));
        cashFlowList.add(String.valueOf(cashFlow.april));
        cashFlowList.add(String.valueOf(cashFlow.may));
        cashFlowList.add(String.valueOf(cashFlow.june));
        cashFlowList.add(String.valueOf(cashFlow.july));
        cashFlowList.add(String.valueOf(cashFlow.august));
        cashFlowList.add(String.valueOf(cashFlow.september));
        cashFlowList.add(String.valueOf(cashFlow.october));
        cashFlowList.add(String.valueOf(cashFlow.november));
        cashFlowList.add(String.valueOf(cashFlow.december));

        Collections.rotate(cashFlowList, -index);
        notifyDataSetChanged();
    }
}

package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

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

/**
 * Created by Samuel K. on 6/6/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCostEstimationCashFlowAdapter extends RecyclerView.Adapter<ACATCostEstimationCashFlowItemHolder> {
    List<String> cashFlowList;
    List<String> monthList;
    int index;

    public ACATCostEstimationCashFlowAdapter(String firstExpenceMonth, List<String> cashFlowList) {
        this.cashFlowList = new ArrayList<>(cashFlowList);

        monthList = Arrays.asList(new DateFormatSymbols().getShortMonths());
        index = ACATUtility.getMonthIndex(firstExpenceMonth);
        Collections.rotate(monthList, -index);
    }

    @Override
    public ACATCostEstimationCashFlowItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_outflow_item_layout, parent, false);

        return new ACATCostEstimationCashFlowItemHolder(itemView, cashFlowList);
    }

    @Override
    public void onBindViewHolder(ACATCostEstimationCashFlowItemHolder holder, int position) {
        String month = monthList.get(position);
        holder.monthLabel.setText(month);
        holder.updatePosition(holder.getAdapterPosition());
        cashFlowList.set(position, holder.cashFlowList.get(position));
        holder.cashInput.setText(cashFlowList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return monthList.size();
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

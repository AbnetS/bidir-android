package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATUtility;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Samuel K. on 3/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATEstimatedYieldAdapter extends RecyclerView.Adapter<ACATEstimatedYieldCashFlowItemHolder> {

    String[] months;
    List<String> monthList;

    public ACATEstimatedYieldAdapter(String firstExpenceMonth) {
        months = new DateFormatSymbols().getShortMonths();
        monthList = Arrays.asList(months);
        int index = ACATUtility.getMonthIndex(firstExpenceMonth);
        Collections.rotate(monthList, -index);
    }

    @Override
    public ACATEstimatedYieldCashFlowItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_outflow_item_layout, parent, false);

        return new ACATEstimatedYieldCashFlowItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ACATEstimatedYieldCashFlowItemHolder holder, int position) {
        String month = monthList.get(position);
        holder.monthLabel.setText(month);
    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }
}

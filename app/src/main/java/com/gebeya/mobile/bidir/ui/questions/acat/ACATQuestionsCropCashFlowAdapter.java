package com.gebeya.mobile.bidir.ui.questions.acat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Samuel K. on 3/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsCropCashFlowAdapter extends RecyclerView.Adapter<ACATQuestionsCropCashFlowItemHolder> {

    String[] months;
    List<String> monthList;

    public ACATQuestionsCropCashFlowAdapter() {
        months = new DateFormatSymbols().getShortMonths();
        monthList = Arrays.asList(months);
    }


    @Override
    public ACATQuestionsCropCashFlowItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_cash_flow_layout, parent, false);

        return new ACATQuestionsCropCashFlowItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ACATQuestionsCropCashFlowItemHolder holder, int position) {
        String month = monthList.get(position);
        holder.monthLabel.setText(month);
    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }
}

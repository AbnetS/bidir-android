package com.gebeya.mobile.bidir.ui.questions.acat;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

import java.util.ArrayList;

/**
 * Created by Samuel K. on 3/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATItemCostEstimationAdapter extends RecyclerView.Adapter<ACATItemCostEstimationViewHolder> {

    ArrayList<String> items;
    int row_index = 0;

    public ACATItemCostEstimationAdapter(@NonNull ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public ACATItemCostEstimationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_entry_left_layout, parent, false);
        return new ACATItemCostEstimationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ACATItemCostEstimationViewHolder holder, int position) {
        String item = items.get(position);
        holder.itemLabel.setText(item);
        holder.rowLayout.setOnClickListener(view -> {
            row_index=position;
            notifyDataSetChanged();
        });
        if(row_index==position){
            holder.rowLayout.setBackgroundColor(Color.parseColor("#b2dc8a"));
        }
        else
        {
            holder.rowLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

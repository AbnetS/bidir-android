package com.gebeya.mobile.bidir.ui.questions.acat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Samuel K. on 3/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsSubTotalAdapter extends RecyclerView.Adapter<ACATQuestionsSubTotalItemHolder> {

    List<String> list ;

    public ACATQuestionsSubTotalAdapter() {
        list = Arrays.asList("0", "0", "0");
    }

    @Override
    public ACATQuestionsSubTotalItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtotal_item_layout, parent, false);

        return new ACATQuestionsSubTotalItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ACATQuestionsSubTotalItemHolder holder, int position) {
        String listItem = list.get(position);
        holder.subTotalItem.setText(listItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

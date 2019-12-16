package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Created by Samuel K. on 5/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class PreliminaryInformationAdapter extends RecyclerView.Adapter<PreliminaryInformationItemHolder>{

    private final PreliminaryInformationContract.Presenter presenter;

    public PreliminaryInformationAdapter(PreliminaryInformationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public PreliminaryInformationItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preliminary_information_crop_check_item_layout, parent, false);
        return new PreliminaryInformationItemHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull PreliminaryInformationItemHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getAnswerCount();
    }
}

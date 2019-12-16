package com.gebeya.mobile.bidir.ui.questions.acat.acatapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Created by Samuel K. on 5/13/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATPreliminaryInfoAdapter extends RecyclerView.Adapter<ACATPreliminaryInfoItemHolder> {

    private ACATPreliminaryInfoContract.Presenter presenter;

    public ACATPreliminaryInfoAdapter(ACATPreliminaryInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ACATPreliminaryInfoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.acat_preliminary_info_item_layout, parent, false);
        return new ACATPreliminaryInfoItemHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ACATPreliminaryInfoItemHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getCropCount();
    }
}

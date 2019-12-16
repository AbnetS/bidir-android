package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class SummaryMenuAdapter extends RecyclerView.Adapter<SummaryMenuViewHolder> {

    private final LayoutInflater inflater;
    private final SummaryContract.Presenter presenter;

    public SummaryMenuAdapter(@NonNull LayoutInflater inflater, @NonNull SummaryContract.Presenter presenter) {
        this.inflater = inflater;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public SummaryMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(getLayout(viewType), parent, false);
        return new SummaryMenuViewHolder(presenter, itemView);
    }

    private int getLayout(int viewType) {
        switch (viewType) {
            case SummaryMenuItem.TYPE_TITLE:
                return R.layout.summary_menu_title_layout;
            case SummaryMenuItem.TYPE_SUBTITLE:
                return R.layout.summary_menu_subtitle_layout;
            case SummaryMenuItem.TYPE_PARENT:
                return R.layout.summary_menu_parent_layout;
            default:
                return R.layout.summary_menu_child_layout;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryMenuViewHolder holder, int position) {
        presenter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getType(position);
    }

    @Override
    public int getItemCount() {
        return presenter.itemCount();
    }
}

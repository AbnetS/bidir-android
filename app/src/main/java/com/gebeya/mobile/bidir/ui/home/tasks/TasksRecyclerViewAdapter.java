package com.gebeya.mobile.bidir.ui.home.tasks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

/**
 * Tasks RecylclerView data adapter
 */
public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private TasksContract.Presenter presenter;

    public TasksRecyclerViewAdapter(TasksContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getTasksCount();
    }
}

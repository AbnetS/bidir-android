package com.gebeya.mobile.bidir.ui.home.tasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * ItemView implementation for the Task view row item
 */
public class TaskViewHolder extends RecyclerView.ViewHolder implements TasksContract.TaskItemView, View.OnClickListener {

    TextView messageLabel;
    TextView timeStampLabel;

    private TasksContract.Presenter presenter;

    public TaskViewHolder(View itemView, TasksContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        messageLabel = BaseActivity.getTv(R.id.taskItemLayoutNameLabel, itemView);
        timeStampLabel = BaseActivity.getTv(R.id.taskItemLayoutTimeStampLabel, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        presenter.onTaskSelected(getAdapterPosition());
    }

    @Override
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void setTimeStamp(String timeStamp) {
        timeStampLabel.setText(timeStamp);
    }
}

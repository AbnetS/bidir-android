package com.gebeya.mobile.bidir.ui.home.tasks;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.data.task.Task;

/**
 * Interface contracts for the Tasks screen.
 */
public interface TasksContract {

    /**
     * Presenter interface contract for the tasks screen
     */
    interface Presenter extends BasePresenter<View> {
        void onBindRowView(TaskItemView view, int position);
        void onTaskSelected(int position);
        void onLoadingCompleted();
        void onLoadingFailed(@NonNull Throwable throwable);
        int getTasksCount();
    }

    /**
     * View interface contract for the tasks screen
     */
    interface View extends BaseView<Presenter> {
        void showTasks();
        void toggleNoTasksLabel(boolean show);
        void showProgressWheel();
        void hideProgressWheel();
        void openSingleTask(@NonNull Task task);
    }

    /**
     * Item View for a single task
     */
    interface TaskItemView {
        void setMessage(String message);
        void setTimeStamp(String timeStamp);
    }
}
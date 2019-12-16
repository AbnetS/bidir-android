package com.gebeya.mobile.bidir.ui.home.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.form.loanapplication.LoanApplicationActivity;
import com.gebeya.mobile.bidir.ui.form.screening.ScreeningActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;

/**
 * View implementation for the Tasks view interface
 */
public class TasksFragment extends BaseFragment implements TasksContract.View {

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    private TasksContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TasksRecyclerViewAdapter adapter;

    private TextView noDataLabel;
    private View tasksProgressWheel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = getView(R.id.tasksRecyclerView);
        noDataLabel = getView(R.id.tasksNoDataLabel);
        tasksProgressWheel = getView(R.id.tasksProgressWheel);

        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);
        adapter = new TasksRecyclerViewAdapter(presenter);

        return root;
    }

    @Override
    public void toggleNoTasksLabel(boolean show) {
        noDataLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgressWheel() {
        tasksProgressWheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressWheel() {
        tasksProgressWheel.setVisibility(View.GONE);
    }

    @Override
    public void showTasks() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openSingleTask(@NonNull Task task) {
        final Class<?> target;
        final String referenceIdArg;
        final String taskArg;

        switch (task.referenceType) {
            case TaskHelper.REF_SCREENING:
                target = ScreeningActivity.class;
                referenceIdArg = ScreeningActivity.ARG_SCREENING_ID;
                taskArg = ScreeningActivity.ARG_TASK;
                break;
            case TaskHelper.REF_CLIENT_ACAT:
                target = ACATCropItemActivity.class;
                referenceIdArg = ACATCropItemActivity.ARG_CLIENT_ACAT_ID;
                taskArg = ACATCropItemActivity.ARG_TASK;
                break;
            default:
                target = LoanApplicationActivity.class;
                referenceIdArg = LoanApplicationActivity.ARG_APPLICATION_ID;
                taskArg = LoanApplicationActivity.ARG_TASK;

        }

        final Intent intent = new Intent(getContext(), target);
        intent.putExtra(referenceIdArg, task.referenceId);
        intent.putExtra(taskArg, task);
        startActivity(intent);
    }

    @Override
    public void attachPresenter(TasksContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
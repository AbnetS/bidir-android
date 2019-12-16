package com.gebeya.mobile.bidir.ui.home.tasks;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskRepoSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;
import com.gebeya.mobile.bidir.impl.util.time.ReadableTime;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Implementation for the tasks contract presenter interface
 */
public class TasksPresenter implements TasksContract.Presenter {

    private TasksContract.View view;

    @Inject TaskRepoSource repository;
    private RequestState state;
    private ReadableTime readableTime;
    @Inject SchedulersProvider schedulers;

    public TasksPresenter(@NonNull RequestState state,
                          @NonNull ReadableTime readableTime) {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);

        this.state = state;
        this.readableTime = readableTime;
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        if (state.loading()) {
            view.showProgressWheel();
            view.toggleNoTasksLabel(false);
            return;
        }

        state.setLoading();
        view.showProgressWheel();
        view.toggleNoTasksLabel(false);

        repository.fetchForceAll()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(tasks -> {
                            state.setComplete();
                            onLoadingCompleted();
                        },
                        throwable -> {
                            state.setError(throwable);
                            onLoadingFailed(throwable);
                        });
    }

    @Override
    @SuppressLint("CheckResult")
    public void onLoadingCompleted() {
        if (view == null) return;

        view.hideProgressWheel();

        repository.getAll()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(tasks -> {
                    view.toggleNoTasksLabel(tasks.isEmpty());
                    view.showTasks();

                    state.reset();
                });
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        if (view == null) return;

        view.hideProgressWheel();
        view.toggleNoTasksLabel(true);

        state.reset();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onBindRowView(TasksContract.TaskItemView view, int position) {
        repository.get(position)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
            final Task task = data.get();
            view.setMessage(task.description);
            view.setTimeStamp(readableTime.toReadable(task.updatedAt));
        });
    }

    @Override
    @SuppressLint("CheckResult")
    public void onTaskSelected(int position) {
        repository.get(position)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> view.openSingleTask(data.get()));
    }

    @Override
    public int getTasksCount() {
        return repository.size();
    }

    @Override
    public void attachView(TasksContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public TasksContract.View getView() {
        return view;
    }
}
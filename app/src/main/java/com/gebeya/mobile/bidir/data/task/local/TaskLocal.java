package com.gebeya.mobile.bidir.data.task.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.data.task.Task_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Local implementation for te {@link TaskLocalSource} interface
 */
public class TaskLocal extends BaseLocalSource implements TaskLocalSource {

    private final Box<Task> box;

    public TaskLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Task.class);
    }

    @Override
    public Observable<Data<Task>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<Task>> get(@NonNull String id) {
        final List<Task> tasks = box.find(Task_._id, id);
        return Observable.just(new Data<>(tasks.isEmpty() ? null : tasks.get(0)));
    }

    @Override
    public Observable<Data<Task>> get(int position) {
        final List<Task> tasks = box.getAll();
        return Observable.just(new Data<>(tasks.isEmpty() ? null : tasks.get(position)));
    }

    @Override
    public Observable<List<Task>> getAll() {
        final List<Task> tasks = box.getAll();
        Collections.sort(tasks, new TaskComparator());
        return Observable.just(tasks);
    }

    @Override
    public Observable<Task> put(@NonNull Task item) {
        box.query().equal(Task_._id, item._id).build().remove();
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Observable<List<Task>> putAll(@NonNull List<Task> tasks) {
        box.removeAll();
        box.put(tasks);
        final List<Task> filtered = box.query()
                .equal(Task_.status, TaskHelper.STATUS_COMPLETED)
                .sort(new TaskComparator())
                .build()
                .find();
        return Observable.just(filtered);
    }

    @Override
    public int size() {
        return (int) box.count();
    }
}

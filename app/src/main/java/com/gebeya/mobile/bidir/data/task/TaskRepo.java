package com.gebeya.mobile.bidir.data.task;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.task.local.TaskLocalSource;
import com.gebeya.mobile.bidir.data.task.remote.TaskRemoteSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Repository implementation for the {@link TaskRepoSource}
 * contract
 */
public class TaskRepo implements TaskRepoSource {

    @Inject TaskLocalSource local;
    @Inject TaskRemoteSource remote;

    public TaskRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<Data<Task>> get(@NonNull String id) {
        return local.get(id);
    }

    @Override
    public Observable<Data<Task>> get(int position) {
        return local.get(position);
    }

    @Override
    public Observable<Data<Task>> first() {
        return local.first();
    }

    @Override
    public Observable<List<Task>> getAll() {
        return local.getAll();
    }

    @Override
    public Observable<List<Task>> fetchAll() {
        return local.getAll()
                .flatMap(tasks ->
                        tasks.isEmpty() ? fetchForceAll() : Observable.just(tasks)
                );
    }

    @Override
    public Observable<List<Task>> fetchForceAll() {
        return remote.getAll()
                .flatMap(local::putAll);
    }

    @Override
    public Completable pushStatus(@NonNull Task task, @NonNull String status, @NonNull String comment) {
        return remote.updateStatus(task, status, comment)
                .flatMapCompletable(apiTask -> {
                    local.put(apiTask);
                    return Completable.complete();
                });
    }

    @Override
    public int size() {
        return local.size();
    }
}
package com.gebeya.mobile.bidir.data.groupedacat.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class GroupedACATLocal extends BaseLocalSource implements GroupedACATLocalSource {

    private final Box<GroupedACAT> box;
    private final Box<ACATApplication> acatApplicationBox;

    public GroupedACATLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(GroupedACAT.class);
        acatApplicationBox = store.boxFor(ACATApplication.class);
    }

    @Override
    public Completable removeAll() {
        box.removeAll();
        acatApplicationBox.query().notEqual(ACATApplication_.groupedACATId, 0).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable remove(int position) {
        final List<GroupedACAT> groupedACATS = box.getAll();

        if (!groupedACATS.isEmpty()) {
            acatApplicationBox.query().equal(ACATApplication_.groupedACATId,
                    groupedACATS.get(position).id).build().remove();
            box.remove(groupedACATS.get(position));
        }

        return Completable.complete();
    }

    @Override
    public Completable remove(@NonNull String id) {
        final List<GroupedACAT> groupedACATS = box.getAll();

        if (!groupedACATS.isEmpty()) {
            acatApplicationBox.query().equal(ACATApplication_.groupedACATId,
                    groupedACATS.get(0).id).build().remove();

            for (ACATApplication acatApplication : groupedACATS.get(0).acatApplications) {
                acatApplicationBox.query().equal(ACATApplication_._id, acatApplication._id).build().remove();
            }
        }

        box.query().equal(GroupedACAT_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public Observable<List<GroupedACAT>> getAll() {
        final List<GroupedACAT> groupedACATS = box.getAll();
        return Observable.just(groupedACATS);
    }

    @Override
    public Observable<Data<GroupedACAT>> get(@NonNull String id) {
        final List<GroupedACAT> groupedACATS = box.find(GroupedACAT_._id, id);
        return Observable.just(new Data<>(groupedACATS.isEmpty() ? null : groupedACATS.get(0)));
    }

    @Override
    public Observable<Data<GroupedACAT>> get(int position) {
        final List<GroupedACAT> groupedACATS = box.getAll();
        return Observable.just(new Data<>(groupedACATS.isEmpty() ? null : groupedACATS.get(position)));
    }

    @Override
    public Observable<Data<GroupedACAT>> getByGroupId(@NonNull String groupId) {
        final List<GroupedACAT> groupedACATS = box.find(GroupedACAT_.groupId, groupId);
        return Observable.just(new Data<>(groupedACATS.isEmpty() ? null : groupedACATS.get(groupedACATS.size() - 1))); // get the latest application
    }

    @Override
    public Observable<Data<GroupedACAT>> first() {
        return get(0);
    }

    @Override
    public int size() {
        return (int) box.count();
    }

    @Override
    public Observable<List<GroupedACAT>> putAll(@NonNull List<GroupedACAT> groupedACATS) {
        box.removeAll();
        box.put(groupedACATS);
        return Observable.just(groupedACATS);
    }

    @Override
    public Observable<GroupedACAT> put(@NonNull GroupedACAT groupedACAT) {
        remove(groupedACAT._id);
        box.put(groupedACAT);
        return Observable.just(groupedACAT);
    }


}

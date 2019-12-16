package com.gebeya.mobile.bidir.data.groupedlist.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedList;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedList_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/11/2018.
 */

public class GroupedListLocal extends BaseLocalSource implements GroupedListLocalSource {

    private final Box<GroupedList> box;

    public GroupedListLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(GroupedList.class);
    }

    @Override
    public Observable<Data<GroupedList>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<GroupedList>> get(int position) {
        final List<GroupedList> groupedLists = box.getAll();
        return Observable.just(new Data<>(groupedLists.isEmpty() ? null : groupedLists.get(position)));
    }

    @Override
    public Observable<Data<GroupedList>> get(@NonNull String id) {
        final List<GroupedList> groupedLists = box.find(GroupedList_._id, id);
        return Observable.just(new Data<>(groupedLists.isEmpty() ? null : groupedLists.get(0)));
    }

    @Override
    public Observable<List<GroupedList>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<GroupedList> put(@NonNull GroupedList groupedList) {
        box.put(groupedList);
        return Observable.just(groupedList);
    }

    @Override
    public Observable<List<GroupedList>> putAll(@NonNull List<GroupedList> groupedLists) {
        box.put(groupedLists);
        return Observable.just(groupedLists);
    }

    @Override
    public Observable<List<GroupedList>> getByCostList(@NonNull String parentCostListId) {
        final List<GroupedList> groupedLists = box.query()
                .equal(GroupedList_.parentCostListId, "\"" + parentCostListId + "\"")
                .build()
                .find();
        return Observable.just(groupedLists);


    }
}

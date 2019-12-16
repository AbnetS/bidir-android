package com.gebeya.mobile.bidir.data.costlist.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.costlist.CostList;
import com.gebeya.mobile.bidir.data.costlist.CostList_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/11/2018.
 */

public class CostListLocal extends BaseLocalSource implements CostListLocalSource {
    private final Box<CostList> box;

    public CostListLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(CostList.class);
    }

    @Override
    public Observable<Data<CostList>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<CostList>> get(int position) {
        final List<CostList> costLists = box.getAll();
        return Observable.just(new Data<>(costLists.isEmpty() ? null : costLists.get(position)));
    }

    @Override
    public Observable<Data<CostList>> get(@NonNull String id) {
        final List<CostList> costLists = box.find(CostList_._id, id);
        return Observable.just(new Data<>(costLists.isEmpty() ? null : costLists.get(0)));
    }

    @Override
    public Observable<List<CostList>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<CostList> put(@NonNull CostList costList) {
        box.put(costList);
        return Observable.just(costList);
    }

    @Override
    public Observable<List<CostList>> putAll(@NonNull List<CostList> costLists) {
        box.put(costLists);
        return Observable.just(costLists);
    }

}

package com.gebeya.mobile.bidir.data.yieldconsumption.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/16/2018.
 */

public class YieldConsumptionLocal extends BaseLocalSource implements YieldConsumptionLocalSource {

    private final Box<YieldConsumption> box;

    public YieldConsumptionLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(YieldConsumption.class);
    }

    @Override
    public Observable<Data<YieldConsumption>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<YieldConsumption>> get(int position) {
        final List<YieldConsumption> yieldConsumptions = box.getAll();
        return Observable.just(new Data<>(yieldConsumptions.isEmpty() ? null : yieldConsumptions.get(position)));
    }

    @Override
    public Observable<Data<YieldConsumption>> get(@NonNull String id) {
        final List<YieldConsumption> yieldConsumptions = box.find(YieldConsumption_._id, id);
        return Observable.just(new Data<>(yieldConsumptions.isEmpty() ? null : yieldConsumptions.get(0)));
    }

    @Override
    public Observable<List<YieldConsumption>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<YieldConsumption> put(@NonNull YieldConsumption yieldConsumption) {
        box.query().equal(YieldConsumption_._id, yieldConsumption._id)
                .build()
                .remove();
        yieldConsumption.id = 0;
        box.put(yieldConsumption);
        return Observable.just(yieldConsumption);
    }

    @Override
    public Observable<List<YieldConsumption>> putAll(@NonNull List<YieldConsumption> yieldConsumptions) {
        box.put(yieldConsumptions);
        return Observable.just(yieldConsumptions);
    }
}

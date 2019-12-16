package com.gebeya.mobile.bidir.data.marketdetails.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.marketdetails.MarketDetails;
import com.gebeya.mobile.bidir.data.marketdetails.MarketDetails_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/15/2018.
 */

public class MarketDetailsLocal extends BaseLocalSource implements MarketDetailsLocalSource {

    private final Box<MarketDetails> box;

    public MarketDetailsLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(MarketDetails.class);
    }

    @Override
    public Observable<Data<MarketDetails>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<MarketDetails>> get(int position) {
        final List<MarketDetails> marketDetails = box.getAll();
        return Observable.just(new Data<>(marketDetails.isEmpty() ? null : marketDetails.get(position)));
    }

    @Override
    public Observable<List<MarketDetails>> getByYieldConsumption(String yieldConsumptionId, String type) {
        final List<MarketDetails> marketDetails = box.query()
                .equal(MarketDetails_.yieldConsumptionID, yieldConsumptionId)
                .and()
                .equal(MarketDetails_.type, type)
                .build()
                .find();
        return Observable.just(marketDetails);


    }

    @Override
    public Observable<Data<MarketDetails>> get(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<List<MarketDetails>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<MarketDetails> put(@NonNull MarketDetails marketDetails) {
        box.put(marketDetails);
        return Observable.just(marketDetails);
    }

    @Override
    public Observable<List<MarketDetails>> putAll(@NonNull List<MarketDetails> marketDetails) {
        box.put(marketDetails);
        return Observable.just(marketDetails);
    }
}

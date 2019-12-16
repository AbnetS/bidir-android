package com.gebeya.mobile.bidir.data.cashflow.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CashFlowLocal extends BaseLocalSource implements CashFlowLocalSource {
    private final Box<CashFlow> box;

    public CashFlowLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(CashFlow.class);
    }
    @Override
    public Observable<Data<CashFlow>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<CashFlow>> get(int position) {
        final List<CashFlow> cashFlows = box.getAll();
        return Observable.just(new Data<>(cashFlows.isEmpty() ? null : cashFlows.get(position)));
    }

    @Override
    public Observable<Data<CashFlow>> get (String id){
        final List<CashFlow> cashFlows = box.find(CashFlow_.referenceId, id);
        return Observable.just(new Data<>(cashFlows.isEmpty() ? null : cashFlows.get(0)));
    }

    @Override
    public Observable<List<CashFlow>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<CashFlow> put(@NonNull CashFlow cashFlow) {
        box.query().equal(CashFlow_.referenceId, cashFlow.referenceId)
                .and()
                .equal(CashFlow_.type, cashFlow.type)
                .and()
                .equal(CashFlow_.classification, cashFlow.classification)
                .build().remove();
        box.put(cashFlow);
        return Observable.just(cashFlow);
    }

    @Override
    public Observable<List<CashFlow>> putAll(@NonNull List<CashFlow> cashFlows) {
        //TODO: The following line is commented out purposely
        //box.removeAll();
        box.put(cashFlows);
        return Observable.just(cashFlows);
    }

    //TO BE IMPLEMENTED

    @Override
    public Observable<List<CashFlow>> get(@NonNull String referenceId, @NonNull String type, @NonNull String classification) {
        final List<CashFlow> cashFlows = box.query()
                .equal(CashFlow_.referenceId, referenceId)
                .and()
                .equal(CashFlow_.type, type)
                .and()
                .equal(CashFlow_.classification, classification)
                .build()
                .find();
        return Observable.just(cashFlows);
    }

    @Override
    public Observable<CashFlow> put(@NonNull String referenceId, @NonNull String referenceType) {
        return null;
    }

    @Override
    public Observable<CashFlow> putNewCashFlow(@NonNull CashFlow cashFlow) {
        box.put(cashFlow);
        return Observable.just(cashFlow);
    }


    @Override
    public Observable<CashFlow> updateACATItemRefId(@NonNull CashFlow cashflow, String acatItemId) {
        CashFlow fetched = box.get(cashflow.id);
        box.remove(fetched.id); //remove the item
        fetched.referenceId = acatItemId; //Update the CashFlow Id with the API Id

        box.put(fetched);
        return Observable.just(fetched);
    }
}

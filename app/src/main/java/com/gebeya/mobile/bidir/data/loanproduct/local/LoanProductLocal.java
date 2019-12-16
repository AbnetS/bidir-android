package com.gebeya.mobile.bidir.data.loanproduct.local;

/**
 * Created by abuti on 5/7/2018.
 */

import android.support.annotation.NonNull;


import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implenentation for the {@link LoanProductLocalSource} interface.
 */

public class LoanProductLocal extends BaseLocalSource implements LoanProductLocalSource {

    private final Box<LoanProduct> box;

    public LoanProductLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(LoanProduct.class);
    }

    @Override
    public Observable<Data<LoanProduct>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<LoanProduct>> get(int position) {
        final List<LoanProduct> loanProducts = box.getAll();
        return Observable.just(new Data<>(loanProducts.isEmpty() ? null : loanProducts.get(position)));
    }

    @Override
    public Observable<Data<LoanProduct>> get(@NonNull String id) {
        final List<LoanProduct> loanProducts = box.find(LoanProduct_._id, id);
        return Observable.just(new Data<>(loanProducts.isEmpty() ? null : loanProducts.get(0)));
    }

    @Override
    public Observable<List<LoanProduct>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<LoanProduct> put(@NonNull LoanProduct loanProduct) {
        box.put(loanProduct);
        return Observable.just(loanProduct);
    }

    @Override
    public Observable<List<LoanProduct>> putAll(@NonNull List<LoanProduct> loanProducts) {
        box.put(loanProducts);
        return Observable.just(loanProducts);
    }
}


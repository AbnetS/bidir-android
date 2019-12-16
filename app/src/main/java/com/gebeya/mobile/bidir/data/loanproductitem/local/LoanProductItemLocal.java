package com.gebeya.mobile.bidir.data.loanproductitem.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/7/2018.
 */

public class LoanProductItemLocal extends BaseLocalSource implements LoanProductItemLocalSource{
    private final Box<LoanProductItem> box;

    public LoanProductItemLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(LoanProductItem.class);
    }

    @Override
    public Observable<Data<LoanProductItem>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<LoanProductItem>> get(int position) {
        final List<LoanProductItem> loanProductItems = box.getAll();
        return Observable.just(new Data<>(loanProductItems.isEmpty() ? null : loanProductItems.get(position)));
    }

    @Override
    public Observable<Data<LoanProductItem>> get(@NonNull String id) {
        final List<LoanProductItem> loanProductItems = box.find(LoanProductItem_._id, id);
        return Observable.just(new Data<>(loanProductItems.isEmpty() ? null : loanProductItems.get(0)));
    }

    @Override
    public Observable<List<LoanProductItem>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<LoanProductItem> put(@NonNull LoanProductItem loanProductItem) {
        box.put(loanProductItem);
        return Observable.just(loanProductItem);
    }

    @Override
    public Observable<List<LoanProductItem>> putAll(@NonNull List<LoanProductItem> loanProductItems) {
        box.put(loanProductItems);
        return Observable.just(loanProductItems);
    }
}


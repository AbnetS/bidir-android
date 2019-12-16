package com.gebeya.mobile.bidir.data.loanproduct;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanproduct.local.LoanProductLocalSource;
import com.gebeya.mobile.bidir.data.loanproduct.remote.LoanProductRemoteSource;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.gebeya.mobile.bidir.data.loanproductitem.local.LoanProductItemLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/9/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProductRepo implements LoanProductRepoSource {

    @Inject LoanProductLocalSource localSource;
    @Inject LoanProductRemoteSource remoteSource;
    @Inject LoanProductItemLocalSource loanProductItemLocal;

    public LoanProductRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<List<LoanProduct>> fetchAll() {
        return localSource.getAll()
                .flatMap(loanProducts -> loanProducts.isEmpty() ? fetchForceAll() : Observable.just(loanProducts));
    }

    @Override
    public Observable<List<LoanProduct>> fetchForceAll() {
        return remoteSource.getAll()
                .flatMap(responses -> {
                    for (LoanProductResponse response : responses) {
                        localSource.put(response.loanProduct);

                        List<LoanProductItem> costOfLoan = new ArrayList<>();
                        costOfLoan.addAll(response.costOfLoan);
                        loanProductItemLocal.putAll(costOfLoan);
                        List<LoanProductItem> deductibles = new ArrayList<>();

                        deductibles.addAll(response.deductibles);
                        loanProductItemLocal.putAll(deductibles);
                    }
                    return localSource.getAll();
                });
    }

    @Override
    public Observable<List<LoanProduct>> getAll() {
        return localSource.getAll();
    }

    @Override
    public Observable<Data<LoanProduct>> get(@NonNull String id) {
        return localSource.get(id);
    }

    @Override
    public Observable<Data<LoanProduct>> get(int position) {
        return localSource.get(position);
    }

    @Override
    public Observable<Data<LoanProduct>> first() {
        return localSource.first();
    }
}

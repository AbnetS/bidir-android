package com.gebeya.mobile.bidir.data.loanapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implementation of the {@link LoanApplicationLocalSource} interface
 */
public class LoanApplicationLocal extends BaseLocalSource implements LoanApplicationLocalSource {

    private final Box<LoanApplication> box;

    public LoanApplicationLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(LoanApplication.class);
    }

    @Override
    public Observable<List<LoanApplication>> getAllByClient(@NonNull String clientId) {
        final List<LoanApplication> applications = box.find(LoanApplication_.clientId, clientId);
        return Observable.just(applications);
    }

    @Override
    public Observable<List<LoanApplication>> getAcceptedClient(@NonNull String status) {
        final List<LoanApplication> loanApplications = box.find(LoanApplication_.status, status);
        return Observable.just(loanApplications);
    }

    @Override
    public Observable<Data<LoanApplication>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<LoanApplication>> get(@NonNull String id) {
        final List<LoanApplication> applications = box.find(LoanApplication_._id, id);
        return Observable.just(new Data<>(applications.isEmpty() ? null : applications.get(0)));
    }

    @Override
    public Observable<Data<LoanApplication>> get(int position) {
        final List<LoanApplication> applications = box.getAll();
        return Observable.just(new Data<>(applications.isEmpty() ? null : applications.get(position)));
    }

    @Override
    public Observable<List<LoanApplication>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<LoanApplication> put(@NonNull LoanApplication application) {
        box.query().equal(LoanApplication_._id, application._id).build().remove();
        box.put(application);
        return Observable.just(application);
    }

    @Override
    public Observable<List<LoanApplication>> putAll(@NonNull List<LoanApplication> applications) {
        box.removeAll();
        box.put(applications);
        return Observable.just(applications);
    }

    @Override
    public int size() {
        return (int) box.count();
    }
}
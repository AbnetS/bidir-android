package com.gebeya.mobile.bidir.data.groupedcomplexloan.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication_;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationComparator;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class GroupComplexLoanLocal extends BaseLocalSource implements GroupedComplexLoanLocalSource{

    private final Box<GroupedComplexLoan> box;
    private final Box<ComplexLoanApplication> complexLoanApplicationBox;

    public GroupComplexLoanLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(GroupedComplexLoan.class);
        complexLoanApplicationBox = store.boxFor(ComplexLoanApplication.class);
    }

    @Override
    public Completable removeAll() {
        box.removeAll();
        complexLoanApplicationBox.query().notEqual(ComplexLoanApplication_.groupedComplexLoanId, 0).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable remove(int position) {
        final List<GroupedComplexLoan> loans = box.getAll();

        if(!loans.isEmpty()) {
            complexLoanApplicationBox.query().equal(ComplexLoanApplication_.groupedComplexLoanId,
                    loans.get(position).id).build().remove();
            box.remove(loans.get(position));
        }

        return Completable.complete();
    }

    @Override
    public Completable remove(@NonNull String id) {
        final List<GroupedComplexLoan> loans = box.getAll();

        if(!loans.isEmpty()) {
            complexLoanApplicationBox.query().equal(ComplexLoanApplication_.groupedComplexLoanId,
                    loans.get(0).id).build().remove();

            for (ComplexLoanApplication loanApplication : loans.get(0).complexLoans) {
                complexLoanApplicationBox.query().equal(ComplexLoanApplication_._id, loanApplication._id).build().remove();
            }
        }

        box.query().equal(GroupedComplexLoan_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public Observable<List<GroupedComplexLoan>> getAll() {
        final List<GroupedComplexLoan> groupedLoans = box.getAll();
        return Observable.just(groupedLoans);
    }

    @Override
    public Observable<Data<GroupedComplexLoan>> get(@NonNull String id) {
        final List<GroupedComplexLoan> loans = box.find(GroupedComplexLoan_._id, id);
        return Observable.just(new Data<>(loans.isEmpty() ? null : loans.get(0)));
    }

    @Override
    public Observable<Data<GroupedComplexLoan>> getByGroupId(@NonNull String groupId) {
        final List<GroupedComplexLoan> loans = box.find(GroupedComplexLoan_.groupId, groupId);
        return Observable.just(new Data<>(loans.isEmpty() ? null : loans.get(loans.size() - 1))); // this is to get the latest application. find a better way.
    }

    @Override
    public Observable<Data<GroupedComplexLoan>> get(int position) {
        final List<GroupedComplexLoan> loans = box.getAll();
        return Observable.just(new Data<>(loans.isEmpty() ? null : loans.get(position)));
    }

    @Override
    public Observable<Data<GroupedComplexLoan>> first() {
        return get(0);
    }

    @Override
    public int size() {
        return (int) box.count();
    }

    @Override
    public Observable<List<GroupedComplexLoan>> putAll(@NonNull List<GroupedComplexLoan> loans) {
        box.removeAll();
        box.put(loans);
        return Observable.just(loans);
    }

    @Override
    public Observable<GroupedComplexLoan> put(@NonNull GroupedComplexLoan loan) {
        remove(loan._id);
        box.put(loan);
        return Observable.just(loan);
    }
}

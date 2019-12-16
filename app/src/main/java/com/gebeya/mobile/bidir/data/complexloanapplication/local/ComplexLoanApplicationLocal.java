package com.gebeya.mobile.bidir.data.complexloanapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication_;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationParser;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link ComplexLoanApplicationLocalSource} source.
 */
public class ComplexLoanApplicationLocal extends BaseLocalSource implements ComplexLoanApplicationLocalSource {

    private final Box<ComplexLoanApplication> box;

    @Inject
    public ComplexLoanApplicationLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ComplexLoanApplication.class);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getByStatus(@NonNull String status) {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, status)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();
        return Observable.just(applications);
    }


    @Override
    public Completable updateWithLocalIds(List<ComplexLoanApplication> remoteLoanApplications) {
        final int length = remoteLoanApplications.size();
        for (int i = 0; i < length; i++) {
            ComplexLoanApplication remoteLoanApplication = remoteLoanApplications.get(i);
            List<ComplexLoanApplication> localApplications = box.find(ComplexLoanApplication_._id, remoteLoanApplication._id);
            if (!localApplications.isEmpty()) {
                ComplexLoanApplication localApplication = localApplications.get(0);
                remoteLoanApplication.id = localApplication.id;
            }
        }

        return Completable.complete();
    }

    @Override
    public Observable<ComplexLoanApplication> markForUpload(@NonNull ComplexLoanApplication application) {
        application.uploaded = false;
        application.modified = true;
        application.updatedAt = new DateTime();
        box.put(application);
        return Observable.just(application);
    }

    @Override
    public List<ComplexLoanApplication> getAllModifiedNonUploaded() {
        return box.query()
                .equal(ComplexLoanApplication_.modified, true)
                .and()
                .equal(ComplexLoanApplication_.uploaded, false)
                .build().find();
    }

    @Override
    public Observable<Data<ComplexLoanApplication>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ComplexLoanApplication>> get(@NonNull String id) {
        final List<ComplexLoanApplication> applications = box.find(ComplexLoanApplication_._id, id);
        return Observable.just(new Data<>(applications.isEmpty() ? null : applications.get(0)));
    }

    @Override
    public Observable<Data<ComplexLoanApplication>> get(int position) {
        final List<ComplexLoanApplication> applications = box.getAll();
        return Observable.just(new Data<>(applications.isEmpty() ? null : applications.get(position)));
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getAll() {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_NEW)
                .or()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_IN_PROGRESS)
                .or()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_SUBMITTED)
                .or()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_DECLINED_FINAL)
                .or()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW)
                .and()
                .equal(ComplexLoanApplication_.forGroup, false)
                .and()
                .equal(ComplexLoanApplication_.groupedComplexLoanId, 0)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();

        return Observable.just(applications);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getNewLoans() {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_NEW)
                .and()
                .equal(ComplexLoanApplication_.forGroup, false)
                .and()
                .equal(ComplexLoanApplication_.groupedComplexLoanId, 0)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();
        return Observable.just(applications);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getInprogressLoans() {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_IN_PROGRESS)
                .and()
                .equal(ComplexLoanApplication_.forGroup, false)
                .and()
                .equal(ComplexLoanApplication_.groupedComplexLoanId, 0)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();
        return Observable.just(applications);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getSubmittedLoans() {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_SUBMITTED)
                .and()
                .equal(ComplexLoanApplication_.forGroup, false)
                .and()
                .equal(ComplexLoanApplication_.groupedComplexLoanId, 0)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();
        return Observable.just(applications);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getApprovedLoans() {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_ACCEPTED)
                .and()
                .equal(ComplexLoanApplication_.forGroup, false)
                .and()
                .equal(ComplexLoanApplication_.groupedComplexLoanId, 0)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();
        return Observable.just(applications);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> getDeclinedLoans() {
        final List<ComplexLoanApplication> applications = box.query()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_DECLINED_FINAL)
                .or()
                .equal(ComplexLoanApplication_.status, ComplexLoanApplicationParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW)
                .and()
                .equal(ComplexLoanApplication_.forGroup, false)
                .and()
                .equal(ComplexLoanApplication_.groupedComplexLoanId, 0)
                .sort(new ComplexLoanApplicationComparator())
                .build()
                .find();
        return Observable.just(applications);
    }

    @Override
    public Observable<ComplexLoanApplication> put(@NonNull ComplexLoanApplication item) {
        box.query().equal(ComplexLoanApplication_._id, item._id).build().remove();
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Observable<List<ComplexLoanApplication>> putAll(@NonNull List<ComplexLoanApplication> applications) {
        box.removeAll();
        box.put(applications);
        return Observable.just(applications);
    }

    @Override
    public Completable remove(int position) {
        throw new RuntimeException("Use the remove(String) version instead!");
    }

    @Override
    public Completable remove(@NonNull String id) {
        box.query().equal(ComplexLoanApplication_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public int size() {
        throw new RuntimeException("Don't use this method as the size is different from the getAllByIds() method!");
    }
}
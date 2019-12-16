package com.gebeya.mobile.bidir.data.acatapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationComparator;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationLatest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication_;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */

public class ACATApplicationLocal extends BaseLocalSource implements ACATApplicationLocalSource{
    private final Box<ACATApplication> box;

    @Inject
    public ACATApplicationLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(ACATApplication.class);
    }

    @Override
    public Observable<Data<ACATApplication>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ACATApplication>> get(int position) {
        final List<ACATApplication> acatApplications = box.getAll();
        return Observable.just(new Data<>(acatApplications.isEmpty() ? null : acatApplications.get(position)));
    }

    @Override
    public Observable<Data<ACATApplication>> get(@NonNull String id) {
        final List<ACATApplication> acatApplications = box.find(ACATApplication_._id, id);
        return Observable.just(new Data<>(acatApplications.isEmpty() ? null : acatApplications.get(0)));
    }

    @Override
    public Observable<Data<ACATApplication>> getACATByClient(@NonNull String clientId) {
        final List<ACATApplication> acatApplications = box.query()
                .equal(ACATApplication_.clientID, clientId)
                .and()
                .notEqual(ACATApplication_.status, ACATApplicationParser.STATUS_LOCAL_LOAN_PAID)
                .build()
                .find();
        Collections.sort(acatApplications, new ACATApplicationLatest());
        return Observable.just(new Data<>(acatApplications.isEmpty() ? null : acatApplications.get(0)));
    }

    @Override
    public Observable<Data<ACATApplication>> getLoanAuthorizedClient(@NonNull String clientId) {
        List<ACATApplication> acatApplications = box.query()
                .equal(ACATApplication_.clientID, clientId)
                .and()
                .equal(ACATApplication_.status, ACATApplicationParser.STATUS_LOCAL_AUTHORIZED)
                .build()
                .find();
        if (acatApplications.isEmpty()) {
            acatApplications = box.query()
                    .equal(ACATApplication_.clientID, clientId)
                    .and()
                    .equal(ACATApplication_.status, ACATApplicationParser.STATUS_LOCAL_LOAN_GRANTED)
                    .build()
                    .find();
        }
        return Observable.just(new Data<>(acatApplications.isEmpty() ? null : acatApplications.get(0)));
    }

    @Override
    public Observable<List<ACATApplication>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<ACATApplication> markForUpload(@NonNull ACATApplication acatApplication) {
        acatApplication.modified = true;
        acatApplication.uploaded = false;
        acatApplication.updatedAt = new DateTime();

        put(acatApplication);
        //box.put(acatApplication);

        return Observable.just(acatApplication);
    }
    @Override
    public Observable<ACATApplication> put(@NonNull ACATApplication acatApplication) {
        box.query().equal(ACATApplication_._id, acatApplication._id)
                .build().remove();
        acatApplication.id = 0;
        box.put(acatApplication);
        return Observable.just(acatApplication);
    }

    @Override
    public Observable<List<ACATApplication>> putAll(@NonNull List<ACATApplication> acatApplications) {
        box.put(acatApplications);
        return Observable.just(acatApplications);
    }

    @Override
    public Completable remove(@NonNull String clientId) {
        box.query().equal(ACATApplication_.clientID, clientId).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable updateWithLocalIds(List<ACATApplication> remoteACATApplications) {
        final int length = remoteACATApplications.size();
        for (int i = 0; i < length; i++) {
            ACATApplication remoteACATApplication = remoteACATApplications.get(i);
            List<ACATApplication> localACATApplications = box.find(ACATApplication_._id, remoteACATApplication._id);
            if (!localACATApplications.isEmpty()) {
                ACATApplication localACATApplication = localACATApplications.get(0);
                remoteACATApplication.id = localACATApplication.id;
            }
        }

        return Completable.complete();
    }
}

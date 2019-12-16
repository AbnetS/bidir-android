package com.gebeya.mobile.bidir.data.acatapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationSync;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationSync_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.Client_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;
import java.util.TooManyListenersException;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class ACATApplicationSyncLocal extends BaseLocalSource implements ACATApplicationSyncLocalSource {

    private final Box<ACATApplicationSync> box;

    public ACATApplicationSyncLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(ACATApplicationSync.class);
    }

    @Override
    public Observable<List<ACATApplicationSync>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<Data<ACATApplicationSync>> get(@NonNull String id) {
        final List<ACATApplicationSync> acatApplicationSyncList = box.find(ACATApplicationSync_.acatAppId, id);
        return Observable.just(new Data<>(acatApplicationSyncList.isEmpty() ? null : acatApplicationSyncList.get(0)));
    }

    @Override
    public Observable<Data<ACATApplicationSync>> get(int position) {
        final List<ACATApplicationSync> acatApplicationSyncList = box.getAll();
        return Observable.just(new Data<>(acatApplicationSyncList.isEmpty() ? null : acatApplicationSyncList.get(position)));
    }

    @Override
    public Observable<Data<ACATApplicationSync>> getByClientId(@NonNull String clientId) {
        final List<ACATApplicationSync> acatApplicationSyncList = box.find(ACATApplicationSync_.clientId, clientId);
        return Observable.just(new Data<>(acatApplicationSyncList.isEmpty() ? null : acatApplicationSyncList.get(0)));
    }

    @Override
    public Observable<Data<ACATApplicationSync>> first() {
        return get(0);
    }

    @Override
    public Observable<List<ACATApplicationSync>> putAll(@NonNull List<ACATApplicationSync> acatApplicationSyncList) {
        box.put(acatApplicationSyncList);
        return Observable.just(acatApplicationSyncList);
    }

    @Override
    public Observable<ACATApplicationSync> put(@NonNull ACATApplicationSync acatApplicationSync) {
        box.query().equal(ACATApplicationSync_.acatAppId, acatApplicationSync.acatAppId)
                .build().remove();
        acatApplicationSync.id = 0;
        box.put(acatApplicationSync);
        return Observable.just(acatApplicationSync);
    }

    @Override
    public Completable remove(@NonNull String clientId) {
        box.query().equal(ACATApplicationSync_.clientId, clientId).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable removeAll(List<String> membersId) {
        for (String id : membersId) {
            box.query().equal(ACATApplicationSync_.clientId, id).build().remove();
        }
        return Completable.complete();
    }

    @Override
    public List<ACATApplicationSync> getAllModifiedACATApplication() {
        return box.query()
                .equal(ACATApplicationSync_.modified, true)
                .and()
                .equal(ACATApplicationSync_.uploaded, false)
                .build()
                .find();

    }

    @Override
    public Observable<Boolean> hasUnSyncedData() {
        final List<ACATApplicationSync> modified = getAllModifiedACATApplication();
        if (!modified.isEmpty()) return Observable.just(true);
        return Observable.just(false);
    }

    @Override
    public Observable<ACATApplicationSync> markForUpload(@NonNull String id) {
        final List<ACATApplicationSync> acatApplicationSyncList = box.find(ACATApplicationSync_.acatAppId, id);
        if (!acatApplicationSyncList.isEmpty()){
            ACATApplicationSync item = acatApplicationSyncList.get(0);
            item.modified = true;
            item.uploaded = false;
            item.updatedAt = new DateTime();

            put(item);
            //box.put(acatApplication);

            return Observable.just(item);
        }
        return null;

    }
}

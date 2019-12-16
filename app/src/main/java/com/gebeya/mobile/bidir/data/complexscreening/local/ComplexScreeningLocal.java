package com.gebeya.mobile.bidir.data.complexscreening.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening_;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningParser;
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
 * Concrete implementation for the {@link ComplexScreeningLocalSource} source.
 */
public class ComplexScreeningLocal extends BaseLocalSource implements ComplexScreeningLocalSource {

    private final Box<ComplexScreening> box;

    @Inject
    public ComplexScreeningLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ComplexScreening.class);
    }

    @Override
    public Observable<Data<ComplexScreening>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ComplexScreening>> get(@NonNull String id) {
        final List<ComplexScreening> screenings = box.find(ComplexScreening_._id, id);
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(0)));
    }

    @Override
    public Observable<Data<ComplexScreening>> get(int position) {
        final List<ComplexScreening> screenings = box.getAll();
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(position)));
    }

    @Override
    public Observable<List<ComplexScreening>> getAll() {
        final List<ComplexScreening> screenings = box.query()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_NEW)
                .or()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_SUBMITTED)
                .or()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_IN_PROGRESS)
                .or()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_DECLINED_FINAL)
                .or()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW)
                .and()
                .equal(ComplexScreening_.forGroup, false)
                .and()
                .equal(ComplexScreening_.groupedComplexScreeningId, 0) // TODO: added for back compatibility with API. Remove when updated.
                .sort(new ComplexScreeningComparator())
                .build()
                .find();
        return Observable.just(screenings);
    }

    @Override
    public Observable<ComplexScreening> markForUpload(@NonNull ComplexScreening screening) {
        screening.uploaded = false;
        screening.modified = true;
        screening.updatedAt = new DateTime();
        box.put(screening);
        return Observable.just(screening);
    }

    @Override
    public List<ComplexScreening> getAllModifiedNonUploaded() {
        return box.query()
                .equal(ComplexScreening_.modified, true)
                .and()
                .equal(ComplexScreening_.uploaded, false)
                .and()
                .equal(ComplexScreening_.pendingCreation, false)
                .build().find();
    }

    @Override
    public Observable<Boolean> hasUnSyncedData() {
        final List<ComplexScreening> modified = getAllModifiedNonUploaded();
        if (!modified.isEmpty()) return Observable.just(true);

        final List<ComplexScreening> created = getAllPendingCreationNonUploaded();
        if (!created.isEmpty()) return Observable.just(true);

        return Observable.just(false);
    }

    @Override
    public List<ComplexScreening> getAllPendingCreationNonUploaded() {
        return box.query()
                .equal(ComplexScreening_.pendingCreation, true)
                .build()
                .find();
    }

    @Override
    public Observable<List<ComplexScreening>> getNewScreenings() {
        final List<ComplexScreening> screenings = box.query()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_NEW)
                .and()
                .equal(ComplexScreening_.forGroup, false)
                .and()
                .equal(ComplexScreening_.groupedComplexScreeningId, 0)
                .build()
                .find();
        Collections.sort(screenings, new ComplexScreeningComparator());

        return Observable.just(screenings);
    }

    @Override
    public Observable<List<ComplexScreening>> getInprogressScreenings() {
        final List<ComplexScreening> screenings = box.query()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_IN_PROGRESS)
                .and()
                .equal(ComplexScreening_.forGroup, false)
                .and()
                .equal(ComplexScreening_.groupedComplexScreeningId, 0)
                .build()
                .find();
        Collections.sort(screenings, new ComplexScreeningComparator());

        return Observable.just(screenings);
    }

    @Override
    public Observable<List<ComplexScreening>> getSubmittedScreenings() {
        final List<ComplexScreening> screenings = box.query()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_SUBMITTED)
                .and()
                .equal(ComplexScreening_.forGroup, false)
                .and()
                .equal(ComplexScreening_.groupedComplexScreeningId, 0)
                .build()
                .find();
        Collections.sort(screenings, new ComplexScreeningComparator());

        return Observable.just(screenings);
    }

    @Override
    public Observable<List<ComplexScreening>> getEligibleScreenings() {
        final List<ComplexScreening> screenings = box.query()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_APPROVED)
                .and()
                .equal(ComplexScreening_.forGroup, false)
                .and()
                .equal(ComplexScreening_.groupedComplexScreeningId, 0)
                .build()
                .find();
        Collections.sort(screenings, new ComplexScreeningComparator());

        return Observable.just(screenings);
    }

    @Override
    public Observable<List<ComplexScreening>> getRejectedScreenings() {
        final List<ComplexScreening> screenings = box.query()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_DECLINED_FINAL)
                .or()
                .equal(ComplexScreening_.status, ComplexScreeningParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW)
                .and()
                .equal(ComplexScreening_.forGroup, false)
                .and()
                .equal(ComplexScreening_.groupedComplexScreeningId, 0)
                .build()
                .find();
        Collections.sort(screenings, new ComplexScreeningComparator());

        return Observable.just(screenings);
    }

    @Override
    public Completable updateWithLocalIds(List<ComplexScreening> remoteScreenings) {
        final int length = remoteScreenings.size();
        for (int i = 0; i < length; i++) {
            ComplexScreening remoteScreening = remoteScreenings.get(i);
            List<ComplexScreening> localScreenings = box.find(ComplexScreening_._id, remoteScreening._id);
            if (!localScreenings.isEmpty()) {
                ComplexScreening localScreening = localScreenings.get(0);
                remoteScreening.id = localScreening.id;
            }
        }

        return Completable.complete();
    }

    @Override
    public Observable<ComplexScreening> put(@NonNull ComplexScreening item) {
        box.query().equal(ComplexScreening_._id, item._id).build().remove();
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Observable<List<ComplexScreening>> putAll(@NonNull List<ComplexScreening> screenings) {
        box.removeAll();
        box.put(screenings);
        return Observable.just(screenings);
    }

    @Override
    public Completable remove(int position) {
        throw new RuntimeException("Use the remove(String) version instead!");
    }

    @Override
    public Completable remove(@NonNull String id) {
        box.query().equal(ComplexScreening_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public int size() {
        throw new RuntimeException("Don't use this method as the size is different from the getAllByIds() method!");
    }
}

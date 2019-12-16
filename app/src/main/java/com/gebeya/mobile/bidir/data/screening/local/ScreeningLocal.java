package com.gebeya.mobile.bidir.data.screening.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.screening.Screening;
import com.gebeya.mobile.bidir.data.screening.ScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.screening.Screening_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Local Screening data source implementation
 */
public class ScreeningLocal extends BaseLocalSource implements ScreeningLocalSource {

    private final Box<Screening> box;

    public ScreeningLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        this.box = store.boxFor(Screening.class);
    }

    @Override
    public Observable<Data<Screening>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<Screening>> get(@NonNull String id) {
        final List<Screening> screenings = box.find(Screening_._id, id);
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(0)));
    }

    @Override
    public Observable<Data<Screening>> get(int position) {
        final List<Screening> screenings = box.getAll();
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(position)));
    }

    @Override
    public Observable<List<Screening>> getAll() {
        final List<Screening> screenings = box.query()
                .equal(Screening_.status, ScreeningStatusHelper.NEW)
                .or()
                .equal(Screening_.status, ScreeningStatusHelper.IN_PROGRESS)
                .or()
                .equal(Screening_.status, ScreeningStatusHelper.DECLINED_FINAL)
                .or()
                .equal(Screening_.status, ScreeningStatusHelper.DECLINED_UNDER_REVIEW)
                .build()
                .find();
        return Observable.just(screenings);
    }

    @Override
    public Observable<Screening> put(@NonNull Screening screening) {
        box.query().equal(Screening_._id, screening._id).build().remove();
        box.put(screening);
        return Observable.just(screening);
    }

    @Override
    public Observable<List<Screening>> putAll(@NonNull List<Screening> screenings) {
        box.removeAll();
        box.put(screenings);
        return Observable.just(screenings);
    }

    @Override
    public Completable remove(int position) {
        final Screening screening = box.getAll().get(position);
        box.remove(screening);
        return Completable.complete();
    }

    @Override
    public Completable remove(@NonNull String id) {
        box.query().equal(Screening_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public int size() {
        return (int) box.count();
    }
}
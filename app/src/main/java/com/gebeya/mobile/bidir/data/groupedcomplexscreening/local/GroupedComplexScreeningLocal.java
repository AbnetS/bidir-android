package com.gebeya.mobile.bidir.data.groupedcomplexscreening.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening_;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningComparator;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanParser;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening_;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningParser;
import com.gebeya.mobile.bidir.data.groups.Group_;
import com.gebeya.mobile.bidir.data.groups.local.GroupComparator;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.home.main.groupedscreenings.GroupedScreeningsPresenter;

import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class GroupedComplexScreeningLocal extends BaseLocalSource implements GroupedComplexScreeningLocalSource {

    private final Box<GroupedComplexScreening> box;
    private final Box<ComplexScreening> complexScreeningBox;

    public GroupedComplexScreeningLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(GroupedComplexScreening.class);
        complexScreeningBox = store.boxFor(ComplexScreening.class);
    }

    @Override
    public Completable removeAll() {
        box.removeAll();
        complexScreeningBox.query().notEqual(ComplexScreening_.groupedComplexScreeningId, 0).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable remove(int position) {
        final List<GroupedComplexScreening> screenings = box.getAll();

        if (!screenings.isEmpty()) {
            complexScreeningBox.query().equal(ComplexScreening_.groupedComplexScreeningId,
                    screenings.get(position).id).build().remove();
            box.remove(screenings.get(position));
        }

        return Completable.complete();
    }

    @Override
    public Completable remove(@NonNull String id) {
        final List<GroupedComplexScreening> screenings = box.getAll();

        if (!screenings.isEmpty()) {
            complexScreeningBox.query().equal(ComplexScreening_.groupedComplexScreeningId,
                    screenings.get(0).id).build().remove();

            for (ComplexScreening screening : screenings.get(0).complexScreenings) {
                complexScreeningBox.query().equal(ComplexScreening_._id, screening._id).build().remove();
            }
        }

        box.query().equal(GroupedComplexScreening_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public Observable<Data<GroupedComplexScreening>> get(@NonNull String id) {
        final List<GroupedComplexScreening> screenings = box.find(GroupedComplexScreening_._id, id);
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(0)));
    }

    @Override
    public Observable<Data<GroupedComplexScreening>> getByGroupId(@NonNull String groupId) {
        final List<GroupedComplexScreening> screenings = box.find(GroupedComplexScreening_.groupId, groupId);
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(screenings.size() - 1))); // get the latest one.
    }

    @Override
    public Observable<Data<GroupedComplexScreening>> get(int position) {
        final List<GroupedComplexScreening> screenings = box.getAll();
        return Observable.just(new Data<>(screenings.isEmpty() ? null : screenings.get(position)));
    }

    @Override
    public Observable<Data<GroupedComplexScreening>> first() {
        return get(0);
    }

    @Override
    public int size() {
        return (int) box.count();
    }

    @Override
    public Observable<List<GroupedComplexScreening>> putAll(@NonNull List<GroupedComplexScreening> screenings) {
        box.removeAll();
        box.put(screenings);
        return Observable.just(screenings);
    }

    @Override
    public Observable<GroupedComplexScreening> put(@NonNull GroupedComplexScreening screening) {
        remove(screening._id);
        box.put(screening);
        return Observable.just(screening);
    }

    @Override
    public Observable<List<GroupedComplexScreening>> getAll() {
        final List<GroupedComplexScreening> screenings = box.query()
                .equal(GroupedComplexScreening_.status, GroupedComplexScreeningParser.STATUS_LOCAL_NEW)
                .or()
                .equal(GroupedComplexScreening_.status, GroupedComplexScreeningParser.STATUS_LOCAL_SUBMITTED)
                .or()
                .equal(GroupedComplexScreening_.status, GroupedComplexScreeningParser.STATUS_LOCAL_IN_PROGRESS)
                .or()
                .equal(GroupedComplexScreening_.status, GroupedComplexScreeningParser.STATUS_API_APPROVED)
                .or()
                .equal(GroupedComplexScreening_.status, GroupedComplexScreeningParser.STATUS_LOCAL_DECLINED_FINAL)
                .or()
                .equal(GroupedComplexScreening_.status, GroupedComplexScreeningParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW)
                .sort(new GroupedScreeningComparator())
                .build()
                .find();
        return Observable.just(screenings);
    }
}

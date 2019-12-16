package com.gebeya.mobile.bidir.data.acatrevenuesection.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection_;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/13/2018.
 */

public class ACATRevenueSectionLocal extends BaseLocalSource implements ACATRevenueSectionLocalSource {

    private final Box<ACATRevenueSection> box;

    public ACATRevenueSectionLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(ACATRevenueSection.class);
    }

    @Override
    public Observable<Data<ACATRevenueSection>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ACATRevenueSection>> get(int position) {
        final List<ACATRevenueSection> acatRevenueSections = box.getAll();
        return Observable.just(new Data<>(acatRevenueSections.isEmpty() ? null : acatRevenueSections.get(position)));
    }

    @Override
    public Observable<Data<ACATRevenueSection>> get(@NonNull String id) {
        final List<ACATRevenueSection> acatRevenueSections = box.find(ACATRevenueSection_._id, id);
        return Observable.just(new Data<>(acatRevenueSections.isEmpty() ? null : acatRevenueSections.get(0)));
    }

    @Override
    public Observable<List<ACATRevenueSection>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<List<ACATRevenueSection>> getAllParentSections() {
        final List<ACATRevenueSection> sections = box.query()
                .isNull(ACATRevenueSection_.parentSectionID)
                .build().find();
        return Observable.just(sections);
    }

    @Override
    public Observable<List<ACATRevenueSection>> getAllByIds(@Nullable List<String> ids) {
        if (ids == null || ids.isEmpty()) return Observable.just(new ArrayList<>());
        final List<ACATRevenueSection> revenueSections = new ArrayList<>();
        for (String id : ids) {
            final List<ACATRevenueSection> sections = box.find(ACATRevenueSection_._id, id);
            revenueSections.addAll(sections);
        }

        Collections.sort(revenueSections, new ACATRevenueSectionComparator());

        return Observable.just(revenueSections);
    }

    @Override
    public Observable<ACATRevenueSection> markForUpload(@NonNull ACATRevenueSection section) {
        section.modified = true;
        section.uploaded = false;
        section.updatedAt = new DateTime();

        //box.put(section);
        put(section);

        return Observable.just(section);
    }

    @Override
    public Observable<ACATRevenueSection> put(@NonNull ACATRevenueSection acatRevenueSection) {
        box.query().equal(ACATRevenueSection_._id, acatRevenueSection._id).build().remove();
        box.put(acatRevenueSection);
        return Observable.just(acatRevenueSection);
    }

    @Override
    public Observable<List<ACATRevenueSection>> putAll(@NonNull List<ACATRevenueSection> acatRevenueSections) {
        box.put(acatRevenueSections);
        return Observable.just(acatRevenueSections);
    }
}

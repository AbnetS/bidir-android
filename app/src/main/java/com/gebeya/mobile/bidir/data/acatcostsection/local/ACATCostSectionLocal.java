package com.gebeya.mobile.bidir.data.acatcostsection.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/12/2018.
 */

public class ACATCostSectionLocal extends BaseLocalSource implements ACATCostSectionLocalSource {
    private final Box<ACATCostSection> box;

    public ACATCostSectionLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ACATCostSection.class);
    }

    @Override
    public Observable<Data<ACATCostSection>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ACATCostSection>> get(int position) {
        final List<ACATCostSection> acatCostSections = box.getAll();
        return Observable.just(new Data<>(acatCostSections.isEmpty() ? null : acatCostSections.get(position)));
    }

    @Override
    public Observable<Data<ACATCostSection>> get(@NonNull String id) {
        final List<ACATCostSection> acatCostSections = box.find(ACATCostSection_._id, id);
        return Observable.just(new Data<>(acatCostSections.isEmpty() ? null : acatCostSections.get(0)));
    }

    @Override
    public Observable<List<ACATCostSection>> getByParent(@NonNull String parentId) {
        final List<ACATCostSection> acatCostSections = box.find(ACATCostSection_.parentSectionID, parentId);
        return Observable.just(acatCostSections);
    }

    @Override
    public Observable<ACATCostSection> markForUpload(@NonNull ACATCostSection section){
        section.modified = true;
        section.uploaded = false;
        section.updatedAt = new DateTime();

        put(section);
        //box.put(section);

        return Observable.just(section);
    }

    @Override
    public Observable<List<ACATCostSection>> getAll() {
        final List<ACATCostSection> acatCostSections = box.getAll();
        return Observable.just(acatCostSections);
    }

    @Override
    public Observable<ACATCostSection> put(@NonNull ACATCostSection acatCostSection) {
        box.query().equal(ACATCostSection_._id, acatCostSection._id).build().remove();
        box.put(acatCostSection);
        return Observable.just(acatCostSection);
    }

    @Override
    public Observable<List<ACATCostSection>> putAll(@NonNull List<ACATCostSection> acatCostSections) {
        box.removeAll();
        box.put(acatCostSections);
        return Observable.just(acatCostSections);
    }

}

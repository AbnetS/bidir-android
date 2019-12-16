package com.gebeya.mobile.bidir.data.acatsection.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatsection.ACATSection;
import com.gebeya.mobile.bidir.data.acatsection.ACATSection_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implenentation for the {@link ACATSectionLocalSource} interface.
 */

public class ACATSectionLocal extends BaseLocalSource implements ACATSectionLocalSource {

    private final Box<ACATSection> box;

    public ACATSectionLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ACATSection.class);
    }

    @Override
    public Observable<List<ACATSection>> putAll(@NonNull List<ACATSection> acatSections) {
        box.put(acatSections);
        return Observable.just(acatSections);
    }

    @Override
    public Observable<List<ACATSection>> getAll(@NonNull String referenceId, @NonNull String referenceType) {
        final List<ACATSection> acatSections = box.query().equal(ACATSection_.referenceId, referenceId)
                .and()
                .equal(ACATSection_.referenceType, referenceType)
                .build()
                .find();
        return Observable.just(acatSections);
    }
}

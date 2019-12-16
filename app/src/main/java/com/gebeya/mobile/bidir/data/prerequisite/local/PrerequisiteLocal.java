package com.gebeya.mobile.bidir.data.prerequisite.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite_;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link PrerequisiteLocalSource} interface.
 */
public class PrerequisiteLocal extends BaseLocalSource implements PrerequisiteLocalSource {

    private final Box<Prerequisite> box;

    public PrerequisiteLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Prerequisite.class);
    }

    @Override
    public Observable<List<Prerequisite>> putAll(@NonNull List<Prerequisite> prerequisites) {
        final int length = prerequisites.size();
        for (int i = 0; i < length; i++) {
            final Prerequisite prerequisite = prerequisites.get(i);
            box.query().equal(Prerequisite_._id, prerequisite._id).build().remove();
        }

        box.put(prerequisites);
        return Observable.just(prerequisites);
    }

    @Override
    public Observable<List<Prerequisite>> getByParentQuestion(@NonNull String parentQuestionId) {
        final List<Prerequisite> prerequisites = box.query()
                .equal(Prerequisite_.parentQuestionId, parentQuestionId)
                .build().find();
        return Observable.just(prerequisites);
    }

    @Override
    public Observable<List<Prerequisite>> getAll() {
        final List<Prerequisite> prerequisites = box.getAll();
        return Observable.just(prerequisites);
    }
}

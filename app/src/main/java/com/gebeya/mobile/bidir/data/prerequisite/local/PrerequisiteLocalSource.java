package com.gebeya.mobile.bidir.data.prerequisite.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteMany;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface definition for a local source class for
 * {@link com.gebeya.mobile.bidir.data.prerequisite.Prerequisite} objects.
 */
public interface PrerequisiteLocalSource extends WriteMany<Prerequisite> {

    /**
     * Get all the {@link Prerequisite} objects that belong to the given parent question ID.
     *
     * @param parentQuestionId Parent question ID whose prerequisites to find.
     * @return Observable list of the prerequisites for the given parent question ID.
     */
    Observable<List<Prerequisite>> getByParentQuestion(@NonNull String parentQuestionId);

    /**
     * Get a list of all the {@link Prerequisite} objects stored locally.
     *
     * @return Observable list of the prerequisites.
     */
    Observable<List<Prerequisite>> getAll();
}
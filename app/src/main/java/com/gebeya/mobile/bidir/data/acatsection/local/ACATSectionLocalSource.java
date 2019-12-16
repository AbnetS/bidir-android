package com.gebeya.mobile.bidir.data.acatsection.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatsection.ACATSection;
import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteMany;

import java.util.List;

import io.reactivex.Observable;


/**
 * Contract class for a local source for {@link com.gebeya.mobile.bidir.data.acatsection.ACATSection} objects.
 */

public interface ACATSectionLocalSource extends WriteMany<ACATSection> {

    /**
     * Get a list of all the ACAT Sections, under the given reference Id.
     *
     * @param referenceId Reference ID to act as a filter.
     * @param referenceType Reference type to act as a filter.
     * @return Observable list of all the ACAT Sectins that match the reference Id and type.
     */
    Observable<List<ACATSection>> getAll(@NonNull String referenceId, @NonNull String referenceType);
}

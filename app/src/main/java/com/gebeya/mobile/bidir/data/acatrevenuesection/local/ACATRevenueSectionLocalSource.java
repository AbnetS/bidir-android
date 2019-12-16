package com.gebeya.mobile.bidir.data.acatrevenuesection.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by abuti on 5/13/2018.
 */

public interface ACATRevenueSectionLocalSource extends ReadableSource<ACATRevenueSection>, WritableSource<ACATRevenueSection> {

    /**
     * Get a list of all the parent ACAT revenue sections).
     *
     * @return Observable List of all the parent sections.
     */
    Observable<List<ACATRevenueSection>> getAllParentSections();

    /**
     * Get a list of ACAT Revenue Sections based on the given List of IDs.
     *
     * @param ids IDs to find.
     * @return List of ACAT Revenue Sections, if available.
     */
    Observable<List<ACATRevenueSection>> getAllByIds(@Nullable List<String> ids);

    Observable<ACATRevenueSection> markForUpload(@NonNull ACATRevenueSection section);
}

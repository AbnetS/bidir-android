package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;


import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;

import io.reactivex.Observable;


/**
 * Created by Samuel K. on 8/29/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ACATRevenueSectionRemoteSource {

    /**
     * Upload a given ACAT revenue section object.
     *
     * @param section         The section to upload.
     * @param clientACATId    The client ACAT ID to which this section belongs to.
     * @param ClientId        The client ID for this section.
     * @param parentSectionId The parent section ID to which the section belongs to.
     * @return Observable object for the updated {@link ACATRevenueSection} object.
     */
    Observable<ACATRevenueSection> upload(@NonNull ACATRevenueSection section,
                                          @NonNull String clientACATId,
                                          @NonNull String ClientId,
                                          @NonNull String parentSectionId);


    Observable<ACATRevenueSection> updateACATRevenueSection(@NonNull String acatRevenueSectionId,
                                                            @NonNull ACATRevenueSectionRequest request);
}

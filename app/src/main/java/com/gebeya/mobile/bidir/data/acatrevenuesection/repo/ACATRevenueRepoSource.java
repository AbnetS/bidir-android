package com.gebeya.mobile.bidir.data.acatrevenuesection.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 9/1/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ACATRevenueRepoSource extends
        FetchOne<ACATRevenueSection>,
        FetchMany<ACATRevenueSection> {

    Observable<ACATRevenueSection> upload(@NonNull ACATRevenueSection section,
                      @NonNull String clientACATId,
                      @NonNull String clientId,
                      @NonNull String parentSectionId);

    Observable<ACATRevenueSection> updateACATRevenueSection(@NonNull String acatRevenueSectionId,
                                                            @NonNull ACATRevenueSectionRequest request);
}

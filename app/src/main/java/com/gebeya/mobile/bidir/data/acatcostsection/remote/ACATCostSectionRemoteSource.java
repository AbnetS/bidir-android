package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionRequest;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;

import io.reactivex.Observable;

public interface ACATCostSectionRemoteSource {
    Observable<ACATCostSectionResponse> updateACATCostSection
            (@NonNull String acatCostSectionId, @NonNull ACATCostSectionRequest request) ;
}

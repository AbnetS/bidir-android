package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;

import io.reactivex.Observable;

/**
 * Created by abuti on 8/29/2018.
 */

public interface YieldConsumptionRemoteSource {
    Observable<YieldConsumption> updateYieldConsumption(@NonNull String yieldConsumptionId,
                                                          @NonNull YieldConsumptionRequest request);
}


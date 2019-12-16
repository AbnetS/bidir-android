package com.gebeya.mobile.bidir.data.yieldconsumption.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;

import io.reactivex.Observable;


/**
 * Created by Samuel K. on 9/1/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface YieldConsumptionRepoSource extends
        FetchMany<YieldConsumption>,
        FetchOne<YieldConsumption> {

    Observable<YieldConsumption> update(@NonNull String yieldConsumption,
                                        @NonNull YieldConsumptionRequest request);
}

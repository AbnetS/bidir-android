package com.gebeya.mobile.bidir.data.yieldconsumption.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.YieldConsumptionRemoteSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 9/1/2018.
 * <p>
 * samkura47@gmail.com
 */

public class YieldConsumptionRepo implements YieldConsumptionRepoSource {

    @Inject YieldConsumptionRemoteSource remote;
    @Inject YieldConsumptionLocalSource local;

    public YieldConsumptionRepo() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }
    @Override
    public Observable<YieldConsumption> update(@NonNull String yieldConsumptionId, @NonNull YieldConsumptionRequest request) {
        return remote.updateYieldConsumption(yieldConsumptionId, request)
                .flatMap(response -> local.put(response));
    }

    @Override
    public Observable<List<YieldConsumption>> fetchAll() {
        return null;
    }

    @Override
    public Observable<List<YieldConsumption>> fetchForceAll() {
        return null;
    }

    @Override
    public Observable<YieldConsumption> fetch(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<YieldConsumption> fetchForce(@NonNull String id) {
        return null;
    }
}

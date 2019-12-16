package com.gebeya.mobile.bidir.data.acatrevenuesection.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.ACATRevenueSectionRemoteSource;
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

public class ACATRevenueRepo implements ACATRevenueRepoSource {

    @Inject ACATRevenueSectionRemoteSource remote;
    @Inject ACATRevenueSectionLocalSource local;

    public ACATRevenueRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<List<ACATRevenueSection>> fetchAll() {
        return null;
    }

    @Override
    public Observable<List<ACATRevenueSection>> fetchForceAll() {
        return null;
    }


    @Override
    public Observable<ACATRevenueSection> fetch(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<ACATRevenueSection> fetchForce(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<ACATRevenueSection> upload(@NonNull ACATRevenueSection section,
                              @NonNull String clientACATId,
                              @NonNull String clientId,
                              @NonNull String parentSectionId) {

        return remote.upload(section, clientACATId, clientId, parentSectionId)
                .flatMap(response -> local.put(response));
    }

    @Override
    public Observable<ACATRevenueSection> updateACATRevenueSection(@NonNull String acatRevenueSectionId,
                                                                   @NonNull ACATRevenueSectionRequest request) {
        return remote.updateACATRevenueSection(acatRevenueSectionId, request)
                .flatMap(section -> local.put(section));
    }
}

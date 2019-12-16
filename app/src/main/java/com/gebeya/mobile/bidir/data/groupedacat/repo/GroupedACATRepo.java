package com.gebeya.mobile.bidir.data.groupedacat.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocal;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepository;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.groupedacat.local.GroupedACATLocalSource;
import com.gebeya.mobile.bidir.data.groupedacat.remote.GroupedACATRemoteSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class GroupedACATRepo implements GroupedACATRepoSource {

    @Inject ACATApplicationLocal acatApplicationLocal;

    @Inject GroupedACATLocalSource local;
    @Inject GroupedACATRemoteSource remote;
    @Inject ACATApplicationRepository acatRepo;

    public GroupedACATRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<GroupedACAT> getById(@NonNull String groupId) {
        return remote.download(groupId)
                .flatMap(response -> {
//                    updateGroupACATId(response);
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedACAT> submitGroup(@NonNull String groupId) {
        return remote.submitGroupACAT(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedACAT> approveGroup(@NonNull String groupId) {
        return remote.approveGroupACAT(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedACAT> updateStatus(@NonNull String groupId) {
        return remote.updateStatus(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedACAT> create(@NonNull String groupId) {
        return remote.create(groupId)
                .flatMap(response -> local.put(response));
    }

    @Override
    public Completable initializeMember(@NonNull String groupId, @NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs) {
        return remote.initializeMemberACAT(groupId, client, loanProduct, cropACATs)
                .flatMapCompletable(response -> {
                    acatRepo.saveACATCostComponentsLocally(response);
                    acatRepo.saveACATRevenueComponentsLocally(response);
                    acatRepo.saveACATApplicationSync(response);
                    return Completable.complete();
                });
    }

    @Override
    public int size() {
        return local.size();
    }

    @Override
    public Observable<List<GroupedACAT>> fetchAll() {
        return local.getAll()
                .flatMap(screenings ->
                        screenings.isEmpty() ? fetchForceAll() : Observable.just(screenings));
    }

    @Override
    public Observable<List<GroupedACAT>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
//                    updateGroupACATIds(responses);
                    local.putAll(responses);
                    return Observable.just(responses);
                });
    }

    @Override
    public Observable<GroupedACAT> fetch(@NonNull String id) {
        return local.getByGroupId(id).flatMap(data -> {
            if (data.empty()) {
                return fetchForce(id);
            } else {
                return Observable.just(data.get());
            }
        });
    }

    @Override
    public Observable<GroupedACAT> fetchForce(@NonNull String id) {
        return getById(id);
    }

    private void updateGroupACATIds(List<GroupedACAT> groupedACATS) {
        final int length = groupedACATS.size();
        for (int i = 0; i < length; i++) {
            final GroupedACAT groupedACAT = groupedACATS.get(i);
            final List<ACATApplication> acatApplications = groupedACAT.acatApplications;
            acatApplicationLocal.updateWithLocalIds(acatApplications);
            updateACATApplicationGroupTarget(acatApplications, groupedACAT);
        }
    }

    private void updateGroupACATId(GroupedACAT groupedACAT) {
        final List<ACATApplication> acatApplications = groupedACAT.acatApplications;
        acatApplicationLocal.updateWithLocalIds(acatApplications);
        updateACATApplicationGroupTarget(acatApplications, groupedACAT);
    }

    private void updateACATApplicationGroupTarget(List<ACATApplication> acatApplications, GroupedACAT groupedACAT) {
        for (ACATApplication acatApplication : acatApplications) {
            acatApplication.groupedACAT.setTarget(groupedACAT);
        }
    }

}

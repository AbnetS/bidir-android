package com.gebeya.mobile.bidir.data.groups;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface GroupRepoSource extends
        FetchMany<Group>,
        FetchOne<Group>,
        ReadSize{

    /**
     * Register a group application
     * @param group Group info to create with.
     * @return Completable for the operation.
     */
    Observable<Group> register(@NonNull Group group, @NonNull String branchId);

    /**
     * Update a given group with a given data.
     * @param group
     * @return
     */
    Completable update(@NonNull Group group);

    /**
     * Update members of a given group.
     * @param group
     * @param clientIds
     * @return
     */
    Completable updateMembers(@NonNull Group group, @NonNull List<String> clientIds);

    /**
     * Update the leader of a given group.
     * @param group
     * @param clientId
     * @return
     */
    Completable updateLeader(@NonNull Group group, @NonNull String clientId);

    /**
     * Update the status of a group by looking at member client statuses.
     * @param group
     * @return
     */
    Completable updateStatus(@NonNull Group group);

    /**
     * Update the status of the group provided the specific status.
     * @param group
     * @param status
     * @return
     */
    Completable updateGroupStatus(@NonNull Group group, @NonNull String status);

}

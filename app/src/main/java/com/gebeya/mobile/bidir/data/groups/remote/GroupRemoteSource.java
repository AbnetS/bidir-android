package com.gebeya.mobile.bidir.data.groups.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groups.Group;

import java.util.List;

import io.reactivex.Observable;


/**
 * Interface contract definition for the grouped clients remote source.
 */
public interface GroupRemoteSource {

    /**
     * Register a group
     *
     * @param group group registration info. like group name, total members and total loan amount;
     * @return Registered group.
     */
    Observable<Group> register(@NonNull Group group, @NonNull String branchId);

    /**
     * Update a given group.
     * @param group new data to save to the API.
     * @return Updated group data.
     */
    Observable<Group> update(@NonNull Group group);

    /**
     * Update the members of a given group.
     * @param group Group with which members are to be added
     * @param clientIds Clients that are ready to be added to the given group.
     * @return
     */
    Observable<Group> updateMembers(@NonNull Group group, @NonNull List<String> clientIds);

    /**
     * Update the leader of a given group.
     * @param group
     * @return
     */
    Observable<Group> updateLeader(@NonNull Group group, @NonNull String clientId);

    /**
     * This will update the group status as Loan granted / Loan paid.
     * @param group
     * @return
     */
    Observable<Group> updateStatus(@NonNull Group group);

    /**
     * This will update given a status.
     * @param group
     * @param status Loan Paid / Loan Granted.
     * @return
     */
    Observable<Group> updateGroupStatus(@NonNull Group group, @NonNull String status);
    /**
     * Retrieve list of all grouped clients.
     * @return List of all the grouped clients with the group info.
     */
    Observable<List<Group>> getAll();

    /**
     * Get a single group from the API.
     * @param groupId Id used to retrieve the specific info.
     * @return Single group info.
     */
    Observable<Group> getById(@NonNull String groupId);
}

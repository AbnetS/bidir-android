package com.gebeya.mobile.bidir.data.groups.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Implementation for the {@link GroupRemoteSource} interface contract.
 */
public class GroupRemote extends BaseRemoteSource<GroupService> implements GroupRemoteSource{

    @Inject GroupParser groupParser;

    public GroupRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.GROUPS, GroupService.class);
    }

    @Override
    public Observable<Group> register(@NonNull Group group, @NonNull String branchId) {
        final JsonObject request = new JsonObject();
        request.addProperty("name", group.groupName);
        request.addProperty("no_of_members", group.membersCount);
        request.addProperty("total_amount", group.totalLoanAmount);
        request.addProperty("branch", branchId);

        return build()
                .call(service.register(request))
                .map(groupParser::parse);
    }

    @Override
    public Observable<Group> update(@NonNull Group group) {
        return null;
    }

    @Override
    public Observable<Group> updateMembers(@NonNull Group group, @NonNull List<String> clientIds) {
        final JsonObject request = new JsonObject();
        request.add("members", toJson(clientIds));

        return build()
                .call(service.updateMembers(group._id, request))
                .map(groupParser::parse);
    }

    @Override
    public Observable<Group> updateLeader(@NonNull Group group, @NonNull String clientId) {
        final JsonObject request = new JsonObject();
        request.addProperty("leader", clientId);

        return build()
                .call(service.updateLeader(group._id, request))
                .map(groupParser::parse);
    }

    @Override
    public Observable<Group> updateStatus(@NonNull Group group) {
        return build()
                .call(service.updateStatus(group._id))
                .map(groupParser::parse);
    }

    @Override
    public Observable<Group> updateGroupStatus(@NonNull Group group, @NonNull String status) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", status);

        return build()
                .call(service.updateGroupStatus(group._id, request))
                .map(groupParser::parse);
    }

    @Override
    public Observable<List<Group>> getAll() {
        return build()
                .call(service.getAll())
                .map(object -> {
                    final List<Group> groups = new ArrayList<>();
                    final JsonArray array = object.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        Group group = groupParser.parse(array.get(i).getAsJsonObject());
                        if (group != null) {
                            groups.add(group);
                        }
                    }
                    return groups;
                });
    }

    @Override
    public Observable<Group> getById(@NonNull String groupId) {
        return build()
                .call(service.getOne(groupId))
                .map(object -> groupParser.parse(object));
    }

    private JsonArray toJson(@NonNull List<String> items) {
        final JsonArray array = new JsonArray();
        for(int i = 0; i < items.size(); i++) {
            array.add(items.get(i));
        }

        return array;
    }
}

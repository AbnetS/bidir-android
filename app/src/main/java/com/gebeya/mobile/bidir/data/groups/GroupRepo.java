package com.gebeya.mobile.bidir.data.groups;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.groups.remote.GroupRemoteSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class GroupRepo implements GroupRepoSource{

    @Inject ClientLocalSource clientLocal;

    @Inject GroupLocalSource local;
    @Inject GroupRemoteSource remote;

    public GroupRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }
    
    @Override
    public Observable<Group> register(@NonNull Group group, @NonNull String branchId) {
        return remote.register(group, branchId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Completable update(@NonNull Group group) {
        return null;
    }

    @Override
    public Completable updateMembers(@NonNull Group group, @NonNull List<String> clientIds) {
        return remote.updateMembers(group, clientIds)
                .flatMapCompletable(response -> {
                    local.put(response);
                    return Completable.complete();
                });
    }

    @Override
    public Completable updateLeader(@NonNull Group group, @NonNull String clientId) {
        return remote.updateLeader(group, clientId)
                .flatMapCompletable(response -> {
                    local.put(response);
                    return Completable.complete();
                });
    }

    @Override
    public Completable updateStatus(@NonNull Group group) {
        return remote.updateStatus(group)
                .flatMapCompletable(response -> {
                    local.put(response);
                    return Completable.complete();
                });
    }

    @Override
    public Completable updateGroupStatus(@NonNull Group group, @NonNull String status) {
        return remote.updateGroupStatus(group, status)
                .flatMapCompletable(response -> {
                    local.put(response);
                    return Completable.complete();
                });
    }

    @Override
    public int size() {
        return local.size();
    }

    @Override
    public Observable<List<Group>> fetchAll() {
        return local.getAll()
                .flatMap(groups ->
                        groups.isEmpty() ? fetchForceAll() : Observable.just(groups));
    }

    @Override
    public Observable<List<Group>> fetchForceAll() {
        return remote.getAll()
                .flatMap(groups -> {

                    updateGroupClientIds(groups);
                    local.putAll(groups);
                    return Observable.just(groups);
                });
    }

    private void updateGroupClientIds(List<Group> groups) {
        final int length = groups.size();
        for (int i = 0; i < length; i++) {
            final Group group = groups.get(i);
            final List<Client> clients = group.clients;
            clientLocal.updateWithLocalIds(clients);
            updateClientGroupTarget(clients, group);
        }
    }

    private void updateClientGroupTarget(List<Client> clients, Group group) {
        for (Client client : clients) {
            client.group.setTarget(group);
        }
    }

    private void updateGroupClientId(Group group) {
        final List<Client> clients = group.clients;
        clientLocal.updateWithLocalIds(clients);
        updateClientGroupTarget(clients, group);
    }

    @Override
    public Observable<Group> fetch(@NonNull String id) {
        return local.get(id).flatMap(data -> {
            if (data.empty()) {
                return fetchForce(id);
            } else {
                return Observable.just(data.get());
            }
        });
    }

    @Override
    public Observable<Group> fetchForce(@NonNull String id) {
        return remote.getById(id)
                .flatMap(group -> {
                    updateGroupClientId(group);
                    local.put(group);
                    return Observable.just(group);
                });
    }
}

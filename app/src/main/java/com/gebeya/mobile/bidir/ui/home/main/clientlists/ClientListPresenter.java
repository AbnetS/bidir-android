package com.gebeya.mobile.bidir.ui.home.main.clientlists;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import io.reactivex.Observable;


public class ClientListPresenter implements ClientListContract.Presenter{

    private ClientListContract.View view;
    private List<Client> clients;
    private List<String> selectedIds;

    private Group group;
    private String groupId;

    @Inject ClientLocalSource clientLocal;
    @Inject UserLocalSource userLocal;
    @Inject SchedulersProvider schedulers;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupLocalSource groupLocal;
    @Inject ClientListState state;
    @Inject ComplexScreeningRepositorySource screeningRepo;
    @Inject ClientRepoSource clientRepo;

    public ClientListPresenter(@NonNull String groupId) {
        Tooth.inject(this, Scopes.SCOPE_STATES);

        this.groupId = groupId;
        clients = new ArrayList<>();
        selectedIds = new ArrayList<>();
    }

    @SuppressLint("CheckResult")
    @Override
    public void start() {
        if (state.loading()) {
            view.showUpdateSuccessMessage();
            return;
        }

        if (state.complete()) {
            onUpdateComplete();
            return;
        }

        if (state.error()) {
            onUpdateFailed(state.getError());
            return;
        }

        clientLocal.getClientForGroup()
                .flatMap(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    return groupLocal.get(groupId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groupData -> {
                    group = groupData.get();

                    view.showClients();
                    view.setToolbarTitle();
                    view.updateSelectedClientCount(selectedIds.size());
                    boolean show = clients.isEmpty();

                    view.toggleNoClientsLabel(show);
                });
    }

    @Override
    public void onBindRowView(@NonNull ClientListContract.ClientItemView holder, int position) {
        final Client client = clients.get(position);

        final String name = Utils.formatName(
                client.firstName,
                client.lastName,
                client.surname
        );

        holder.setName(name);
        holder.setImage(client.photoUrl);
        holder.setStatus(client.status);
        holder.setChoice(selectedIds.contains(client._id));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onOKButtonClicked() {
        if (state.loading()) return;

        if (selectedIds.size() == 0)
            return;

        state.setLoading();
        view.shoUpdateProgress();

        groupRepo.updateMembers(group, selectedIds)
                .andThen(groupRepo.fetchForceAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                    state.setComplete();
                    onUpdateMemberComplete();
                }, throwable -> {
                    state.setError(throwable);
                    onUpdateFailed(throwable);
                });
    }


    @Override
    public void onClientSelected(int position) {
        toggleClient(position);
        view.refreshClients();
    }

    private void toggleClient(int position) {
        final Client client = clients.get(position);
        final String id = client._id;
        if (selectedIds.contains(id)) {
            selectedIds.remove(id);
        } else {
            selectedIds.add(id);
        }

        view.updateSelectedClientCount(selectedIds.size());
    }


    @Override
    public int getClientCount() {
        return clients.size();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onUpdateMemberComplete() {
        if (view == null) return;

        state.setLoading();
        Observable.fromIterable(selectedIds)
                .concatMap(Observable::just)
                .subscribe(clientId -> screeningRepo.createScreening(clientId, true)
                        .doOnNext(done -> clientRepo.fetchForceAll())
                        .flatMap(done -> screeningRepo.fetchForceAll())
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.ui())
                        .subscribe(done -> {
                            state.setComplete();
                            onUpdateComplete();
                        }, throwable -> {
                            state.setError(throwable);
                            onUpdateFailed(throwable);
                        }));

    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.hideUpdateProgress();
        view.showUpdateSuccessMessage();

        state.reset();
        view.close();
    }

    @Override
    public void onUpdateFailed(Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = ApiErrors.GROUP_APPLICATION_UPDATE_GENERIC_ERROR;

        view.hideUpdateProgress();
        view.showError(Result.createError(message, error));

        state.reset();
    }

    @Override
    public void attachView(ClientListContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public ClientListContract.View getView() {
        return view;
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }
}

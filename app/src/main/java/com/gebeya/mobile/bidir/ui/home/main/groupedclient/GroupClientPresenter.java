package com.gebeya.mobile.bidir.ui.home.main.groupedclient;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

public class GroupClientPresenter implements GroupedClientContract.Presenter {

    private GroupedClientContract.View view;

    private String groupId;
    private String title;

    private Group group;

    @Inject GroupRepoSource groupRepo;

    @Inject GroupedClientLoadState loadState;
    @Inject GroupedClientUpdateState updateState;

    @Inject ClientRepoSource clientRepo;
    @Inject LoanProposalLocalSource loanProposalLocal;
    @Inject LoanProposalRepoSource loanProposalRepo;

    @Inject ClientLocalSource clientLocal;

    @Inject SchedulersProvider schedulers;

    private LoanProposal loanProposal;
    private Client client;

    public GroupClientPresenter(@NonNull String groupId, @NonNull String title) {
        this.groupId = groupId;
        this.title = title;

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }


    @SuppressLint("CheckResult")
    @Override
    public void start() {
        // Update state restoration
        if (updateState.loading()) {
            view.showUpdateProgress();
            return;
        }
        if (updateState.complete()) {
            onUpdateComplete();
            return;
        }
        if (updateState.error()) {
            onUpdateFailed(updateState.getError());
            return;
        }

        // Loading state restoration
        if (loadState.loading()) {
            view.showLoadingProgress();
            return;
        }
        if (loadState.complete()) {
            onLoadingComplete();
            return;
        }
        if (loadState.error()) {
            onLoadingFailed(loadState.getError());
            return;
        }

        loadState.setLoading();
        groupRepo.fetch(groupId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(group -> {
                            this.group = group;
                            loadState.setComplete();
                            onLoadingComplete();
                            view.showClients();
                            boolean clientCount = group.clients.size() == group.membersCount;
                            view.toggleClientAddButton(clientCount);
                            view.toggleNoClientsLabel(this.group.clients.isEmpty());
                        },
                        throwable -> {
                            loadState.setError(throwable);
                            onLoadingFailed(throwable);
                        });
    }


    @Override
    public void onBindRowView(@NonNull GroupedClientContract.GroupedApplicationItemView holder, int position) {
        final Client client = group.clients.get(position);

        final String name = Utils.formatName(
                client.firstName,
                client.lastName,
                client.surname
        );

        holder.setName(name);
        holder.setImage(client.photoUrl);
        holder.setStatus(client.status);
    }

    @Override
    public void onClientSelected(int position) {
        final Client client = group.clients.get(position);
        view.openClient(client);
    }

    @Override
    public int getClientCount() {
        return group.clients.size();
    }

    @Override
    public void onOptionMenuClicked(@NonNull TextView anchor, int position) {
        final Client client = group.clients.get(position);
        view.openPopUpMenu(anchor, client, group);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onAssignLeaderClicked(@NonNull Client client) {
        if (updateState.loading()) return;

        updateState.setLoading();
        view.showUpdateProgress();

        groupRepo.updateLeader(group, client._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(() -> {
                    updateState.setComplete();
                    onUpdateComplete();
                }, throwable -> {
                    updateState.setError(throwable);
                    onUpdateFailed(throwable);
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void onRecordReturned(@NonNull String loanApprovedAmount, @NonNull String clientId) {
        view.showUpdatingProgress();

        loanProposalLocal.getProposalByClient(clientId)
                .flatMap(loanProposalData -> {
                    loanProposal = loanProposalData.get();
                    loanProposal.loanApproved = Double.parseDouble(loanApprovedAmount);
                    return loanProposalRepo.updateLoanProposal(loanProposal);
                })
                .flatMap(done -> clientLocal.get(clientId))
                .flatMap(clientData -> {
                    client = clientData.get();
                    client.status = ClientParser.API_LOAN_GRANTED;
                    return clientRepo.updateStatus(client);
                })
                .flatMap(clientData -> clientRepo.fetch(clientData._id))
                .flatMapCompletable(done -> groupRepo.updateStatus(group))
                .andThen(groupRepo.fetchForce(groupId))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(ignore -> onUpdateComplete(),
                        throwable -> {
                            throwable.printStackTrace();
                            onUpdateFailed(throwable);
                        });
    }

    @Override
    public void onAddButtonClicked() {
        view.openAddMemberChoiceScreen();
        //view.openAddClientScreen(group);
    }

    @Override
    public void onAddGroupMemberReturned(boolean isNew) {
        if (isNew) {
            view.openAddClientScreen(group);
        } else {
            view.openSelectClientScreen(group);
        }
    }

    @Override
    public void onLoanPaidClicked(@NonNull Client client) {
        view.openConfirmationDialog(client);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSetLoanPaidReturned(@NonNull Client client) {
        view.showUpdatingClientProgress();
        client.status = ClientParser.API_LOAN_PAID;

        clientRepo.updateStatus(client)
                .flatMapCompletable(done -> groupRepo.updateStatus(group))
                .andThen(clientRepo.fetchForce(client._id))
                .flatMap(done -> groupRepo.fetchForce(groupId))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> onUpdateComplete(), this::onUpdateFailed);
    }

    @Override
    public void onLoadingComplete() {
        if (view == null) return;

        view.setTitle(title);
        view.hideUpdateProgress();

        loadState.reset();
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        loadState.reset();
    }

    @Override
    public void onRequestComplete() {
        if (view == null) return;

        view.hideUpdateProgress();
        view.showUpdateSuccessfulMessage();


        updateState.reset();
        loadState.reset();
    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.hideUpdateProgress();
        view.showUpdateSuccessfulMessage();

        updateState.reset();
        loadState.reset();

        view.close();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.GROUP_APPLICATION_UPDATE_GENERIC_ERROR;

        view.hideUpdateProgress();
        view.showError(Result.createError(message, error));

        updateState.reset();
    }

    @Override
    public void attachView(GroupedClientContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }

    @Override
    public GroupedClientContract.View getView() {
        return view;
    }
}

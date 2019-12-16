package com.gebeya.mobile.bidir.ui.home.main.acatapplications;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.SyncCallback;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACADataDownloadState;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATDataDownloadService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATDataDownloadServiceCallback;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATSyncState;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepo;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.groupedacat.local.GroupedACATLocalSource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.impl.util.location.preference.PreferenceHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by Samuel K. on 5/9/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATApplicationsPresenter implements ACATApplicationsContract.Presenter, SyncCallback,
        ACATDataDownloadServiceCallback {

    private static final String title = "A-CAT Applications";

    private ACATApplicationsContract.View view;

    @Inject ClientRepo clientRepo;
    @Inject ClientLocalSource clientLocal;

    @Inject ACATApplicationLocalSource acatApplicationLocal;
    @Inject ACATApplicationSyncLocalSource acatSyncLocal;

    @Inject SchedulersProvider schedulers;
    @Inject ACATSyncState syncState;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject LoanProposalLocalSource loanProposalLocal;
    @Inject PreferenceHelper preference;
    @Inject GroupLocalSource groupLocal;
    @Inject UserLocalSource userLocal;
    @Inject GroupedACATLocalSource groupedACATLocal;

    @Inject ACADataDownloadState state;

    List<Group> groups;
    private LoanProposal loanProposal;

    Client c;

    private final List<Client> clients;
    private final List<ACATApplication> acatApplications;

    public ACATApplicationsPresenter() {
        Tooth.inject(this, Scopes.SCOPE_STATES);
        clients = new ArrayList<>();
        acatApplications = new ArrayList<>();
        groups = new ArrayList<>();
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        if (preference.getIndividual()) {
            loadACATs();
        } else {
            loadGroupedApplications();
        }

        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());

        syncState.addSyncCallback(this);
        loadSyncStatus();

        ACATDataDownloadService.addCallback(this);
        if (state.busy()) {
            view.showDownloadProgress();
        }
    }

    @Override
    public void onDownloadStarted(int totalPages) {
        view.showDownloadProgress();
    }

    @Override
    public void onUpdate(int totalProgress, int totalPages) {

    }

    @Override
    public void onDownloadStopped() {
        view.hideDownloadProgress();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onBindRowView(@NonNull ACATApplicationsContract.ACATApplicationItemView view, int position) {
        final Client client = clients.get(position);

        final String name = Utils.formatName(
                client.firstName,
                client.lastName,
                client.surname
        );
        view.setName(name);
        view.setImage(client.photoUrl);

        acatApplicationLocal.getACATByClient(client._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (!data.empty() && !client.status.equalsIgnoreCase(ClientParser.LOAN_APPLICATION_ACCEPTED)){
                        final ACATApplication application = data.get();
                        view.setStatus(application.status);
//                        view.toggleCreatedIndicator(application.pendingCreation);
                    } else {
                        view.setStatus(client.status);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onACATApplicationSelected(int position) {
        final Client client = clients.get(position);
        acatApplicationLocal.getACATByClient(client._id)
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(data -> {
                        if (client.status.equals(ClientParser.LOAN_APPLICATION_ACCEPTED) || data.empty()) {
                            view.openACATApplicationInitializer(client._id);
                        } else {
                            if (data.get().status.equals(ACATApplicationParser.STATUS_LOCAL_SUBMITTED) ||
                                    data.get().status.equalsIgnoreCase(ACATApplicationParser.STATUS_LOCAL_AUTHORIZED)) {
                                view.openCropList(client._id);
                            } else {
                                view.openACATPreliminaryScreen(client._id);
                            }
                        }
                    });
    }

    @Override
    public void attachView(ACATApplicationsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        syncState.removeSyncCallback(this);
        ACATDataDownloadService.removeCallback(this);
        view = null;
    }

    @Override
    public ACATApplicationsContract.View getView() {
        return view;
    }


    @Override
    public int getACATApplicationCount() {
        return clients.size();
    }

    @Override
    public void onOptionMenuClicked(TextView anchor, int position) {
        final Client client = clients.get(position);
        view.openPopUpMenu(anchor, client);
    }
    
    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.hideUpdatingProgress();
        view.showUpdateSuccessfulMessage();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.LOAN_APPROVED_UPDATE_GENERIC_ERROR;

        view.hideUpdatingProgress();
        view.showError(Result.createError(message, error));
    }

    @Override
    public void onIndividualButtonPressed() {
        preference.setIndividual(true);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadACATs();
    }

    @Override
    public void onGroupButtonPressed() {
        preference.setIndividual(false);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadGroupedApplications();
        view.toggleNoACATApplications(false);
    }

    @Override
    public void onGroupSelected(int position) {
        final Group group = groups.get(position);
        view.openGroupedACATScreen(group, title);
    }

    @Override
    public int groupCount() {
        return groups.size();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupBindRowView(ACATApplicationsContract.GroupItemView holder, int position) {
        final Group group = groups.get(position);

        holder.setGroupName(group.groupName);
        holder.setGroupCount(group.membersCount);

        groupedACATLocal.getByGroupId(group._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (data.empty())
                        return;
                    GroupedACAT groupedACAT = data.get();
                    holder.setStatus(groupedACAT.status);
                });

        if (group.leaderId == null) {
            holder.setLeaderName(null);
            return;
        }

        clientLocal.get(group.leaderId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clientData -> {

                    Client leader = clientData.get();

                    final String name = Utils.formatName(
                            leader.firstName,
                            leader.lastName,
                            leader.surname
                    );

                    holder.setLeaderName(name);
                    holder.setImage(leader.photoUrl);

                });
    }

    @Override
    public void onACATsFilterClicked() {
        if (preference.getIndividual()) {
            view.openIndividualFilterMenu();
        } else {
            view.openGroupedFilterMenu();
        }

    }

    @SuppressLint("CheckResult")
    @Override
    public void onSyncStarted() {
        Completable.complete()
                .observeOn(schedulers.ui())
                .subscribe(() -> view.showSyncInProgress());
    }

    @Override
    public void onSyncStopped() {
        loadSyncStatus();
        loadACATs();
    }

    @Override
    public void onSyncPressed() {
        if (view == null) return;
        view.startSyncService();
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadACATs() {
        clientLocal.getClientWithACAT()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
//                    if (view == null) return;
                    clients.clear();
                    clients.addAll(list);
                    view.showACATApplications();
                    view.toggleNoACATApplications(clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadNewACATs() {
        clientLocal.getNewACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    view.showACATApplications();
                    view.toggleNoACATApplications(this.clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInprogressACATs() {
        clientLocal.getInprogressACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    view.showACATApplications();
                    view.toggleNoACATApplications(this.clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadSubmittedACATs() {
        clientLocal.getSubmittedACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    view.showACATApplications();
                    view.toggleNoACATApplications(this.clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadResubmittedACATs() {
        clientLocal.getResubmittedACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    view.showACATApplications();
                    view.toggleNoACATApplications(this.clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadApprovedACATs() {
        clientLocal.getApprovedACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    view.showACATApplications();
                    view.toggleNoACATApplications(this.clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadDeclinedForReviewACATs() {
        clientLocal.getDeclinedACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);
                    view.showACATApplications();
                    view.toggleNoACATApplications(this.clients.isEmpty());
                    view.toggleNoGroupACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedApplications() {
        userLocal.first()
                .flatMap(data -> groupLocal.getAllACATs())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedNewACATs() {
        groupLocal.getGroupedNewACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedInprogressACATs() {
        groupLocal.getGroupedInprogressACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedSubmittedACATs() {
        groupLocal.getGroupedSubmittedACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedResubmittedACATs() {
        groupLocal.getGroupedResubmittedACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedApprovedACATs() {
        groupLocal.getGroupedAuthorizedACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedDeclinedForReviewACATs() {
        groupLocal.getGroupedInReviewACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupACATApplications(this.groups.isEmpty());
                    view.toggleNoACATApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    private void loadSyncStatus() {
        acatSyncLocal.hasUnSyncedData()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(hasUnSyncedData -> {
                    if (syncState.busy()) {
                        view.showSyncInProgress();
                    } else {
                        view.showSyncIdle(hasUnSyncedData);
                    }
                });
    }
}

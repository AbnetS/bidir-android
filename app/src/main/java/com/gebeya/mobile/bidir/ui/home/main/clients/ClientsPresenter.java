package com.gebeya.mobile.bidir.ui.home.main.clients;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.SyncCallback;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.client.ClientSyncState;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientComparator;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATParser;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepo;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.groups.local.GroupComparator;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalParser;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.impl.util.location.preference.PreferenceHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Clients presenter implementation
 */
public class ClientsPresenter implements ClientsContract.Presenter, SyncCallback {

    private ClientsContract.View view;


    private static final String title = "Clients";

    @Inject ClientLocalSource clientLocal;
    @Inject ClientRepoSource clientRepo;
    @Inject ComplexScreeningRepositorySource screeningRepo;
    @Inject UserLocalSource userLocal;
    @Inject PermissionLocalSource permissionLocal;

    @Inject ClientSyncState syncState;
    @Inject SchedulersProvider schedulers;
    @Inject ClientState creatingState;
    @Inject ACATApplicationSyncLocalSource acatSyncLocal;
    @Inject ACATApplicationLocalSource acatLocal;
    @Inject ACATApplicationRepositorySource acatRepo;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject CropACATRepoSource cropACATRepo;
    @Inject LoanProposalLocalSource loanProposalLocal;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject PreferenceHelper preference;
    @Inject GroupLocalSource groupLocal;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupedComplexScreeningRepo groupedScreeningRepo;

    private ACATApplication acatApplication;
    private LoanProposal loanProposal;
    private Client client;

    private List<Client> clients;
    private List<Group> groups;

    public ClientsPresenter() {
        Tooth.inject(this, Scopes.SCOPE_STATES);
        clients = new ArrayList<>();
        groups = new ArrayList<>();
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        if (preference.getIndividual()) {
            loadIndividualClients();
        } else {
            loadGroupedClients();
        }

        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());

        permissionLocal
                .hasPermission(PermissionHelper.ENTITY_CLIENT, PermissionHelper.OPERATION_CREATE)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(view::toggleAddClientsButton);

        syncState.addSyncCallback(this);
        loadSyncStatus();
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadIndividualClients() {
        userLocal.first()
                .flatMap(data -> clientLocal.getAll(data.get()._id))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedClients() {
        userLocal.first()
                .flatMap(data -> groupLocal.getAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    this.groups.addAll(groups);

                    view.showGrouped();
                    view.toggleNoGroupsLabel(groups.isEmpty());
                    view.toggleNoClientsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    private void loadSyncStatus() {
        clientLocal.hasUnSyncedData()
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

    @Override
    public void onSyncPressed() {
        if (view == null) return;
        view.startSyncService();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onSyncStarted() {
        Completable.complete()
                .observeOn(schedulers.ui())
                .subscribe(() -> view.showSyncInProgress());
    }

    @Override
    public void onSyncStopped() {
        loadSyncStatus();
        loadIndividualClients();
    }

    @Override
    public void onClientSelected(int position) {
        final Client client = clients.get(position);
        view.openClient(client);
    }

    @Override
    public void onGroupSelected(int position) {
        final Group group = groups.get(position);
        view.openGroupedApplicationScreen(group, title);
    }

    @Override
    public void onAddButtonClicked() {
        if (preference.getIndividual()) {
            view.openAddClientScreen();
        } else {
            view.openAddGroupScreen();
        }
    }

    @Override
    public void onBindRowView(ClientsContract.ClientItemView holder, int position) {
        final Client client = clients.get(position);

        final String name = Utils.formatName(
                client.firstName,
                client.lastName,
                client.surname
        );

        holder.setName(name);
        holder.setImage(client.photoUrl);
        holder.setStatus(client.status);
        holder.toggleCreatedIndicator(client.pendingCreation);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupBindRowView(ClientsContract.GroupItemView holder, int position) {
        final Group group = groups.get(position);

        holder.setGroupName(group.groupName);
        holder.setStatus(group.groupStatus);
        holder.setGroupCount(group.membersCount);

        if (group.leaderId == null) {
            holder.setLeaderName(null);
            return;
        }

        clientLocal.get(group.leaderId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clientData -> {
                        if (clientData.empty()) return;
                        Client leader = clientData.get();

                    final String name = Utils.formatName(
                            leader.firstName,
                            leader.lastName,
                            leader.surname
                    );

                    holder.setLeaderName(name);
                    holder.setImage(leader.photoUrl);

                });

        // TODO: Add pending creation.
    }

    @Override
    public int getClientCount() {
        return clients.size();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreateReturned(@NonNull String groupCode, int memberCount, double loanAmount) {
        final Group group = new Group();

        group.groupName = groupCode;
        group.membersCount = memberCount;
        group.totalLoanAmount = loanAmount;

        view.showGroupLoadingProgress();

        userLocal.first()
                .flatMap(userData -> {
                    String branchId = userData.get().branchId;
                    return groupRepo.register(group, branchId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(this::onCreateComplete,
                        throwable -> {
                            onCreateFailed(throwable);
                            throwable.printStackTrace();
                        });
    }

    @Override
    public void onLoanPaidClicked(@NonNull Client client) {
        view.openConfirmationDialog(client, null);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSetLoanPaidReturned(@NonNull Client client) {
        view.showUpdatingClientProgress();
        client.status = ClientParser.API_LOAN_PAID;

        clientRepo.updateStatus(client)
                .flatMap(done -> clientRepo.fetchForceAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients = clients;
                    Collections.sort(clients, new ClientComparator());
                    view.refreshClients();
                    onUpdateComplete();
                }, this::onUpdateFailed);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupNewLoanCycleClicked(@NonNull String groupId, @NonNull String loanAmount) {
        if (creatingState.loading()) return;

        creatingState.setLoading();
        view.showLoadingProgress();

        groupedScreeningRepo.createNewLoanCycle(groupId, loanAmount)
                .flatMap(done -> clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForce(groupId))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(group -> {
                    creatingState.setComplete();
                    acatSyncLocal.removeAll(group.membersId);
                    loadGroupedClients();
                    onLoadingComplete();
                }, throwable -> {
                    creatingState.setError(throwable);
                    onLoadingFailed(throwable);
                    throwable.printStackTrace();
                });
    }

    @Override
    public void onGroupLoanPaidClicked(@NonNull Group group) {
        view.openConfirmationDialog(null, group);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupSetLoanPaidReturned(@NonNull Group group) {
        view.showUpdatingGroupProgress();

        groupRepo.updateGroupStatus(group, GroupParser.API_LOAN_PAID)
                .andThen(clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForceAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groupList -> {
                    this.groups.clear();
                    this.groups = groupList;
                    Collections.sort(groups, new GroupComparator());
                    view.refreshGrouped();
                    onUpdateComplete();
                }, this::onGroupUpdateFailed);
    }


    @Override
    public void onCreateComplete(@NonNull Group groupResponse) {
        if (view == null) return;

        groups.add(groupResponse);
        Collections.sort(groups, new GroupComparator());
        view.refreshGrouped();
        view.toggleNoGroupsLabel(groups.isEmpty());

        view.hideLoadingProgress();
        view.showCompleteMessage();
    }

    @Override
    public void onCreateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.GROUP_CREATION_GENERIC_ERROR;
        view.hideLoadingProgress();
        view.showError(Result.createError(message, error));
    }

    @Override
    public void onOptionMenuClicked(TextView anchor, int position) {
        final Client client = clients.get(position);
        view.openPopUpMenu(anchor, client);
    }

    @Override
    public void onClientsFilterClicked() {
        if (preference.getIndividual()) {
            view.openIndividualFilterMenu();
        } else {
            view.openGroupedFilterMenu();
        }
    }

    @Override
    public void onGroupOptionMenuClicked(@NonNull TextView anchor, int position) {
        final Group group = groups.get(position);
        view.openGroupPopUpMenu(anchor, group);
    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.hideLoadingProgress();
        view.showStatusChangedMessage();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.CLIENT_UDPATE_GENERIC_MESSAGE;

        view.hideLoadingProgress();
        view.showError(Result.create(message, error));
    }

    @Override
    public void onGroupUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.GROUP_APPLICATION_UPDATE_GENERIC_ERROR;

        view.hideLoadingProgress();
        view.showError(Result.create(message, error));
    }

    @Override
    public void onLoadingComplete() {
        if (view == null) return;
//        view.refreshClients();
        view.hideLoadingProgress();
        view.showTaskSuccessfulMessage();
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.SCREENING_CREATION_GENERIC_ERROR;

        view.hideLoadingProgress();
        view.showError(Result.createError(message, error));
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
                .flatMap(clientData -> clientRepo.fetchForceAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                            this.clients.clear();
                            this.clients = clients;
                            Collections.sort(clients, new ClientComparator());
                            view.refreshClients();
                            onUpdateComplete();

                        },
                        throwable -> {
                            throwable.printStackTrace();
                            onUpdateFailed(throwable);
                        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onNewLoanCycleClicked(@NonNull Client client) {
        if (creatingState.loading()) return;

        creatingState.setLoading();
        view.showLoadingProgress();

        acatLocal.getLoanAuthorizedClient(client._id)
            .flatMap(acatApplicationData -> {
                acatApplication = acatApplicationData.get();
                return acatRepo.changeClientACATStatus(acatApplication, ACATApplicationParser.STATUS_API_LOAN_PAID);
            })
            .flatMap(acat -> loanProposalLocal.getProposalByACAT(acat._id))
            .flatMap(loanProposalData -> {
                final LoanProposal loanProposal = loanProposalData.get();
                return loanProposalRepo.changeApiStatus(loanProposal, LoanProposalParser.STATUS_API_LOAN_PAID);
            })
            .flatMap(done -> cropACATLocal.getCropACATByApp(acatApplication._id))
            .flatMap(cropACATS -> Observable.fromIterable(cropACATS)
                    .concatMap(Observable::just))
            .flatMap(cropACAT -> cropACATRepo.changeCropACATStatus(cropACAT._id, acatApplication._id, CropACATParser.STATUS_API_LOAN_PAID))
            .flatMap(done -> screeningRepo.createScreening(client._id, false))
            .flatMap(done -> clientRepo.fetchForce(client._id))
            .subscribeOn(schedulers.background())
            .observeOn(schedulers.ui())
            .subscribe(done -> {
                creatingState.setComplete();
                acatSyncLocal.remove(client._id);
                loadIndividualClients();
                view.startScreeningSyncService();
                onLoadingComplete();
            }, throwable -> {
                creatingState.setError(throwable);
                onLoadingFailed(throwable);
                throwable.printStackTrace();
            });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadNewClients() {
        clientLocal.getNewClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadScreeningClients() {
        clientLocal.getScreeningClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadLoanClients() {
        clientLocal.getLoanClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadACATClients() {
        clientLocal.getACATClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGrantedClients() {
        clientLocal.getLoanGrantedClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadPaidClients() {
        clientLocal.getLoanPaidClients()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clients -> {
                    this.clients.clear();
                    this.clients.addAll(clients);

                    view.showClients();
                    boolean show = clients.isEmpty();
                    view.toggleNoClientsLabel(show);
                    view.toggleNoGroupsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedNewClients() {
        groupLocal.getAllNewGroups()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    this.groups.addAll(groups);

                    view.showGrouped();
                    view.toggleNoGroupsLabel(groups.isEmpty());
                    view.toggleNoClientsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedScreeningClients() {
        groupLocal.getAllScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    this.groups.addAll(groups);

                    view.showGrouped();
                    view.toggleNoGroupsLabel(groups.isEmpty());
                    view.toggleNoClientsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedLoanClients() {
        groupLocal.getAllLoanApplications()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    this.groups.addAll(groups);

                    view.showGrouped();
                    view.toggleNoGroupsLabel(groups.isEmpty());
                    view.toggleNoClientsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedACATClients() {
        groupLocal.getAllACATs()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    this.groups.addAll(groups);

                    view.showGrouped();
                    view.toggleNoGroupsLabel(groups.isEmpty());
                    view.toggleNoClientsLabel(false);
                });
    }

    @Override
    public void onIndividualButtonPressed() {
        preference.setIndividual(true);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadIndividualClients();
    }

    @Override
    public void onGroupButtonPressed() {
        preference.setIndividual(false);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadGroupedClients();
    }

    @Override
    public void attachView(ClientsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        syncState.removeSyncCallback(this);
        view = null;
    }

    @Override
    public ClientsContract.View getView() {
        return view;
    }
}
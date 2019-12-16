package com.gebeya.mobile.bidir.ui.home.main.groupedacat;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientComparator;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.groupedacat.local.GroupedACATLocalSource;
import com.gebeya.mobile.bidir.data.groupedacat.remote.GroupedACATParser;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepoSource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class GroupedACATPresenter implements GroupedACATContract.Presenter {

    private GroupedACATContract.View view;

    private String groupId;
    private String title;

    private Group group;
    private GroupedACAT groupedACAT;

    @Inject SchedulersProvider schedulers;
    @Inject GroupedACATUpdateState updateState;
    @Inject GroupedACATLoadState loadState;
    @Inject GroupRepoSource groupRepo;
    @Inject ClientLocalSource clientLocal;
    @Inject GroupedACATRepoSource groupedACATRepo;
    @Inject GroupedACATLocalSource groupedACATLocal;
    @Inject ACATApplicationRepositorySource acatRepo;
    @Inject ACATApplicationLocalSource acatApplicationLocal;

    @Inject ClientRepoSource clientRepo;
    @Inject PermissionLocalSource permissionLocal;

    @Inject LoanProposalLocalSource loanProposalLocal;
    @Inject LoanProposalRepoSource loanProposalRepo;

    private LoanProposal loanProposal;
    private Client c;
    private List<Permission> permissions;
    private boolean canAuthorize;

    public GroupedACATPresenter(@NonNull String groupId, @NonNull String title) {
        this.groupId = groupId;
        this.title = title;

        permissions = new ArrayList<>();

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
        view.showLoadingProgress();

//        groupedACATRepo.fetch(groupId)
//                .subscribeOn(schedulers.background())
//                .subscribe(groupedACAT -> {
//                    this.groupedACAT = groupedACAT;
//                    Collections.sort(groupedACAT.acatApplications);
//                    loadState.setComplete();
//                })
        groupRepo.fetch(groupId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(group -> {
                    this.group = group;
                    Collections.sort(group.clients, new ClientComparator());
                    loadState.setComplete();
                    view.showACATs();
                    boolean show = !group.groupStatus.equals(GroupParser.ACAT_AUTHORIZED);
                    view.toggleDoneMenuButton(show);
                    onLoadingComplete();
                }, throwable -> {
                    loadState.setError(throwable);
                    view.hideLoadingProgress();
                    onLoadingFailed(throwable);
                });

        permissionLocal.getByEntity(PermissionHelper.ENTITY_ACAT)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    permissions.clear();
                    permissions.addAll(list);

                    canAuthorize = hasPermission(PermissionHelper.OPERATION_AUTHORIZE);
                });
    }

    private boolean hasPermission(@NonNull String operation) {
        final int length = permissions.size();
        for (int i = 0; i < length; i++) {
            final Permission permission = permissions.get(i);
            if (permission.operation.equals(operation)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindRowView(@NonNull GroupedACATContract.GroupedACATsItemView holder, int position) {
        final Client client = group.clients.get(position);

        acatApplicationLocal.getACATByClient(client._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (client.status.equals(ClientParser.LOAN_APPLICATION_ACCEPTED) || data.empty())
                        holder.setStatus(ClientParser.LOAN_APPLICATION_ACCEPTED);
                    else
                        holder.setStatus(data.get().status);
//                        holder.setStatus(ClientParser.LOAN_APPLICATION_ACCEPTED);
                });
        final String name = Utils.formatName(
                client.firstName,
                client.lastName,
                client.surname
        );
        holder.setName(name);
        holder.setImage(client.photoUrl);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onACATSelected(int position) {
        final Client client = group.clients.get(position);
        acatApplicationLocal.getACATByClient(client._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (client.status.equals(ClientParser.LOAN_APPLICATION_ACCEPTED) || data.empty()) {
                        view.openACATApplicationInitializer(client._id, groupId);
                    } else {
                        if (data.get().status.equals(ACATApplicationParser.STATUS_LOCAL_SUBMITTED) ||
                                data.get().status.equalsIgnoreCase(ACATApplicationParser.STATUS_LOCAL_AUTHORIZED)) {
                            view.openCropList(client._id, groupId);
                        } else {
                            view.openACAT(client._id, groupId);
                        }
                    }
                });
    }

    @Override
    public int getACATCount() {
        return group.clients.size();
    }

    @Override
    public void onDoneClicked() {
        if (updateState.loading()) return;

        updateState.setLoading();
        view.showUpdateProgress();

        if (canAuthorize) {
            approveGroup();
        } else {
            submitGroup();
        }
    }

    @SuppressLint("CheckResult")
    private void submitGroup() {
        groupedACATRepo.submitGroup(groupId)
                .flatMap(done -> clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForceAll())
                .flatMap(done -> groupedACATRepo.fetchForceAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                    updateState.setComplete();
                    onUpdateComplete();
                }, throwable -> {
                    updateState.setError(throwable);
                    onUpdateFailed(throwable);
                });
    }

    @SuppressLint("CheckResult")
    private void approveGroup() {
        groupedACATRepo.approveGroup(groupId)
                .flatMap(done -> clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForceAll())
                .flatMap(done -> groupedACATRepo.fetchForceAll())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groupedACAT -> {
                    updateState.setComplete();
                    onUpdateComplete();
                }, throwable -> {
                    updateState.setError(throwable);
                    onUpdateFailed(throwable);
                });
    }

    @Override
    public void onOptionMenuClicked(@NonNull TextView anchor, int position) {
        final Client client = this.group.clients.get(position);
        view.openPopUpMenu(anchor, client, group);
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
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.GROUP_APPLICATION_LOADING_GENERIC_ERROR;

        view.hideUpdateProgress();
        view.showError(Result.createError(message, error));

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

        loadState.reset();
        updateState.reset();
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }

    @Override
    public void attachView(GroupedACATContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public GroupedACATContract.View getView() {
        return view;
    }
}

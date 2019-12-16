package com.gebeya.mobile.bidir.ui.home.main.groupedscreenings;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningComparator;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.repo.GroupedComplexLoanRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningParser;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepoSource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepo;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
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

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class GroupedScreeningsPresenter implements GroupedScreeningsContract.Presenter {

    private GroupedScreeningsContract.View view;

    private String groupScreeningId;
    private String groupId;
    private String title;

    private Group group;
    private GroupedComplexScreening groupedComplexscreening;
    private ComplexScreening complexScreening;

    @Inject SchedulersProvider schedulers;
    @Inject GroupedScreeningsUpdateState updateState;
    @Inject GroupedScreeningsLoadState loadState;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupedComplexScreeningRepoSource groupedScreeningRepo;
    @Inject ClientRepoSource clientRepo;
    @Inject ComplexScreeningRepositorySource screeningRepo;
    @Inject GroupedComplexLoanRepoSource groupedLoanRepo;
    @Inject ComplexLoanApplicationRepositorySource loanRepo;

    @Inject ComplexScreeningLocalSource screeningLocal;

    @Inject ClientLocalSource clientLocal;
    @Inject PermissionLocalSource permissionLocal;

    private List<Permission> permissions;
    private boolean canAuthorize;

    public GroupedScreeningsPresenter(@NonNull String groupId, @NonNull String title) {
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
        groupedScreeningRepo.fetch(groupId)
                .flatMap(screening -> {
                    groupedComplexscreening = screening;

                    return groupRepo.fetch(groupId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(group -> {
                            this.group = group;
                            loadState.setComplete();
                            view.showScreenings();
                            onLoadingComplete();
                        }, throwable -> {
                            loadState.setError(throwable);
                            view.hideLoadingProgress();
                            onLoadingFailed(throwable);
                        });

        permissionLocal.getByEntity(PermissionHelper.ENTITY_SCREENING)
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
    public void onBindRowView(@NonNull GroupedScreeningsContract.GroupedScreeningsItemView holder, int position) {
        final ComplexScreening complexScreening = groupedComplexscreening.complexScreenings.get(position);
        holder.setStatus(complexScreening.status);

        clientLocal.get(complexScreening.clientId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clientData -> {
                    if (!clientData.empty()) {
                        final Client client = clientData.get();
                        final String name = Utils.formatName(
                                client.firstName,
                                client.lastName,
                                client.surname
                        );
                        holder.setName(name);
                        holder.setImage(client.photoUrl);
                    } else {
                        Utils.e(this, "No client was found locally! Should probably use fetch method instead");
                    }
                });
    }

    @Override
    public void onScreeningSelected(int position) {
        final ComplexScreening screening = groupedComplexscreening.complexScreenings.get(position);
        view.openScreening(screening._id, groupId);
    }

    @Override
    public int getScreeningCount() {
        return groupedComplexscreening.complexScreenings.size();
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
        groupedScreeningRepo.submitGroup(groupId)
                .flatMap(done -> clientRepo.fetchForceAll())
                .flatMap(done -> screeningRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForce(groupId))
                .flatMap(done -> groupedScreeningRepo.fetchForce(groupId))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                            updateState.setComplete();
                            view.hideUpdateProgress();
                            onUpdateComplete();
                        }, throwable -> {
                            updateState.setError(throwable);
                            view.hideUpdateProgress();
                            onUpdateFailed(throwable);
                        });
    }

    @SuppressLint("CheckResult")
    private void approveGroup() {
        groupedScreeningRepo.approveGroup(groupId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groupedComplexScreening -> {
                        if (groupedComplexScreening.status.equalsIgnoreCase(GroupedComplexScreeningParser.STATUS_LOCAL_APPROVED)) {
                            onApproveGroupScreening();
                            return;
                        }
                        updateState.setComplete();
                        onUpdateComplete();
                        }, throwable -> {
                            updateState.setError(throwable);
                            onUpdateFailed(throwable);
                        });
    }

    @SuppressLint("CheckResult")
    private void onApproveGroupScreening() {
        view.hideUpdateProgress();
        view.showCreatingLoanProgress();
        groupedLoanRepo.create(groupId)
                .flatMap(done -> clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForce(groupId))
                .flatMap(done -> groupedLoanRepo.fetchForce(groupId))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                    updateState.setComplete();
                    onLoanCreated();

                }, throwable -> {
                    updateState.setError(throwable);
                    onUpdateFailed(throwable);
                });
    }

    @Override
    public void onLoanCreated() {
        if (view == null) return;

        view.hideUpdateProgress();
        view.showUpdateSuccessfulMessage();

        updateState.reset();
        loadState.reset();

        view.close();
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

        view.hideLoadingProgress();
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

        updateState.reset();
    }


    @Override
    public void attachView(GroupedScreeningsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public GroupedScreeningsContract.View getView() {
        return view;
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }
}

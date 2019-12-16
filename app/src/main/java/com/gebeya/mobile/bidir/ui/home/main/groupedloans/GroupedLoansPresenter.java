package com.gebeya.mobile.bidir.ui.home.main.groupedloans;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanParser;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.repo.GroupedComplexLoanRepoSource;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GroupedLoansPresenter implements GroupedLoansContract.Presenter {

    private GroupedLoansContract.View view;

    private String groupId;
    private String title;

    private GroupedComplexLoan groupedComplexLoan;

    @Inject SchedulersProvider schedulers;
    @Inject GroupedLoansUpdateState updateState;
    @Inject GroupedLoansLoadState loadState;
    @Inject GroupRepoSource groupRepo;
    @Inject ClientLocalSource clientLocal;
    @Inject GroupedComplexLoanRepoSource groupedLoanRepo;
    @Inject ComplexLoanApplicationRepositorySource loanRepo;
    @Inject GroupedACATRepoSource groupACATRepo;

    @Inject ClientRepoSource clientRepo;
    @Inject PermissionLocalSource permissionLocal;

    private List<Permission> permissions;
    private boolean canAuthorize;

    public GroupedLoansPresenter(@NonNull String groupId, @NonNull String title) {
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

        groupedLoanRepo.fetch(groupId) // TODO: Remember to replace this with fetch (after API is fixed)
                .flatMap(loan -> {
                    groupedComplexLoan = loan;
                    return groupRepo.fetch(groupId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
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
    public void onBindRowView(@NonNull GroupedLoansContract.GroupedLoansItemView holder, int position) {
        final ComplexLoanApplication loanApplication = groupedComplexLoan.complexLoans.get(position);
        holder.setStatus(loanApplication.status);

        clientLocal.get(loanApplication.clientId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (!data.empty()) {
                        final Client client = data.get();
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
    public void onLoanSelected(int position) {
        final ComplexLoanApplication loanApplication = groupedComplexLoan.complexLoans.get(position);
        view.openLoan(loanApplication._id, groupId);
    }

    @Override
    public int getLoanCount() {
        return groupedComplexLoan.complexLoans.size();
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
        groupedLoanRepo.submitGroup(groupId)
                .flatMap(done -> clientRepo.fetchForceAll())
//                .flatMap(done -> loanRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForce(groupId))
                .flatMap(done -> groupedLoanRepo.fetchForce(groupId))
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
        groupedLoanRepo.approveGroup(groupId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groupedComplexLoan -> {
                    if (groupedComplexLoan.status.equalsIgnoreCase(GroupedComplexLoanParser.STATUS_LOCAL_ACCEPTED)) {
                        onApproveGroupLoan();
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
    private void onApproveGroupLoan() {
        view.hideLoadingProgress();
        view.showCreatingACATProgress();
        groupACATRepo.create(groupId)
                .flatMap(done -> clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForceAll())
                .flatMap(done -> groupACATRepo.fetchForce(groupId))
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

        updateState.reset();
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }


    @Override
    public void attachView(GroupedLoansContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public GroupedLoansContract.View getView() {
        return view;
    }
}

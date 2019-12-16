package com.gebeya.mobile.bidir.ui.crop_summary;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryPresenter implements CropSummaryContract.Presenter {

    private CropSummaryContract.View view;

    private final String clientId;
    private final List<CropACAT> cropACATList;
    @Nullable private Client client;
    private LoanProposal loanProposal;
    private List<Permission> permissions;

    @Nullable private Task task;

    @Inject ClientLocalSource clientLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject SchedulersProvider schedulers;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject PermissionLocalSource permissionLocal;


    public CropSummaryPresenter(@NonNull String clientId, @Nullable Task task) {
        this.clientId = clientId;
        this.task = task;
        cropACATList = new ArrayList<>();
        permissions = new ArrayList<>();

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }


    @Override
    public void start() {
        if (task != null) {
            view.showLoadingMessage();
            loanProposalRepo.fetchByClient(clientId)
                    .flatMap(loanProposal -> {
                        this.loanProposal = loanProposal;
                        return clientLocal.get(clientId);
                    })
                    .flatMap(data -> {
                        client = data.get();
                        view.setTitle(Utils.formatName(
                                client.firstName,
                                client.surname,
                                client.lastName
                        ));
                        return cropACATLocal.getClientACATCrop(clientId);
                    })
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(cropACATs -> {
                                cropACATList.clear();
                                cropACATList.addAll(cropACATs);
                                view.loadMenu();
                                view.hideLoadingProgress();
                            },
                            Throwable::printStackTrace
                    );
        } else {
            clientLocal.get(clientId)
                    .flatMap(data -> {
                        client = data.get();
                        view.setTitle(Utils.formatName(
                                client.firstName,
                                client.surname,
                                client.lastName
                        ));
                        return cropACATLocal.getClientACATCrop(clientId);
                    })
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(cropACATs -> {
                                cropACATList.clear();
                                cropACATList.addAll(cropACATs);
                                view.loadMenu();
                            },
                            Throwable::printStackTrace
                    );
        }

        permissionLocal.getByEntity(PermissionHelper.ENTITY_SCREENING)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    permissions.clear();
                    permissions.addAll(list);

                    applyPermissions();
                });

    }

    private void applyPermissions() {
        final boolean canAuthorize = hasPermission(PermissionHelper.OPERATION_AUTHORIZE);

        view.toggleApproveButton(canAuthorize);
        view.toggleDeclineButton(canAuthorize);
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

    @Override
    public void attachView(CropSummaryContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public CropSummaryContract.View getView() {
        return view;
    }

    @Override
    public void onBindRowView(@NonNull CropSummaryContract.MenuItemView holder, int position) {
        CropACAT cropACAT = cropACATList.get(position);

        holder.setCropMenuLabel(cropACAT.cropName);
    }

    @Override
    public int getMenuItemCount() {
        return cropACATList.size();
    }

    @Override
    public void onMenuItemSelected(int position) {
        CropACAT cropACAT = cropACATList.get(position);

        view.loadCrops(clientId, cropACAT.cropID);
    }

    @Override
    public void onLoanProposalClicked() {
        view.loadLoanProposal(clientId);
        view.toggleMenuButton(true);
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }

    @Override
    public void onApproveClicked() {
        view.showUpdatingProgress();
        loanProposalRepo.approveLoanProposal(loanProposal)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> view.hideUpdatingProgress());
    }

    @Override
    public void onDeclineClicked() {
        view.openDeclineDialog();
    }

    @Override
    public void onDeclineReturned(@NonNull String remark, boolean isFinal) {
        view.showUpdatingProgress();
        loanProposalRepo.declineForReview(loanProposal)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> view.hideUpdatingProgress());
    }

    @Override
    public void onUpdateComplete() {

    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {

    }


}

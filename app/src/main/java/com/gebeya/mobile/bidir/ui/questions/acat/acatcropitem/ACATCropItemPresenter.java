package com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATParser;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepo;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepoSource;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepoSource;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalParser;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by Samuel K. on 7/11/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATCropItemPresenter implements ACATCropItemContract.Presenter {

    private ACATCropItemContract.View view;

    @Inject ClientLocalSource clientLocal;
    @Inject ClientRepoSource clientRepo;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject CropACATRepoSource cropACATRepo;
    @Inject SchedulersProvider scheduler;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject ACATApplicationRepositorySource acatApplicationRepo;
    @Inject ACATApplicationLocalSource acatLocalSource;
    @Inject PermissionLocalSource permissionLocal;
    @Inject SchedulersProvider schedulers;
    @Inject ACATApplicationSyncLocalSource acatSyncLocal;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupedACATRepoSource groupACATRepo;
    private List<CropACAT> cropACATList;
    private List<Permission> permissions;


    private String clientId;
    private String groupId;
    private Client client;
    private Task task;
    private String clientACATId;
    private boolean isMonitoring;
    private ACATApplication acatApplication;
    @Nullable private LoanProposal loanProposal;


    public ACATCropItemPresenter(@Nullable String clientId, @Nullable Task task, @Nullable String clientACATId, boolean isMonitoring, @Nullable String groupId) {
        this.task = task;
        this.clientId = clientId;
        this.groupId = groupId;
        this.clientACATId = clientACATId;
        cropACATList = new ArrayList<>();
        permissions = new ArrayList<>();
        this.isMonitoring = isMonitoring;

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        if (task != null) {
            view.showLoadingProgress();
            acatApplicationRepo.fetchByACATId(clientACATId)
                    .flatMap(acatApplication -> {
                        this.acatApplication = acatApplication;
                        return loanProposalRepo.fetchByClient(acatApplication.clientID);
                    })
                    .flatMap(loanProposal -> {
                        this.loanProposal = loanProposal;
                        return cropACATLocal.getCropACATByApp(acatApplication._id);

                    })
                    .flatMap(cropACATs -> {
                        cropACATList.clear();
                        cropACATList.addAll(cropACATs);
                        return permissionLocal.getByEntity(PermissionHelper.ENTITY_ACAT_PROCESSOR);
                    })
                    .flatMap(list -> {
                        permissions.clear();
                        permissions.addAll(list);
                        return clientLocal.get(loanProposal.clientId);
                    })
                    .subscribeOn(scheduler.background())
                    .observeOn(scheduler.ui())
                    .subscribe(clientData -> {
                        if (view == null) return;
                        client = clientData.get();
                        view.setTitle(Utils.formatName(
                                client.firstName,
                                client.surname,
                                client.lastName
                        ));
                        view.showACATCropItems();
                        view.setLoanStatus(loanProposal);
                        view.toggleLoanProposal(true);
                        view.toggleOptionMenu(false);
                        view.hideLoadingProgress();

                        applyPermissions();
                    }, this::onLoadingFailed);

        } else {
            acatLocalSource.getACATByClient(clientId)
                    .flatMap(acatApplication -> {
                        this.acatApplication = acatApplication.get();
                        return cropACATLocal.getCropACATByApp(this.acatApplication._id);
                    })
                    .flatMap(cropACATs -> {
                        cropACATList.clear();
                        cropACATList.addAll(cropACATs);
                        return loanProposalRepo.fetchByClient(clientId);
                    })
                    .flatMap(loanProposal -> {
                        this.loanProposal = loanProposal;
                        return permissionLocal.getByEntity(PermissionHelper.ENTITY_ACAT_PROCESSOR);
                    })
                    .flatMap(list -> {
                        permissions.clear();
                        permissions.addAll(list);
                        return clientLocal.get(loanProposal.clientId);
                    })
                    .subscribeOn(scheduler.background())
                    .observeOn(scheduler.ui())
                    .subscribe(clientData -> {
                        if (view == null) return;
                        client = clientData.get();
                        view.setTitle(Utils.formatName(
                                client.firstName,
                                client.surname,
                                client.lastName
                        ));
                        view.showACATCropItems();
                        view.setLoanStatus(loanProposal);
                        applyPermissions();
                        if (isMonitoring) {
                            view.toggleLoanProposal(false);
                        } else {
                            view.toggleLoanProposal(true);
                        }

                    });
        }

    }

    private void applyPermissions() {
        final boolean canAuthorize = hasPermission(PermissionHelper.OPERATION_AUTHORIZE);
//        for (CropACAT cropACAT : cropACATList) {
//            if (cropACAT.status.equalsIgnoreCase(CropACATParser.STATUS_LOCAL_DECLINED_FOR_REVIEW) ||
//                    loanProposal.status.equalsIgnoreCase(LoanProposalParser.STATUS_LOCAL_DECLINED_FOR_REVIEW))
//                view.toggleDeclineButton(canAuthorize);
//        }

        if (isMonitoring || client.status.equalsIgnoreCase(ClientParser.LOAN_GRANTED)) {
            view.toggleApproveButton(false);
            view.toggleApproveButton(false);
        } else {
            view.toggleApproveButton(canAuthorize);
            view.toggleDeclineButton(canAuthorize);
        }


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
    public void attachView(ACATCropItemContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public ACATCropItemContract.View getView() {
        return view;
    }

    @Override
    public void onBindRowView(@NonNull ACATCropItemContract.ACATCropItemView holder, int position) {
        final CropACAT cropACAT = cropACATList.get(position);

        holder.setName(cropACAT.cropName);
        holder.setStatus(cropACAT.status);

//        holder.setStatus(cropACAT.complete ? "Complete" : "In progress");
        // TODO: Set Crop Image here.
    }

    @Override
    public void onACATCropItemSelected(int position) {
        final CropACAT cropACAT = cropACATList.get(position);
        if (task != null) {
            view.openACATSummary(client._id, cropACAT.cropID);
        } else {
            if (cropACAT.status.equalsIgnoreCase(ACATApplicationParser.STATUS_API_SUBMITTED) ||
                    cropACAT.status.equalsIgnoreCase(ACATApplicationParser.STATUS_LOCAL_AUTHORIZED)) {
                if (isMonitoring) {
//                    view.openACATCostEstimation(clientId, cropACAT._id, isMonitoring);
//                } else {
                    view.openACATSummary(clientId, cropACAT._id);
                }

            } else {
                view.openACATCostEstimation(clientId, cropACAT._id, isMonitoring);
            }
        }
    }


    @Override
    public void onOptionMenuClicked(@NonNull View anchor, int position) {
        final CropACAT cropACAT = cropACATList.get(position);
        view.openPopUpMenu(anchor, clientId, cropACAT.cropID);
    }

    @Override
    public void onLoanProposalClicked() {
        view.openLoanProposalSummary(clientId, task);
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
            view.hideLoadingProgress();
            return;
        }

        final String message = ApiErrors.CLIENT_ACAT_LOADING_GENERIC_ERROR;

        view.hideLoadingProgress();
        view.showError(Result.createError(message, error));

    }

    @Override
    public int optionMenuVisibility() {
        if (task != null || isMonitoring) {
            return View.GONE;
        }
        return View.GONE; //todo Was Visible before.
    }

    @SuppressLint("CheckResult")
    @Override
    public void onApproveButtonClicked() {
        if (groupId == null) {
            submitAndApprove();
        } else {
            approveGroupMembers();
        }
    }

    @SuppressLint("CheckResult")
    private void submitAndApprove() {
        view.showUpdatingProgress();
        Observable.fromIterable(cropACATList)
                .concatMap(cropACAT -> Observable.just(cropACAT)
                        .doOnNext(cropACATData -> cropACATRepo.submitCropACAT(cropACATData._id, acatApplication._id)))
                .doOnNext(cropACAT -> loanProposalRepo.submitLoanProposal(loanProposal))
                .doOnNext(cropACAT -> acatApplicationRepo.submitACATApplication(acatApplication))
                .doOnNext(cropACAT -> Observable.fromIterable(cropACATList)
                        .concatMap(cropACATData -> Observable.just(cropACAT)))
                .subscribe(cropACAT -> cropACATRepo.approveCropACAT(cropACAT._id, acatApplication._id)
                        .flatMap(done -> {
                            Observable.just(done);
                            return loanProposalRepo.approveLoanProposal(loanProposal);
                        })
                        .flatMap(done -> {
                            Observable.just(done);
                            return acatApplicationRepo.approveACATApplication(acatApplication);
                        })
                        .flatMap(done ->
                            clientRepo.fetchForce(clientId)
                        )
                        .subscribeOn(scheduler.background())
                        .observeOn(scheduler.ui())
                        .subscribe(client -> {
                            view.showUpdatingSuccessMessage();
                            view.hideLoadingProgress();
                            view.close();
//                            loadCropItem(client);
                        }, this::onUpdateFailed), this::onUpdateFailed);
    }

    @SuppressLint("CheckResult")
    private void approveGroupMembers() {
        view.showUpdatingProgress();
        Observable.fromIterable(cropACATList)
                .concatMap(cropACAT -> Observable.just(cropACAT)
                        .doOnNext(cropACATData -> cropACATRepo.submitCropACAT(cropACATData._id, acatApplication._id)))
                .doOnNext(cropACAT -> loanProposalRepo.submitLoanProposal(loanProposal))
                .doOnNext(cropACAT -> acatApplicationRepo.submitACATApplication(acatApplication))
                .doOnNext(cropACAT -> Observable.fromIterable(cropACATList)
                        .concatMap(cropACATData -> Observable.just(cropACAT)))
                .subscribe(cropACAT -> cropACATRepo.approveCropACAT(cropACAT._id, acatApplication._id)
                        .flatMap(done -> {
                            Observable.just(done);
                            return loanProposalRepo.approveLoanProposal(loanProposal);
                        })
                        .flatMap(done -> {
                            Observable.just(done);
                            return acatApplicationRepo.approveACATApplication(acatApplication);
                        })
                        .flatMap(done -> {
                            Observable.just(done);
                            return groupACATRepo.updateStatus(groupId);
                        })
                        .flatMap(done ->
                            clientRepo.fetchForce(clientId)
                        )
                        .flatMap(client -> {
                            this.client = client;
                            return groupRepo.fetchForce(groupId);
                        })
                        .flatMap(done -> groupACATRepo.fetchForce(groupId))
                        .subscribeOn(scheduler.background())
                        .observeOn(scheduler.ui())
                        .subscribe(done -> {
                            view.showUpdatingSuccessMessage();
                            view.hideLoadingProgress();
                            view.close();
                        }, this::onUpdateFailed), this::onUpdateFailed);
    }

    @SuppressLint("CheckResult")
    private void loadCropItem(Client client) {
        acatLocalSource.getACATByClient(client._id)
                .flatMap(acatApplication -> {
                    this.acatApplication = acatApplication.get();
                    return cropACATLocal.getClientACATCrop(client._id);
                })
                .flatMap(cropACATs -> {
                    cropACATList.clear();
                    cropACATList.addAll(cropACATs);
                    return loanProposalRepo.fetchByClient(client._id);
                })
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(loanProposal -> {
                    view.showACATCropItems();
                    view.setLoanStatus(loanProposal);
                    view.toggleLoanProposal(true);
                });
    }

    private void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.LOAN_APPROVED_UPDATE_GENERIC_ERROR;

        view.hideUpdatingProgress();
        view.showError(Result.createError(message, error));
    }

    @Override
    public void onDeclineButtonClicked() {
        view.openDeclineDialog();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDeclineReturned(@NonNull String remark, boolean isFinal) {
        if (groupId == null) {
            view.showUpdatingProgress();
            Observable.fromIterable(cropACATList)
                    .concatMap(cropACAT -> Observable.just(cropACAT)
                            .doOnNext(cropACATData -> cropACATRepo.submitCropACAT(cropACATData._id, acatApplication._id)))
                    .doOnNext(cropACAT -> loanProposalRepo.submitLoanProposal(loanProposal))
                    .doOnNext(cropACAT -> acatApplicationRepo.submitACATApplication(acatApplication))
                    .doOnNext(cropACAT -> Observable.fromIterable(cropACATList)
                            .concatMap(Observable::just))
                    .subscribe(cropACAT -> cropACATRepo.declineCropACAT(cropACAT._id, acatApplication._id)
                            .flatMap(done -> {
                                Observable.just(done);
                                return loanProposalRepo.declineForReview(loanProposal);
                            })
                            .flatMap(done -> {
                                Observable.just(done);
                                return acatApplicationRepo.declineACATApplication(acatApplication, remark);
                            })
                            .flatMap(done ->
                                clientRepo.fetchForce(clientId)
                            )
                            .subscribeOn(scheduler.background())
                            .observeOn(scheduler.ui())
                            .subscribe(client -> {
                                view.showUpdatingSuccessMessage();
                                view.hideLoadingProgress();
                                loadCropItem(client);
                            }, this::onUpdateFailed), this::onUpdateFailed);
        } else {
            view.showUpdatingProgress();
            Observable.fromIterable(cropACATList)
                    .concatMap(cropACAT -> Observable.just(cropACAT)
                            .doOnNext(cropACATData -> cropACATRepo.submitCropACAT(cropACATData._id, acatApplication._id)))
                    .doOnNext(cropACAT -> loanProposalRepo.submitLoanProposal(loanProposal))
                    .doOnNext(cropACAT -> acatApplicationRepo.submitACATApplication(acatApplication))
                    .doOnNext(cropACAT -> Observable.fromIterable(cropACATList)
                            .concatMap(Observable::just))
                    .subscribe(cropACAT -> cropACATRepo.declineCropACAT(cropACAT._id, acatApplication._id)
                            .flatMap(done -> {
                                Observable.just(done);
                                return loanProposalRepo.declineForReview(loanProposal);
                            })
                            .flatMap(done -> {
                                Observable.just(done);
                                return acatApplicationRepo.declineACATApplication(acatApplication, remark);
                            })
                            .flatMap(done ->
                                    clientRepo.fetchForce(clientId)
                            )
                            .flatMap(client -> {
                                this.client = client;
                                return groupRepo.fetchForce(groupId);
                            })
                            .flatMap(done -> groupACATRepo.fetchForce(groupId))
                            .subscribeOn(scheduler.background())
                            .observeOn(scheduler.ui())
                            .subscribe(done -> {
                                view.showUpdatingSuccessMessage();
                                view.hideLoadingProgress();
                                loadCropItem(client);
                            }, this::onUpdateFailed), this::onUpdateFailed);
        }

    }

    @Override
    public int getACATCropItemCount() {
        return cropACATList.size();
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }
}

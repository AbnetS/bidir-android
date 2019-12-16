package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepoSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;

import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.estimatedyield.ACATEstimatedRevenueActivity;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATLoanProposalFragment extends BaseFragment implements OnMenuButtonClickListener,
        ErrorDialogCallback,
        DeclineDialogCallback {

    public static final int FRAGMENT_COUNT = 2;

    private String[] titles;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Client client;
    private TextView tabLabel;
    private String clientId;
    private String acatId;

    private TextView menuSave;
    private TextView menuSubmit;
    private TextView menuApprove;
    private TextView menuDecline;

    private ACATLoanProposalAdapter adapter;

    @Inject ClientRepoSource clientRepo;
    @Inject ACATLoanProposalLoadState loadState;
    @Inject SchedulersProvider scheduler;
    @Inject LoanProposalLocalSource local;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject ACATApplicationRepositorySource acatApplicationRepo;
    @Inject CropACATRepoSource cropACATRepo;
    @Inject ACATApplicationLocalSource acatAppLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject PermissionLocalSource permissionLocal;
    @Inject ACATApplicationSyncLocalSource acatSyncLocal;
    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    private ErrorDialog errorDialog;
    private WaitingDialog waitingDialog;
    private DeclineDialog declineDialog;

    private Button previousButton;

    private CashFlow cashFlow;
    private LoanProposal loanProposal;
    private ACATApplication acatApplication;
    private ACATApplication acatData;
    private ACATApplicationRequest request;
    private List<CropACAT> cropACATList;
    private List<Permission> permissions;
    private CropACAT cropACAT;


    public static ACATLoanProposalFragment newInstance() {
        return new ACATLoanProposalFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = getResources().getStringArray(R.array.acat_loan_proposal_titles);
        clientId = getArguments().getString(ACATLoanProposalActivity.CLIENT_ID);
        acatId = getArguments().getString(ACATLoanProposalActivity.ACAT_ID);
        adapter = new ACATLoanProposalAdapter(this, getChildFragmentManager(), titles, clientId, acatId);

        request = new ACATApplicationRequest();
        permissions = new ArrayList<>();
        cropACATList = new ArrayList<>();

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_loan_proposal_layout, container, false);

        tabLayout = getView(R.id.loanProposalFragmentTabLayout);

        toolbar = getView(R.id.acat_loan_proposal_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        viewPager = getView(R.id.loanProposalFragmentViewPager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        previousButton = getBt(R.id.loan_proposal_previous_button);
        previousButton.setOnClickListener(view -> getActivity().onBackPressed());

        menuSave = getTv(R.id.loanAssessmentMenuSave);
        menuSave.setOnClickListener(v -> onSaveClicked());

        menuSubmit = getTv(R.id.loanAssessmentMenuSubmit);
        menuSubmit.setOnClickListener(v -> onSubmitClicked());

        menuApprove = getTv(R.id.loanAssessmentMenuApprove);
        menuApprove.setOnClickListener(v -> onApproveClicked());

        menuDecline = getTv(R.id.loanAssessmentMenuDecline);
        menuDecline.setOnClickListener(v -> onDeclineClicked());

        fetchClient(clientId);
        fetchInfo();
        fetchPermission();
        return root;
    }

    @SuppressLint("CheckResult")
    private void fetchPermission() {
        permissionLocal.getByEntity(PermissionHelper.ENTITY_ACAT_PROCESSOR)
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(list -> {
                    permissions.clear();
                    permissions.addAll(list);

                    applyPermissions();
                });
    }

    private void applyPermissions() {
        final boolean canUpdate = hasPermission(PermissionHelper.OPERATION_UPDATE);
        final boolean canAuthorize = hasPermission(PermissionHelper.OPERATION_AUTHORIZE);

//        if (canAuthorize) {
//            toggleSaveButton(false);
//            toggleSubmitButton(false);
//        } else if (canUpdate){
//            toggleApproveButton(false);
//            toggleDeclineButton(false);
//        }

        toggleSaveButton(canUpdate);
        toggleSubmitButton(!canAuthorize);
        toggleApproveButton(false);
        toggleDeclineButton(false);
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

    private void toggleSaveButton(boolean show) {
        menuSave.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void toggleSubmitButton(boolean show) {
        menuSubmit.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void toggleApproveButton(boolean show) {
        menuApprove.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void toggleDeclineButton(boolean show) {
        menuDecline.setVisibility(show ? View.VISIBLE : View.GONE);
    }



    @SuppressLint("CheckResult")
    private void onSaveClicked() {
        if (loanProposal == null || loanProposal.loanProposed == 0) {
            toast("Please fill in loan Requested and loan proposed values.");
            return;
        }

        showUpdatingProgress();
        local.put(loanProposal)
                .flatMapCompletable(loanProposal -> loanProposalRepo.saveLoanProposal(loanProposal, cashFlow, acatApplication))
                .andThen(acatApplicationRepo.updateACATApplication(acatApplication._id, request))
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(() ->  {
                    toast("Updated successfully.");
                    getParent().finish();
                    hideUpdatingProgress();
                }, throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION)  ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        hideUpdatingProgress();
                        acatApplicationSyncLocalSource.markForUpload(acatApplication._id);
                        Log.d("TAG", "SAVED");
                        //showUpdateFailedMessage(throwable);
                        getParent().finish();
                        return;
                    }
                    showUpdateFailedMessage(throwable);
                });
//        saveACATandLoan();

    }

    @SuppressLint("CheckResult")
    private void onSubmitClicked() {
        acatSyncLocal.hasUnSyncedData()
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(hasUnSyncedData -> {
                    if (hasUnSyncedData) {
                        toast(R.string.acat_data_not_synced_message);
                    } else {
                        submitACAT();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void submitACAT() {
        showUpdatingProgress();
        Observable
                .fromIterable(cropACATList)
                .concatMap(Observable::just)
                .subscribe(cropACAT -> cropACATRepo.submitCropACAT(cropACAT._id, acatApplication._id)
                        .flatMap(done -> {
                            Observable.just(done);
                            loanProposalRepo.saveLoanProposal(loanProposal, cashFlow, acatApplication);
                            return loanProposalRepo.submitLoanProposal(loanProposal);
                        })
                        .flatMap(done -> {
                            Observable.just(done);
                            acatApplicationRepo.updateACATApplication(acatApplication._id, request);
                            return acatApplicationRepo.submitACATApplication(acatApplication);
                        })
                        .flatMapCompletable(done -> {
                            clientRepo.fetchForce(clientId);
                            return Completable.complete();
                        })
                        .subscribeOn(scheduler.background())
                        .observeOn(scheduler.ui())
                        .subscribe(() -> {
                                    onUpdateComplete();
                                    toast("Submitted successfully.");
                                },
                                this::showUpdateFailedMessage)
                );
    }
    @SuppressLint("CheckResult")
    private void onApproveClicked() {
        showUpdatingProgress();
        Observable
                .fromIterable(cropACATList)
                .concatMap(Observable::just)
                .subscribe(cropACAT1 -> cropACATRepo.approveCropACAT(cropACAT1._id, acatApplication._id)
                        .flatMap(done -> {
                            Observable.just(done);
                            return loanProposalRepo.approveLoanProposal(loanProposal);
                        })
                        .flatMap(done -> {
                            Observable.just(done);
                            return acatApplicationRepo.approveACATApplication(acatApplication);
                        })
                        .flatMapCompletable(done -> {
                            clientRepo.fetchForce(clientId);
                            return Completable.complete();
                        })
                        .subscribeOn(scheduler.background())
                        .observeOn(scheduler.ui())
                        .subscribe(this::hideUpdatingProgress));
    }

    private void onDeclineClicked() {
        openDeclineDialog();
    }

    private void openDeclineDialog() {
        declineDialog = DeclineDialog.newInstance(false, true);
        declineDialog.setCallback(this);
        declineDialog.show(getChildFragmentManager(), null);
    }


    private void setTitle(String title) {

        toolbar.setTitle(title);
    }

    @SuppressLint("CheckResult")
    private void fetchClient(String clientId) {
        loadState.setLoading();
        showLoadingProgress();

        clientRepo.fetch(clientId)
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(client -> {
                    this.client = client;
                    onLoadingComplete();
                });
    }

    private void onLoadingComplete() {
        hideLoadingProgress();
        setTitle(Utils.formatName(
                client.firstName,
                client.surname,
                client.lastName
        ));

        loadState.reset();
    }

    private void hideLoadingProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(
                getString(R.string.loan_proposal_loading_state)
        );
        waitingDialog.show(getChildFragmentManager(), null);
    }

    private void showUpdatingProgress(){
        waitingDialog = WaitingDialog.newInstance(getString(R.string.updating_acat_message));
        waitingDialog.show(getChildFragmentManager(), null);

    }

    private void hideUpdatingProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showUpdateFailedMessage(Throwable throwable) {
        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

//        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION)) {
//            hideUpdatingProgress();
//            return;
//        }

        final String message = ApiErrors.CLIENT_ACAT_UPDATE_GENERIC_ERROR;
        hideUpdatingProgress();
        showError(Result.createError(message, error));
    }

    private void showError(Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.acat_questions_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.questions_error_title),
                message,
                extra
        );

        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onNetCashFlowReturned(@NonNull CashFlow cashFlow, @NonNull ACATApplication acatApplication) {
        this.cashFlow = cashFlow;
        request.estimatedCashFlow = cashFlow;
        request.actualCashFlow = new CashFlow();
        this.acatApplication = acatApplication;
    }

    @Override
    public void onLoanProposalDataReturned(@NonNull LoanProposal loanProposal) {
        this.loanProposal = loanProposal;
    }

    @SuppressLint("CheckResult")
    private void fetchInfo() {
        acatAppLocal.get(acatId)
                .flatMap(data -> {
                    acatData = data.get();
                    request.acatApplication = acatData;
                    return cropACATLocal.getCropACATByApp(acatId);
                })
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(cropACATS -> {
                    cropACATList.clear();
                    cropACATList.addAll(cropACATS);
                });

    }

    @SuppressLint("CheckResult")
    private void saveACATandLoan() {
        acatAppLocal.getACATByClient(acatId)
                .flatMap(data -> {
                    acatData = data.get();
                    request.acatApplication = acatData;
                    return cropACATLocal.getClientACATCrop(cropACAT._id);
                })
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(cropACATS -> {
                    cropACATList.clear();
                    cropACATList.addAll(cropACATS);
                    updateCropACATS(cropACATList);
                    loanProposalRepo.saveLoanProposal(loanProposal, cashFlow, acatApplication)
                            .andThen(acatApplicationRepo.updateACATApplication(acatApplication._id, request))
                            .subscribeOn(scheduler.background())
                            .observeOn(scheduler.ui())
                            .subscribe(this::hideUpdatingProgress);
                }, throwable -> {

                });


    }

    private void updateCropACATS(List<CropACAT> cropACATList) {
        for (int i = 0; i < cropACATList.size(); i++) {
            cropACATRepo.updateCropACATInstance(cropACATList.get(0)._id, acatData._id)
                    .subscribeOn(scheduler.background())
                    .observeOn(scheduler.ui())
                    .subscribe();
        }
    }

    private void onUpdateComplete() {
        hideUpdatingProgress();
//        openCropList();
        getParent().finish();
//        startActivity(new Intent(getContext(), HomeActivity.class));
    }

    private void openCropList() {
        final Intent intent = new Intent(getActivity(), ACATCropItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void onDecline(@NonNull String remark, boolean isFinal) {
        onDeclineReturned(remark, isFinal);
    }

    @SuppressLint("CheckResult")
    private void onDeclineReturned(@NonNull String remark, boolean isFinal) {
        showUpdatingProgress();
        acatApplicationRepo.declineACATApplication(acatApplication, remark)
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(done -> hideLoadingProgress(), throwable -> throwable.printStackTrace()); // TODO: Handle Error here.
    }
}

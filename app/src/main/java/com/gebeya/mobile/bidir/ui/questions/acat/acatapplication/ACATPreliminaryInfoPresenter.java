package com.gebeya.mobile.bidir.ui.questions.acat.acatapplication;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepository;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.crop.Crop;
import com.gebeya.mobile.bidir.data.crop.local.CropLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATRemote;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepoSource;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATUtility;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 5/13/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATPreliminaryInfoPresenter implements ACATPreliminaryInfoContract.Presenter {

    private ACATPreliminaryInfoContract.View view;

    private String groupId;
    private String clientId;
    private String cropId;
    private String applicationId;
    private ACATApplication acatApplication;

    private List<CropACAT> cropACATs;
    private final List<Crop> cropList;
    private Client c;
    private String[] expenseMonth;
    private String firstExpenseMonth;
    private int position;

    @Inject
    CropACATLocalSource cropACATLocalSource;
    @Inject
    CropLocalSource cropLocal;
    @Inject CropACATRemote remote;
    @Inject
    ClientRepoSource clientRepo;
    @Inject
    ACATApplicationRepository applicationRepo;
    @Inject
    CropACATRepoSource cropACATRepo;
    @Inject
    SchedulersProvider schedulers;
    @Inject
    ACATPreliminaryInfoLoadState loadState;
    @Inject
    ACATPreliminaryInfoUpdateState updateState;
    @Inject
    GPSLocationLocalSource gpsLocal;

    @Inject
    ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    public ACATPreliminaryInfoPresenter(@NonNull String clientId, String[] expenseMonth, @Nullable String groupId) {
        this.clientId = clientId;
        this.groupId = groupId;

        cropList = new ArrayList<>();
        cropACATs = new ArrayList<>();
        this.expenseMonth = expenseMonth;

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
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
        if (updateState.loading()) {
            view.showUpdatingProgress();
            return;
        }
        if (updateState.complete()) {
            onUpdateComplete();
            return;
        }
        if (updateState.error()) {
            onUpdateFailed(loadState.getError());
            return;
        }

        loadState.setLoading();
        view.showLoadingProgress();

        applicationRepo.fetch(clientId)
                .flatMap(application -> {
                    acatApplication = application;
                    return cropACATLocalSource.getCropACATByApp(application._id);
                })
                .flatMap(cropACAT -> {
                    cropACATs.clear();
                    cropACATs.addAll(cropACAT);
                    return clientRepo.fetch(clientId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(client -> {
                            c = client;
                            loadState.setComplete();
                            onLoadingComplete();
                        },
                        throwable -> {
                            loadState.setError(throwable);
                            onLoadingFailed(throwable);
                        });

    }

    @Override
    public void attachView(ACATPreliminaryInfoContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public ACATPreliminaryInfoContract.View getView() {
        return view;
    }

    @Override
    @SuppressLint("CheckResult")
    public void onSaveClicked() {
        if (updateState.loading()) return;
        view.showUpdatingProgress();
        for (int i = 0; i < cropACATs.size(); i++) {
            cropACATRepo.updateCropACATInstance(cropACATs.get(i)._id, acatApplication._id)
                    .andThen(cropACATLocalSource.getActiveClientCropACAT(clientId, cropACATs.get(i)._id))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(cropACATData -> {
//                                if (cropACATData.empty()) {
//                                    openCropList();
//                                } else {
//                                    openACATProcessor();
//                                }
                                view.close();
                                openCropList(groupId);
                            },
                            throwable -> {
                                String error = throwable.getMessage();
                                if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                                        error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException) ) {
                                    acatApplicationSyncLocalSource.markForUpload(acatApplication._id);
                                    openCropList(groupId);
                                    return;
                                }
                                updateState.setError(throwable);
                                onUpdateFailed(throwable);
                            });
        }
    }

    private void openCropList(@Nullable String groupId) {
        if (view == null) return;
        view.hideLoadingProgress();
        updateState.setComplete();

        view.showUpdateSuccessMessage();
        updateState.reset();

        view.openCropListActivity(clientId, acatApplication._id, groupId);
    }

    private void openACATProcessor() {
        if (view == null) return;
        view.hideLoadingProgress();
        updateState.setComplete();

        view.showUpdateSuccessMessage();
        updateState.reset();

        view.openCostEstimationActivity(clientId);
    }

    @Override
    public void onBackButtonPressed() {
        loadState.reset();
        updateState.reset();

        view.close();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onBindRowView(ACATPreliminaryInfoContract.QuestionItemView view, int position) {
        final CropACAT cropACAT = cropACATs.get(position);

        view.setCropName(cropACAT.cropName);

        int monthPosition = 0;
        if (cropACAT.firstExpenseMonth != null) {
            monthPosition = ACATUtility.getMonthIndex(cropACAT.firstExpenseMonth);
        }
        view.setFirstExpenseMonth(monthPosition);
        view.setCroppingArea(cropACAT.croppingArea);
        view.toggleNonFinServices(cropACAT.accessToNonFinServices);
        view.showDetailButton(cropACAT.accessToNonFinServices);

        gpsLocal.getAllCropACATGPSLocations(cropACAT._id, acatApplication._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    final int size = list.size();
                    view.setLocationInfo(size);
                    view.toggleLocationAddEditButton(size != 0);
                });
    }

    @Override
    public int getCropCount() {
        return cropACATs.size();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onNonFinancialServiceToggled(boolean toggled, int position) {
        final CropACAT cropACAT = cropACATs.get(position);
        final ArrayList<String> serviceList = new ArrayList<>(cropACAT.NonFinServices);

        if (toggled) {
            //saveLoanProposal the local copy in memory
            view.openNonFinancialServiceDialog(position, serviceList);
        } else {
            //saveLoanProposal the local copy in memory
            cropACAT.NonFinServices = new ArrayList<>();
            cropACAT.accessToNonFinServices = false;

            cropACATLocalSource.put(cropACAT)
                    .subscribeOn(schedulers.background())
                    .subscribe();
        }

        view.refreshItem(position);
    }

    @Override
    @SuppressLint("CheckResult")
    public void onDetailButtonPressed(int position) {
        final CropACAT cropACAT = cropACATs.get(position);

        cropACATLocalSource.get(cropACAT._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(cropACATData -> {
                    CropACAT crop = cropACATData.get();
                    final List<String> servicesList = new ArrayList<>(crop.NonFinServices);
                    view.openNonFinancialServiceDialog(position, servicesList);
                });
    }

    @Override
    public void onGPSLocationPressed(int position) {
        final CropACAT cropACAT = cropACATs.get(position);
        view.openGPSSelector(cropACAT._id, acatApplication._id);
    }

    @Override
    public void onNonFinancialResourceChanged(@NonNull List<String> services, int position) {
        //saveLoanProposal the local copy in memory
        CropACAT cropACAT = cropACATs.get(position);
        cropACAT.NonFinServices = services;
        cropACAT.accessToNonFinServices = !services.isEmpty();

        cropACATLocalSource.put(cropACAT)
                .subscribeOn(schedulers.background())
                .subscribe();

        if (view == null) return;
        view.refreshItem(position);
    }

    @Override
    public void onNonFinancialResourcesDismissed(int position) {
        if (view == null) return;
        view.refreshItem(position);
    }

    @Override
    public void onFirstExpenseMonthSelected(@NonNull String expenseMonth, int position) {
        //Update the local copy in memory
        final CropACAT cropACAT = cropACATs.get(position);
        cropACAT.firstExpenseMonth = expenseMonth;

        cropACATLocalSource.put(cropACAT)
                .subscribeOn(schedulers.background())
                .subscribe();
    }

    @Override
    public void onCroppingAreaChanged(@NonNull String croppingArea, int position) {
        //Update the local copy in memory
        final CropACAT cropACAT = cropACATs.get(position);
        cropACAT.croppingArea = croppingArea;

        cropACATLocalSource.put(cropACAT)
                .subscribeOn(schedulers.background())
                .subscribe();
    }

    @Override
    public void onSuccessDismissed() {

    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.hideUpdatingProgress();
        view.showUpdateSuccessMessage();

        updateState.reset();

        view.close();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.ACAT_FORM_UPDATE_GENERIC_ERROR;

        view.hideUpdatingProgress();
        view.showError(Result.createError(message, error));

        updateState.reset();
    }

    @Override
    public void onLoadingComplete() {
        if (view == null) return;
        view.hideLoadingProgress();
        view.setTitle(Utils.formatName(
                c.firstName,
                c.surname,
                c.lastName
        ));

        view.showCrops();
        loadState.reset();
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        if (view == null) return;
        view.hideLoadingProgress();

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = ApiErrors.ACAT_FORM_LOAD_GENERIC_ERROR;

        view.showError(Result.createError(message, error));
        loadState.reset();
    }
}

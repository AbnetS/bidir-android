package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepository;
import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.gebeya.mobile.bidir.data.acatform.ACATFormRepoSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.crop.Crop;
import com.gebeya.mobile.bidir.data.crop.local.CropLocalSource;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepoSource;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProductRepoSource;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 5/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class PreliminaryInformationPresenter implements PreliminaryInformationContract.Presenter {

    private PreliminaryInformationContract.View view;

    @Inject ACATFormRepoSource acatRepoSource;
    @Inject LoanProductRepoSource loanProductRepoSource;
    @Inject ClientRepoSource clientRepo;
    @Inject PermissionLocalSource permissionLocalSource;
    @Inject SchedulersProvider schedulers;
    @Inject CropLocalSource cropLocal;
    @Inject ACATApplicationRepository acatRepository;
    @Inject ClientLocalSource clientLocal;
    @Inject PreliminaryInformationState loadState;
    @Inject LoanProposalRepoSource loanProposalRepoSource;

    @Inject ACATApplicationLocalSource acatApplicationLocalSource;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupedACATRepoSource groupACATRepo;

    private String clientId;
    private String groupId;
    private Client client;
    private final List<Crop> crops;
    private final List<String> selectedIds;
    private final List<LoanProduct> loanProducts;
    private final List<ACATForm> acatForms;
    private ACATApplication acatApplication;

    private LoanProduct loanProduct;

    public PreliminaryInformationPresenter(@NonNull String clientId, @Nullable String groupId) {
        Tooth.inject(this, Scopes.SCOPE_STATES);

        this.clientId = clientId;
        this.groupId = groupId;

        crops = new ArrayList<>();
        selectedIds = new ArrayList<>();
        loanProducts = new ArrayList<>();
        acatForms = new ArrayList<>();
        loanProduct = new LoanProduct();
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {

        if (loadState.loading()) {
            view.showUpdateSuccessMessage();
            return;
        }

        if (loadState.complete()) {
            onUpdateComplete();
            return;
        }

        if (loadState.error()) {
            onUpdateFailed(loadState.getError());
            return;
        }

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

        loanProductRepoSource.fetchAll()
                .flatMap(list -> {
                    loanProducts.clear();
                    loanProducts.addAll(list);
                    return acatRepoSource.fetchAll();
                })
                .flatMap(list -> {
                    acatForms.clear();
                    acatForms.addAll(list);
                    return cropLocal.getAll();
                })
                .flatMap(list -> {
                    crops.clear();
                    crops.addAll(list);
                    return clientRepo.fetch(clientId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(c -> {
                    client = c;
                    loadState.setComplete();
                    onLoadingComplete();
                }, throwable -> {
                    loadState.setError(throwable);
                    onLoadingFailed(loadState.getError());
                });
    }

    @Override
    public void attachView(PreliminaryInformationContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public PreliminaryInformationContract.View getView() {
        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onNextClicked() {
        if (!hasCropSelected()) {
            view.showAnswerMissingError();
            return;
        }

        loadState.setLoading();
        view.showLoadingProgress();

        if (groupId == null) {
            acatRepository.initializeClientACAT(client, loanProduct, selectedIds)
                    .andThen(acatRepository.fetchForce(client._id))
                    .flatMapCompletable(acatApplication -> {
                        this.acatApplication = acatApplication;
                        return loanProposalRepoSource.createLoanProposal(this.acatApplication, loanProduct);
                    })
                    .andThen(clientRepo.fetchForce(clientId))
                    .flatMap(done -> acatRepoSource.fetchForceAll())
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(client -> {//
                                loadState.setComplete();
                                loadState.reset();
                                view.hideProgress();
                                view.openACATPreliminaryInfo(clientId, groupId);
                            },
                            throwable -> {
                                loadState.setError(throwable);
                                onLoadingFailed(loadState.getError());
                            });
        } else {
            groupACATRepo.initializeMember(groupId, client, loanProduct, selectedIds)
                    .andThen( acatRepository.fetchForce(client._id))
                    .flatMapCompletable(acatApplication -> {
                        this.acatApplication = acatApplication;
                        return loanProposalRepoSource.createLoanProposal(this.acatApplication, loanProduct);
                    })
                    .andThen(clientRepo.fetchForce(clientId))
                    .flatMap(done -> acatRepoSource.fetchForceAll())
                    .flatMap(done -> groupRepo.fetchForce(groupId))
                    .flatMap(done -> groupACATRepo.fetchForce(groupId))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(client -> {//
                                loadState.setComplete();
                                loadState.reset();
                                view.hideProgress();
                                view.openACATPreliminaryInfo(clientId, groupId);
                            },
                            throwable -> {
                                loadState.setError(throwable);
                                onLoadingFailed(loadState.getError());
                            });
        }

    }

    private boolean hasCropSelected() {
        return !selectedIds.isEmpty();
    }

    @Override
    public void onCropToggled(int position) {
        toggleCrop(position);
        view.refreshAnswers();
    }

    private void toggleCrop(int position) {
        final Crop crop = crops.get(position);
        final String id = crop.acatId;
        if (selectedIds.contains(id)) {
            selectedIds.remove(id);
        } else {
            selectedIds.add(id);
        }

        view.updateSelectedCropsCount(selectedIds.size());
        view.toggleNextButton(!selectedIds.isEmpty());
    }

    @Override
    public void onBindRowView(PreliminaryInformationContract.PreliminaryQuestionItemView view, int position) {
        final Crop crop = crops.get(position);
        view.setChoice(crop.name, selectedIds.contains(crop.acatId));
    }

    @Override
    public void onBindProductRowItem(@NonNull PreliminaryInformationContract.LoanProductItemView holder, int position) {
        final LoanProduct product = loanProducts.get(position);
        holder.setName(product.name);
    }

    @Override
    public int getLoanProductCount() {
        return loanProducts.size();
    }

    @Override
    public void onLoanProductItemSelected(int position) {
        final LoanProduct product = loanProducts.get(position);
        loanProduct = product;
    }

    @Override
    public int getAnswerCount() {
        return crops.size();
    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.showUpdateSuccessMessage();

        loadState.reset();
        view.close();
    }

    @Override
    public void onLoadingComplete() {
        if (view == null) return;
        view.hideProgress();

        view.setTitle(Utils.formatName(
                client.firstName,
                client.surname,
                client.lastName
        ));

        view.showAnswers();
        view.showLoanProducts();
        view.updateSelectedCropsCount(selectedIds.size());
        view.toggleNextButton(!selectedIds.isEmpty());
        loadState.setComplete();
        loadState.reset();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = ApiErrors.ACAT_FORM_UPDATE_GENERIC_ERROR;

        view.hideProgress();
        view.showError(Result.createError(message, error));

        loadState.reset();
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        if (view == null) return;
        view.hideProgress();

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = ApiErrors.ACAT_FORM_LOAD_GENERIC_ERROR;

        view.showLoadingError(Result.createError(message, error));
        loadState.reset();
    }

    @Override
    public void onErrorDismissed() {
        if (view == null) return;

        view.close();
    }

    @Override
    public void onBackButtonPressed() {
        loadState.reset();
        view.close();
    }
}

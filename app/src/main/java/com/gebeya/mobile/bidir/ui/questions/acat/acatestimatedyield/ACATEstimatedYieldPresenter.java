package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.repo.ACATRevenueRepoSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepo;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.repo.YieldConsumptionRepo;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATUtility;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostUpdater;

import java.net.UnknownHostException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Implementation for the {@link ACATEstimatedYieldContract.Presenter} presenter.
 */
public class ACATEstimatedYieldPresenter implements ACATEstimatedYieldContract.Presenter {

    private ACATEstimatedYieldContract.View view;

    private static final String SECTION_TYPE_PROBABLE = "Probable";
    private static final String SECTION_TYPE_MAX = "Maximum";
    private static final String SECTION_TYPE_MIN = "Minimum";

    private final String clientId;
    private final String cropACATId;
    private final boolean isEditing;
    public final boolean isMonitoring;

    private ACATApplication application;
    private CropACAT cropACAT;
    private ACATRevenueSection revenueSection;
    private final List<ACATRevenueSection> yieldSections;
    private List<ACATRevenueSectionRequest> request;
    private List<CashFlow> cashFlowList;
    private int activePosition;

    private ACATItem acatItem;
    private double totalProbableRevenue;
    private double totalForMarketRevenue;
    private double probableUnitPrice;

    private ACATItemValue estimatedValue;
    private ACATApplicationResponse acatApplicationResponse;
//    private ACATItemValue actualValue;

    @Nullable private YieldConsumption yieldConsumption;
    private CashFlow yieldCashFlow;

    private final List<CashFlowItem> yieldCashOutflowItems;
    private final List<CashFlowItem> netCashOutflowItems;

    @Inject ClientRepoSource clientRepo;
    @Inject ACATApplicationLocalSource acatApplicationLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject ACATRevenueSectionLocalSource sectionLocal;
    @Inject YieldConsumptionLocalSource yieldConsumptionLocal;
    @Inject ACATItemLocalSource acatItemLocal;
    @Inject ACATItemValueLocalSource acatItemValueLocal;
    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject ACATCostUpdater acatCostUpdater;
    @Inject CropACATRepo cropACATRepo;
    @Inject ACATRevenueRepoSource revenueRepo;
    @Inject YieldConsumptionRepo yieldConsumptionRepo;
    @Inject SchedulersProvider schedulers;
    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;


    public ACATEstimatedYieldPresenter(@NonNull String clientId, @NonNull boolean isEditing, @Nullable String cropACATId, @NonNull boolean isMonitoring) {
        this.clientId = clientId;
        this.cropACATId = cropACATId;
        this.isEditing = isEditing;
        this.isMonitoring = isMonitoring;
        yieldSections = new ArrayList<>();
        activePosition = 0;
        yieldCashOutflowItems = new ArrayList<>();
        netCashOutflowItems = new ArrayList<>();
        request = new ArrayList<>();
        cashFlowList = new ArrayList<>();
        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Override
    public void start() {
        loadClient();
/**
        if (isEditing) {
            loadCropItemRevenueSection();
        } else {
            loadACATRevenueSections();
        }
 **/
        loadCropItemRevenueSection();

    }

    @SuppressLint("CheckResult")
    private void loadCropItemRevenueSection() {
        cropACATLocal.get(cropACATId)
                .flatMap(cropACATData -> {
                    cropACAT = cropACATData.get();
                    return acatApplicationLocal.get(cropACAT.acatApplicationID);
                })
                .flatMap(acatApplicationData -> {
                    application = acatApplicationData.get();
                    return sectionLocal.get(cropACAT.revenueSectionId);
                })
                .flatMap(data -> {
                    revenueSection = data.get();
                    return sectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    view.setCropLabel(cropACAT.cropName);
                    yieldSections.clear();
                    yieldSections.addAll(list);

                    if (yieldSections.isEmpty()) return;

                    view.highlightYieldTitle(activePosition);
                    loadNetCashFlow(yieldSections.get(0), true);
                    loadData(activePosition);
                }, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void loadClient() {
        clientRepo.fetch(clientId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(client -> {
                    if (view == null) return;
                    view.setTitle(Utils.formatName(
                            client.firstName,
                            client.surname,
                            client.lastName
                    ));
                });
    }



    @Override
    public void onBindYieldViewHolder(@NonNull ACATEstimatedYieldContract.CashFlowItemView holder, int position) {
        final CashFlowItem item = yieldCashOutflowItems.get(position);
        holder.setTitle(item.month);
        holder.setValue(item.value);
    }

    @Override
    public void onBindNetViewHolder(@NonNull ACATEstimatedYieldContract.CashFlowItemView holder, int position) {
        final CashFlowItem item = netCashOutflowItems.get(position);
        holder.setTitle(item.month);
        holder.setValue(item.value);
    }

    @Override
    public int cashNetFlowCount() {
        return netCashOutflowItems.size();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onNextClicked(@Nullable ACATEstimatedYieldDto dto) throws Exception {
        view.showUpdatingMessage();
        if (dto != null ) { // Save the last edited items.
            switch (activePosition) {
                case 0:
                    saveYieldNetCashFlowData();
                    saveYieldData(dto);
                    saveYieldCashOutflowData();
                    saveProbableRevenueData();
                    break;
                case 1:
                    saveYieldData(dto);
                    saveYieldCashOutflowData();
                    saveMaxRevenueData();
                    break;
                case 2:
                    saveYieldData(dto);
                    saveYieldCashOutflowData();
                    saveMinRevenueData();
            }


            acatCostUpdater.computeEstimatedNetIncomeAndCashFlow(cropACAT._id, Double.valueOf(dto.estimatedForMarket), probableUnitPrice)
                    .flatMap(response -> {

                        this.acatApplicationResponse = response;
                        return cropACATRepo.updateProgress(cropACAT._id, cropACAT.acatApplicationID);
                    })
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(this::onUpdateComplete,
                            this::onUpdateFailed);
        }
/**
        Observable.just(true)
                .flatMap(ignore -> {
//                    acatCostUpdater.computeEstimatedNetIncomeAndCashFlow(cropACAT._id);        // TODO: THIS IS CAUSING PROBLEMS
                    return Observable.just(true);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                    if (view == null) return;
                    view.openLoanProposal(clientId);
                });
 **/
    }

    @SuppressLint("CheckResult")
    @Override
    public void onUpdateComplete(@NonNull CropACAT response) {
        if (view == null) return;

        if (acatApplicationResponse != null) {
            response.complete = true;
            response.active = false;
            cropACATLocal.put(response)
                    .flatMap(cropACAT -> uploadRevenueSection())
                    .flatMap(done -> uploadYieldConsumption())
                    .flatMap(done -> cropACATLocal.getActiveClientCropACAT(clientId, cropACAT.acatApplicationID))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(cropACATData -> {
                            view.hideUpdatingMessage();
                            if (view == null) return;
                            if (!cropACATData.empty() && !isEditing) {
                                view.openCropList(clientId);
                            } else {
                                view.close();
                                view.openLoanProposal(clientId, cropACAT.acatApplicationID);
                            }
                        },
                            this::onUpdateFailed);
        }

    }

    // TODO: Find a better way to do this.
    private Observable<ACATRevenueSection> uploadRevenueSection() {
        ACATRevenueSection section = new ACATRevenueSection();

        ACATRevenueSectionRequest request = new ACATRevenueSectionRequest();
        request.acatApplicationId = cropACAT.acatApplicationID;
        return sectionLocal.get(cropACAT.revenueSectionId)
                .flatMap(data -> {
                    revenueSection = data.get();
                    return sectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .flatMap(list -> {
                    yieldSections.clear();
                    yieldSections.addAll(list);
                    request.section = list.get(0);
                    return cashFlowLocal.get(list.get(0)._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(yieldSections.get(0)._id, request);
                })
                .flatMap(response -> {
                    request.section = yieldSections.get(1);
                    return cashFlowLocal.get(request.section._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(yieldSections.get(1)._id, request);
                })
                .flatMap(response -> {
                    request.section = yieldSections.get(2);
                    return cashFlowLocal.get(request.section._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(yieldSections.get(2)._id, request);
                })
                .flatMap(response -> {
                    request.section = yieldSections.get(0);
                    return cashFlowLocal.get(request.section._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    request.section = revenueSection;
                    return revenueRepo.updateACATRevenueSection(revenueSection._id, request);
                })
                .onErrorResumeNext((Function<? super Throwable, ? extends ObservableSource<? extends ACATRevenueSection>>) throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(cropACAT.acatApplicationID);
                        return Observable.just(section);
                    }
                    else
                        throw new Exception(throwable);
                });
    }

    private Observable<YieldConsumption> uploadYieldConsumption() {
        YieldConsumption consumption = new YieldConsumption();
        YieldConsumptionRequest request = new YieldConsumptionRequest();
        return sectionLocal.get(cropACAT.revenueSectionId)
                .flatMap(data -> {
                    revenueSection = data.get();
                    return sectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .flatMap(list -> {
                    ACATRevenueSection section = list.get(0);
                    return yieldConsumptionLocal.get(section.yieldConsumptionID);
                })
                .flatMap(data -> {
                    YieldConsumption yieldConsumption = data.get();
                    request.yieldConsumption = yieldConsumption;
                    return yieldConsumptionRepo.update(yieldConsumption._id, request);
                })
                .onErrorResumeNext((Function<? super Throwable, ? extends ObservableSource<? extends YieldConsumption>>) throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(cropACAT.acatApplicationID);
                        return Observable.just(consumption);
                    }
                    else
                        throw new Exception(throwable);
                });
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
            acatApplicationSyncLocalSource.markForUpload(cropACAT.acatApplicationID);
            view.hideUpdatingMessage();
            return;
        }

        final String message = ApiErrors.SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR;
        throwable.printStackTrace();
        view.hideUpdatingMessage();
        view.showError(Result.createError(message, error));
    }

    @Override
    public void onDetailButtonClicked() {
        view.openForMarketDialog();
    }

    @Override
    public boolean getMonitoring() {
        return isMonitoring;
    }

    @Override
    public void onPreviousClicked(@Nullable ACATEstimatedYieldDto dto) throws Exception {
        if (dto != null) { // saving the last edited values.
            switch (activePosition) {
                case 0:
                    saveYieldNetCashFlowData();
                    saveYieldData(dto);
                    saveYieldCashOutflowData();
                    saveProbableRevenueData();
                    break;
                case 1:
                    saveYieldData(dto);
                    saveYieldCashOutflowData();
                    saveMaxRevenueData();
                    break;
                case 2:
                    saveYieldData(dto);
                    saveYieldCashOutflowData();
                    saveMinRevenueData();
            }

            view.showUpdatingMessage();
            acatCostUpdater.computeEstimatedNetIncomeAndCashFlow(cropACAT._id, Double.valueOf(dto.estimatedForMarket), probableUnitPrice)
                    .flatMap(response -> {
                        this.acatApplicationResponse = response;
                        return cropACATRepo.updateProgress(cropACAT._id, cropACAT.acatApplicationID);
                    })
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(response -> view.openPreviousScreen(),
                            this::onUpdateFailed);
        }
    }

    @Override
    public void onRotationCallBackReturned() {
        view.openACATCostEstimation(clientId);
    }

    @Override
    public int cashYieldFlowCount() {
        return yieldCashOutflowItems.size();
    }

    @Override
    public void onCashFlowItemUpdated(@NonNull String value, int position) {
        final CashFlowItem item = yieldCashOutflowItems.get(position);
        item.value = value;

        if (activePosition == 0) {
            refreshNetCashOutflow();
            view.refreshNetCashOutflow();
        }
    }

    @Override
    public void onYieldSelected(int position, @NonNull ACATEstimatedYieldDto dto) {
        if (view == null) return;

        if (position == activePosition) return;

        if (dto.estimatedUnit.isEmpty()) {
            view.showMissingUnitError(true);
            return;
        }
/*        if (dto.actualUnit.isEmpty()) {
            view.showMissingUnitError(false);
            return;
        }*/

        if (dto.estimatedQuantity.isEmpty()) {
            view.showMissingQuantityError(true);
            return;
        }
        if (parse(dto.estimatedQuantity) == 0) {
            view.showInvalidQuantityError(true);
            return;
        }
/*        if (dto.actualQuantity.isEmpty()) {
            view.showMissingQuantityError(false);
            return;
        }
        if (parse(dto.actualQuantity) == 0) {
            view.showInvalidQuantityError(false);
            return;
        }*/

        if (dto.estimatedUnitPrice.isEmpty()) {
            view.showMissingUnitPriceError(true);
            return;
        }
        if (parse(dto.estimatedUnitPrice) == 0) {
            view.showInvalidUnitPriceError(true);
            return;
        }
/*        if (dto.actualUnitPrice.isEmpty()) {
            view.showMissingUnitPriceError(false);
            return;
        }
        if (parse(dto.actualUnitPrice) == 0) {
            view.showInvalidUnitPriceError(false);
            return;
        }*/

        if (yieldConsumption != null) {
            if (dto.estimatedOwnCons.isEmpty()) {
                view.showMissingOwnConsError(true);
                return;
            }
/*            if (dto.actualOwnCons.isEmpty()) {
                view.showMissingOwnConsError(false);
                return;
            }*/

            if (dto.estimatedSeedRes.isEmpty()) {
                view.showMissingSeedResError(true);
                return;
            }
/*            if (dto.actualSeedRes.isEmpty()) {
                view.showMissingSeedResError(false);
                return;
            }*/

            if (dto.estimatedForMarket.isEmpty()) {
                view.showMissingForMarketError(true);
                return;
            }
/*            if (dto.actualForMarket.isEmpty()) {
                view.showMissingForMarketError(false);
                return;
            }*/

            final double estimatedTotalYieldConsumption =
                    parse(dto.estimatedOwnCons) +
                            parse(dto.estimatedSeedRes) +
                            parse(dto.estimatedForMarket);

            if (estimatedTotalYieldConsumption != parse(dto.estimatedQuantity)) {
                view.showInvalidYieldConsumptionInputsError(true);
                return;
            }
/*            final double actualTotalYieldConsumption =
                    parse(dto.actualOwnCons) +
                    parse(dto.actualSeedRes) +
                    parse(dto.actualForMarket);
            if (actualTotalYieldConsumption != parse(dto.actualQuantity)) {
                view.showInvalidYieldConsumptionInputsError(false);
                return;
            }*/
        }

        if (position != activePosition) {
            if (activePosition == 0) {
                saveYieldNetCashFlowData();
                probableUnitPrice = Double.parseDouble(dto.estimatedUnitPrice);
            }
            saveYieldData(dto);
            saveYieldCashOutflowData();
            saveProbableRevenueData();
//            saveMaxRevenueData();
//            saveMinRevenueData();
        }

        activePosition = position;
        view.highlightYieldTitle(position);

        loadData(position);
    }

    @SuppressLint("CheckResult")
    private void saveProbableRevenueData() {
        saveProbableRevenueSectionData()
                .flatMap(this::saveProbableParentSectionData)
                .subscribeOn(schedulers.background())
                .subscribe(saved -> {}, Throwable::printStackTrace);
    }

    private void saveMaxRevenueData() {
        saveMaxRevenueSectionData()
                .flatMap(this::saveMaxParentSectionData)
                .subscribeOn(schedulers.background())
                .subscribe(saved -> {}, Throwable::printStackTrace);
    }

    private void saveMinRevenueData() {
        saveMinRevenueSectionData()
                .flatMap(this::saveMinParentSectionData)
                .subscribeOn(schedulers.background())
                .subscribe(saved -> {}, Throwable::printStackTrace);
    }

    private Observable<ACATItemValue> saveProbableRevenueSectionData() {
        final ACATRevenueSection section = getProbableRevenueSection();
        return acatItemLocal.get(section.yieldID) //was ._id
                .flatMap(data -> {
                    final ACATItem item = data.get();
                    return acatItemValueLocal.getByType(item._id, ACATItemValueHelper.ESTIMATED_VALUE_TYPE); // TODO: Consider making it to work with actual values as well
                })
                .flatMap(data -> {
//                    if (data.empty()) return Observable.just(new ACATItemValue());
                    final ACATItemValue value = data.get();
                    section.estimatedSubTotal = totalProbableRevenue;
                    totalForMarketRevenue = value.totalPrice;
                    sectionLocal.put(section);
                    return Observable.just(value);
                });
    }

    private Observable<ACATItemValue> saveProbableParentSectionData(@NonNull ACATItemValue value) {
//        revenueSection.estimatedProbRevenue = value.totalPrice;
        revenueSection.estimatedProbRevenue = totalForMarketRevenue;
        revenueSection.estimatedSubTotal = value.totalPrice;
        return sectionLocal.put(revenueSection).flatMap(updated -> Observable.just(value));
    }

    private Observable<ACATItemValue> saveMaxRevenueSectionData() {
        final ACATRevenueSection section = getMaxRevenueSection();
        return acatItemLocal.get(section.yieldID) //was ._id
                .flatMap(data -> {
                    final ACATItem item = data.get();
                    return acatItemValueLocal.getByType(item._id, ACATItemValueHelper.ESTIMATED_VALUE_TYPE); // TODO: Consider making it to work with actual values as well
                })
                .flatMap(data -> {
                    final ACATItemValue value = data.get();
                    section.estimatedSubTotal = value.totalPrice;
                    sectionLocal.put(section);
                    return Observable.just(value);
                });
    }

    private Observable<ACATItemValue> saveMaxParentSectionData(@NonNull ACATItemValue value) {
        revenueSection.estimatedMaxRevenue = value.totalPrice;
        revenueSection.estimatedSubTotal = value.totalPrice;
        return sectionLocal.put(revenueSection).flatMap(updated -> Observable.just(value));
    }

    private Observable<ACATItemValue> saveMinRevenueSectionData() {
        final ACATRevenueSection section = getMinRevenueSection();
        return acatItemLocal.get(section.yieldID) //was ._id
                .flatMap(data -> {
                    final ACATItem item = data.get();
                    return acatItemValueLocal.getByType(item._id, ACATItemValueHelper.ESTIMATED_VALUE_TYPE); // TODO: Consider making it to work with actual values as well
                })
                .flatMap(data -> {
                    final ACATItemValue value = data.get();
                    section.estimatedSubTotal = value.totalPrice;
                    sectionLocal.put(section);
                    return Observable.just(value);
                });
    }

    private Observable<ACATItemValue> saveMinParentSectionData(@NonNull ACATItemValue value) {
        revenueSection.estimatedMinRevenue = value.totalPrice;
        revenueSection.estimatedSubTotal = value.totalPrice;
        return sectionLocal.put(revenueSection).flatMap(updated -> Observable.just(value));
    }

    private ACATRevenueSection getProbableRevenueSection() {
//        for (ACATRevenueSection section : yieldSections) {
//            if (section.yieldConsumptionID != null) return section; //was ==
//        }
        return yieldSections.get(0);
    }

    private ACATRevenueSection getMaxRevenueSection() {
//        for (ACATRevenueSection section : yieldSections) {
//            if (section.yieldConsumptionID != null) return section; //was ==
//        }
        return yieldSections.get(1);
    }

    private ACATRevenueSection getMinRevenueSection() {
//        for (ACATRevenueSection section : yieldSections) {
//            if (section.yieldConsumptionID != null) return section; //was ==
//        }
        return yieldSections.get(2);
    }


    @SuppressLint("CheckResult")
    private void loadData(int position) {
        final ACATRevenueSection section = yieldSections.get(position);

        // ACATItem -> "yield"
        loadYield(section.yieldID, true);
        // YieldConsumption -> "yield_consumption"
        loadYieldConsumption(section.yieldConsumptionID, true);

        loadRevenues();

    }

    private void loadRevenues() {
        if (view == null) return;

        view.setProbableRevenue(String.valueOf(revenueSection.estimatedProbRevenue));
        view.setMaximumRevenue(String.valueOf(revenueSection.estimatedMaxRevenue));
        view.setMinimumRevenue(String.valueOf(revenueSection.estimatedMinRevenue));
    }

    private void saveYieldCashOutflowData() {
        updateYieldCashFlowData(yieldCashOutflowItems, yieldCashFlow);
        cashFlowLocal.put(yieldCashFlow)
                .subscribeOn(schedulers.background())
                .subscribe();
    }

    private void updateYieldCashFlowData(@NonNull List<CashFlowItem> items, @NonNull CashFlow cashFlow) {
        final int length = items.size();
        for (int i = 0; i < length; i++) {
            final CashFlowItem item = items.get(i);
            final String value = item.value.trim();
            final int position = ACATUtility.returnMonthPositionInOriginalList(item.month);
            final double monthValue = parse(value);
            setMonthValue(cashFlow, monthValue, position);
        }
    }

    private void saveYieldData(@NonNull ACATEstimatedYieldDto dto) {
        updateACATItemData(dto);

        updateACATItemValueData(estimatedValue, dto, true);
//        updateACATItemValueData(actualValue, dto, false);
        uploadYieldConsumption(yieldConsumption, dto);

        acatItemLocal.put(acatItem)
                .flatMap(saved -> acatItemValueLocal.put(estimatedValue))
//                .flatMap(saved -> acatItemValueLocal.put(actualValue))
                .flatMap(saved -> {
                    if (yieldConsumption != null) {
                        return yieldConsumptionLocal.put(yieldConsumption);
                    }
                    return Observable.just(true);
                })
                .subscribeOn(schedulers.background())
                .subscribe();
    }

    private void updateACATItemData(@NonNull ACATEstimatedYieldDto dto) {
        acatItem.unit = dto.estimatedUnit;      // TODO: Change this to also account for actual
        acatItem.remark = dto.yieldRemark;
    }

    private void updateACATItemValueData(@NonNull ACATItemValue value, @NonNull ACATEstimatedYieldDto dto, boolean estimated) {
        value.value = parse(estimated ? dto.estimatedQuantity : dto.actualQuantity);
        value.unitPrice = parse(estimated ? dto.estimatedUnitPrice : dto.actualUnitPrice);
        if (!dto.estimatedForMarket.isEmpty() && activePosition == 0)//Compute revenue from the "For Market" value
            value.totalPrice = Double.parseDouble(dto.estimatedForMarket) * value.unitPrice;
        else
            value.totalPrice = value.value * value.unitPrice;
    }

    @SuppressLint("CheckResult")
    private void loadYield(@NonNull String yieldId, boolean estimated) {
        acatItemLocal.get(yieldId)
                .flatMap(data -> {
                    acatItem = data.get();
                    return acatItemValueLocal.getByType(acatItem._id, estimated ?
                            ACATItemValueHelper.ESTIMATED_VALUE_TYPE :
                            ACATItemValueHelper.ACTUAL_VALUE_TYPE
                    );
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (view == null) return;

                    final ACATItemValue value = data.get();
                    if (estimated) {
                        estimatedValue = value;
                    } else {
//                        actualValue = value;
                    }
                    view.setUnit(acatItem.unit, estimated);
                    view.setQuantity(value.value, estimated);
                    view.setUnitPrice(value.unitPrice, estimated);
                    view.setTotal(value.value * value.unitPrice, estimated);
                    if (activePosition == 0) {
                        totalProbableRevenue = value.value * value.unitPrice;
                        probableUnitPrice = value.unitPrice;
                    }
                    //view.setTotal(value.totalPrice, estimated);
                    view.setYieldRemark(acatItem.remark);

                    loadCashFlow(acatItem._id, estimated);
                });
    }

    @SuppressLint("CheckResult")
    private void saveYieldNetCashFlowData() {
        final ACATRevenueSection activeSection = yieldSections.get(activePosition);
        cashFlowLocal.get(activeSection._id,
                CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Observable.error(new Throwable("Could not load CashFlows from reference ID: " + activeSection._id));
                    } else {
                        final CashFlow cashFlow = list.get(0);
                        updateYieldCashFlowData(netCashOutflowItems, cashFlow);
                        cashFlowLocal.put(cashFlow);
                        return cashFlowLocal.get(revenueSection._id,
                                CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                    }
                })
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Observable.error(new Throwable("Could not load CashFlows from reference ID: " + revenueSection._id));
                    } else {
                        if (activeSection.yieldConsumptionID != null) {     // Copy the probable.estimated_cash_flow to revenue.estimated_cash_flow (only if the current yield is a probable yield)
                            final CashFlow cashFlow = list.get(0);
                            updateYieldCashFlowData(netCashOutflowItems, cashFlow);
                            return cashFlowLocal.put(cashFlow);
                        } else {                            // Maximum or minimum yield
                            return Observable.just(true);
                        }
                    }
                })
                .subscribeOn(schedulers.background()).subscribe(done -> {
        }, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void loadCashFlow(@NonNull String referenceId, boolean estimated) {
        final String type = estimated ? CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE : CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;
        cashFlowLocal.get(referenceId, type, CashFlowHelper.ITEM_CASH_FLOW)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (list.isEmpty()) {
                        Utils.e(ACATEstimatedYieldPresenter.this, "Could not load CashFlows from reference ID: %s", referenceId);
                        return;
                    }
                    if (view == null) return;
                    yieldCashFlow = list.get(0);
                    generateYieldCashOutflowData();
                });
    }

    @SuppressLint("CheckResult")
    private void loadNetCashFlow(@NonNull ACATRevenueSection section, boolean estimated) {
        final String type = estimated ? CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE : CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;
        acatItemLocal.get(section.yieldID)
                .flatMap(data ->{
                    ACATItem acatItem = data.get();
                    return cashFlowLocal.get(acatItem._id, type, CashFlowHelper.ITEM_CASH_FLOW);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (list.isEmpty()) {
                        Utils.e(ACATEstimatedYieldPresenter.this, "Could not load CashFlows from reference ID: %s", section.yieldID);
                        return;
                    }
                    if (view == null) return;
                    yieldCashFlow = list.get(0);
                    generateNetCashFlow();
                });
    }

    private void generateYieldCashOutflowData() {
        final String[] months = new DateFormatSymbols().getShortMonths();
        yieldCashOutflowItems.clear();
        for (String month : months) {
            final int position = ACATUtility.returnMonthPositionInOriginalList(month);
            final double value = getMonthValue(yieldCashFlow, position);
            yieldCashOutflowItems.add(new CashFlowItem(month, String.valueOf(value)));
        }

        final int distance = ACATUtility.getMonthIndex(cropACAT.firstExpenseMonth);
        Collections.rotate(yieldCashOutflowItems, -distance);

        view.loadYieldCashOutflow();

    }

    private void generateNetCashFlow() {
        final String[] months = new DateFormatSymbols().getShortMonths();
        netCashOutflowItems.clear();
        for (String month : months) {
            netCashOutflowItems.add(new CashFlowItem(month, String.valueOf(0)));
        }

        final int distance = ACATUtility.getMonthIndex(cropACAT.firstExpenseMonth);
        Collections.rotate(netCashOutflowItems, -distance);

        view.loadNetCashOutflow();
        refreshNetCashOutflow();
    }
    private void refreshNetCashOutflow() {
        final int length = yieldCashOutflowItems.size();
        for (int i = 0; i < length; i++) {
            final CashFlowItem currentYieldMonth = yieldCashOutflowItems.get(i);
            final CashFlowItem currentNetMonth = netCashOutflowItems.get(i);

            final double currentYieldMonthValue = parse(currentYieldMonth.value);
            double previousNetMonthValue = 0;
            if (i > 0) {
                final CashFlowItem previousNetMonth = netCashOutflowItems.get(i - 1);
                previousNetMonthValue = parse(previousNetMonth.value);
            }
            double currentNetMonthValue = previousNetMonthValue + currentYieldMonthValue;
            currentNetMonth.value = String.valueOf(currentNetMonthValue);
        }
    }

    private double parse(@Nullable String value) {
        if (value == null) return 0;
        if (value.trim().isEmpty()) return 0;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private double getMonthValue(@NonNull CashFlow cashFlow, int position) {
        switch (position) {
            case 0: return cashFlow.january;
            case 1: return cashFlow.february;
            case 2: return cashFlow.march;
            case 3: return cashFlow.april;
            case 4: return cashFlow.may;
            case 5: return cashFlow.june;
            case 6: return cashFlow.july;
            case 7: return cashFlow.august;
            case 8: return cashFlow.september;
            case 9: return cashFlow.october;
            case 10: return cashFlow.november;
            case 11: return cashFlow.december;
            default: return 0;
        }
    }

    private void setMonthValue(@NonNull CashFlow cashFlow, double value, int position) {
        switch (position) {
            case 0:
                cashFlow.january = value;
                break;
            case 1:
                cashFlow.february = value;
                break;
            case 2:
                cashFlow.march = value;
                break;
            case 3:
                cashFlow.april = value;
                break;
            case 4:
                cashFlow.may = value;
                break;
            case 5:
                cashFlow.june = value;
                break;
            case 6:
                cashFlow.july = value;
                break;
            case 7:
                cashFlow.august = value;
                break;
            case 8:
                cashFlow.september = value;
                break;
            case 9:
                cashFlow.october = value;
                break;
            case 10:
                cashFlow.november = value;
                break;
            case 11:
                cashFlow.december = value;
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void loadYieldConsumption(@Nullable String id, boolean estimated) {
        yieldConsumptionLocal.get(id == null ? "-" : id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (view == null) return;
                    view.toggleYieldConsumptionInputs(!data.empty());

                    if (data.empty()) {
                        yieldConsumption = null;
                    } else {
                        yieldConsumption = data.get();

                        view.setOwnCons(estimated ? yieldConsumption.estimatedOwnCons : yieldConsumption.actualOwnCons, estimated);
                        view.setSeedRes(estimated ? yieldConsumption.estimatedSeedReserve : yieldConsumption.actualSeedReserve, estimated);
                        view.setForMarket(estimated ? yieldConsumption.estimatedForMarket : yieldConsumption.actualForMarket, estimated);
                        view.setYieldConsumptionRemark(yieldConsumption.remark);
                    }
                });
    }

    private void uploadYieldConsumption(@Nullable YieldConsumption consumption, @NonNull ACATEstimatedYieldDto dto) {
        if (consumption == null) return;

        consumption.estimatedOwnCons = parse(dto.estimatedOwnCons);
        consumption.estimatedSeedReserve = parse(dto.estimatedSeedRes);
        consumption.estimatedForMarket = parse(dto.estimatedForMarket);

        consumption.actualOwnCons = parse(dto.actualOwnCons);
        consumption.actualSeedReserve = parse(dto.actualSeedRes);
        consumption.actualForMarket = parse(dto.actualForMarket);

        consumption.remark = dto.yieldConsumptionRemark;
    }

    @Override
    public void attachView(ACATEstimatedYieldContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public ACATEstimatedYieldContract.View getView() {
        return view;
    }
}

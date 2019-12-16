package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import android.annotation.SuppressLint;
import android.util.Log;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


/**
 * Created by abuti on 6/10/2018.
 */

public class ACATCostUpdater {
    @Inject
    ACATCostItemUpdater acatCostItemUpdater;

    @Inject
    ACATCostSectionUpdater acatCostSectionUpdater;

    @Inject CropACATUpdater cropACATUpdater;
    @Inject ACATApplicationUpdater acatApplicationUpdater;
    @Inject CashFlowLocalSource cashFlowLocalSource;

    @Inject UpdaterUtility utilities;

    @Inject SchedulersProvider schedulers;

    CashFlow oldItemCashFlow;
    Double oldTotal = 0.0;
    ACATItemResponse acatItemResponse;
    Double sum = 0.0;
    String sectionToBeUpdatedId;
    boolean processed;

    ACATCostSection Dsection;
    ACATCostSection DparentSection;
    ACATCostSectionResponse acatCostSectionResponse;


    private ACATApplicationResponse acatApplicationResponse;

    public ACATCostUpdater() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }


    private Observable<ACATApplicationResponse> uploadACATApplication(CropACATDto cropACATDto){
        final ACATApplicationDto acatApplicationDto = new ACATApplicationDto();

        utilities.returnACATApplication(cropACATDto.cropACAT.acatApplicationID)
                .subscribe(response -> acatApplicationDto.acatApplication = response);

        ACATApplicationResponse updatedACATApp = aggregateEstimatedValuesToACATApp(acatApplicationDto.acatApplication,
                cropACATDto.cropACAT.firstExpenseMonth);

        acatApplicationDto.acatApplication = updatedACATApp.acatApplication;
        acatApplicationDto.estimatedNetCashFlow = updatedACATApp.estimatedNetCashFlow;
        return acatApplicationUpdater.uploadEstimatedValuesForACATApplication(acatApplicationDto)
//                .subscribeOn(schedulers.background())
//                .observeOn(schedulers.ui())
                .concatMap(response -> {
                    acatApplicationResponse = response;
                    return Observable.just(acatApplicationResponse);
                    //return Completable.complete();

                });


    }

    private Observable<ACATApplicationResponse> uploadActualACATApplication(CropACATDto cropACATDto) {
        final ACATApplicationDto acatApplicationDto = new ACATApplicationDto();

        utilities.returnACATApplication(cropACATDto.cropACAT.acatApplicationID)
                .subscribe(response -> acatApplicationDto.acatApplication = response);

        ACATApplicationResponse updatedACATApp = aggregateActualValuesToACATApp(acatApplicationDto.acatApplication,
                cropACATDto.cropACAT.firstExpenseMonth);

        acatApplicationDto.acatApplication = updatedACATApp.acatApplication;
        acatApplicationDto.actualNetCashFlow = updatedACATApp.actualNetCashFlow;
        return acatApplicationUpdater.uploadActualValuesForACATApplication(acatApplicationDto)
//                .subscribeOn(schedulers.background())
//                .observeOn(schedulers.ui())
                .concatMap(response -> {
                    acatApplicationResponse = response;
                    return Observable.just(acatApplicationResponse);
                    //return Completable.complete();

                });
    }
    public Observable<ACATApplicationResponse> computeEstimatedNetIncomeAndCashFlow(String cropACATId, double forMarket, double unitPrice) throws Exception {
        final CropACATDto cropACATDto = new CropACATDto();
        CropACATResponse updatedCropACAT = new CropACATResponse();
        utilities.returnCropACAT(cropACATId)
                .subscribe(response -> {
                    cropACATDto.cropACAT = response;
                });


//        utilities.returnRevenueSection(cropACATDto.cropACAT.revenueSectionId)
//                .subscribe(pageResponse ->{
//                    cropACATDto.cropACAT.estimatedTotalRevenue = pageResponse.estimatedProbRevenue;
//                });
//
        cropACATDto.cropACAT.estimatedTotalRevenue = forMarket * unitPrice;
        Log.e("For Market", String.valueOf(forMarket));
        Log.e("Unit Price", String.valueOf(unitPrice));
        try {
            updatedCropACAT = aggregateEstimatedValuesToCropACAT(null,cropACATDto.cropACAT, cropACATDto.cropACAT.firstExpenseMonth);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Revenue section is empty.");
        }


        cropACATDto.cropACAT = updatedCropACAT.cropACAT;
        cropACATDto.estimatedNetCashFlow = updatedCropACAT.estimatedNetCashFlow;


        //cropACATUpdater.updateEstimatedValuesForCropACAT(cropACATDto);


        return cropACATUpdater.uploadEstimatedValuesForCropACAT(cropACATDto)
               // .andThen(uploadACATApplication(cropACATDto))
//                .subscribeOn(schedulers.background())
//                .observeOn(schedulers.ui())
                .flatMap(cropACATResponse -> {
                    return uploadACATApplication(cropACATDto);
//                            .subscribeOn(schedulers.background())
//                            .observeOn(schedulers.ui())
//                            .subscribe(appResponse -> {
//                                return Observable.just(appResponse);
//                            });
                });

    }

    public Observable<ACATApplicationResponse> computeActualNetIncomeAndCashFlow(String cropACATId, double forMarket, double unitPrice) throws Exception {
        final CropACATDto cropACATDto = new CropACATDto();
        CropACATResponse updatedCropACAT = new CropACATResponse();
        utilities.returnCropACAT(cropACATId)
                .subscribe(response -> {
                    cropACATDto.cropACAT = response;
                });


//        utilities.returnRevenueSection(cropACATDto.cropACAT.revenueSectionId)
//                .subscribe(response ->{
//                    cropACATDto.cropACAT.estimatedTotalRevenue = response.estimatedProbRevenue;
//                });
//
        cropACATDto.cropACAT.actualTotalRevenue = forMarket * unitPrice;
        Log.e("For Market", String.valueOf(forMarket));
        Log.e("Unit Price", String.valueOf(unitPrice));
        try {
            updatedCropACAT = aggregateActualValuesToCropACAT(null,cropACATDto.cropACAT, cropACATDto.cropACAT.firstExpenseMonth);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Revenue section is empty.");
        }


        cropACATDto.cropACAT = updatedCropACAT.cropACAT;
        cropACATDto.actualNetCashFlow = updatedCropACAT.actualNetCashFlow;

        //cropACATUpdater.updateEstimatedValuesForCropACAT(cropACATDto);


        return cropACATUpdater.uploadActualValuesForCropACAT(cropACATDto)
                // .andThen(uploadACATApplication(cropACATDto))
//                .subscribeOn(schedulers.background())
//                .observeOn(schedulers.ui())
                .flatMap(cropACATResponse -> {
//                    return uploadACATApplication(cropACATDto);
                    return uploadActualACATApplication(cropACATDto);
//                            .subscribeOn(schedulers.background())
//                            .observeOn(schedulers.ui())
//                            .subscribe(appResponse -> {
//                                return Observable.just(appResponse);
//                            });
                });

    }



    private Observable<String> returnNextCostSectionId2(ACATCostSectionDto acatCostSectionDto){
        sectionToBeUpdatedId = acatCostSectionDto.section.parentSectionID;
        if (acatCostSectionDto.section.parentSectionID == null)
            return Observable.just("");
        else
            return Observable.just( acatCostSectionDto.section.parentSectionID);
    }



    @SuppressLint("CheckResult")
    private Observable<ACATCostSectionResponse> uploadEstimatedValuesForRelevantACATCostSections(ACATItemDto acatItemDto){
        List<String> sectionIds = new ArrayList();


        utilities.returnCostSection(acatItemDto.sectionId)
                .subscribe(response -> {
                    final ACATCostSection section = new ACATCostSection();
                    section._id = response._id;
                    section.parentSectionID = response.parentSectionID;
                    sectionIds.add(section._id);
                    while (section.parentSectionID != null) {
                        sectionIds.add(section.parentSectionID);
                        utilities.returnCostSection(section.parentSectionID)
                                .subscribe(response2 -> {
                                    section._id = response2._id;
                                    section.parentSectionID = response2.parentSectionID;
                                                                    });
                    }
                });

        //Update the sections
        Observable.fromIterable(sectionIds)
                .concatMap(nextSectionId -> Observable.just(nextSectionId))
                .subscribe(nextSectionId -> {
                    if (nextSectionId != null) {
                        final ACATCostSectionDto acatCostSectionDto = new ACATCostSectionDto();
                        utilities.returnCostSection(nextSectionId)
                                .subscribe(response -> {
                                    acatCostSectionDto.section = response;
                                });
                        utilities.returnSectionCashFlow(nextSectionId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE)
                                .subscribe(response -> {
                                    acatCostSectionDto.estimatedCashFlow = response;
                                });
                        if (acatCostSectionDto.section.title.compareTo("Seed")==0) {
                            if (!acatItemDto.variety.isEmpty())
                                acatCostSectionDto.section.variety = acatItemDto.variety;

                            if (acatItemDto.seedSource.size() > 0) {
                                acatCostSectionDto.section.seedSources = new ArrayList<>();
                                acatCostSectionDto.section.seedSources.addAll(acatItemDto.seedSource);
                            }
                        }

                        ACATCostSectionResponse updatedSection = aggregateEstimatedValuesToSections(acatItemResponse,
                                acatCostSectionDto.section, acatCostSectionDto.estimatedCashFlow, acatItemDto.firstExpenseMonth);

                        acatCostSectionDto.section = updatedSection.sections.get(0);

                        acatCostSectionDto.estimatedCashFlow = updatedSection.estimatedCashFlows.get(0);
                        acatCostSectionDto.acatApplicationId = acatItemDto.acatApplicationId; // TODO: Added to fix issue. on August 21/2018

                        processed = true;

                        acatCostSectionUpdater.uploadEstimatedValuesForACATCostSection(acatCostSectionDto)
                                //.andThen(returnNextCostSectionId(acatCostSectionDto))
                                .subscribe(costResponse -> acatCostSectionResponse = costResponse);
                    }

                });
        return Observable.just(acatCostSectionResponse);

    }

    @SuppressLint("CheckResult")
    private Observable<ACATCostSectionResponse> uploadActualValuesForRelevantACATCostSections(ACATItemDto acatItemDto){
        List<String> sectionIds = new ArrayList();


        utilities.returnCostSection(acatItemDto.sectionId)
                .subscribe(response -> {
                    final ACATCostSection section = new ACATCostSection();
                    section._id = response._id;
                    section.parentSectionID = response.parentSectionID;
                    sectionIds.add(section._id);
                    while (section.parentSectionID != null) {
                        sectionIds.add(section.parentSectionID);
                        utilities.returnCostSection(section.parentSectionID)
                                .subscribe(response2 -> {
                                    section._id = response2._id;
                                    section.parentSectionID = response2.parentSectionID;
                                });
                    }
                });

        //Update the sections
        Observable.fromIterable(sectionIds)
                .concatMap(nextSectionId -> Observable.just(nextSectionId))
                .subscribe(nextSectionId -> {
                    if (nextSectionId != null) {
                        final ACATCostSectionDto acatCostSectionDto = new ACATCostSectionDto();
                        utilities.returnCostSection(nextSectionId)
                                .subscribe(response -> {
                                    acatCostSectionDto.section = response;
                                });
                        utilities.returnSectionCashFlow(nextSectionId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE)
                                .subscribe(response -> {
                                    acatCostSectionDto.actualCashFlow = response;
                                });
                        if (acatCostSectionDto.section.title.compareTo("Seed")==0) {
                            if (!acatItemDto.variety.isEmpty())
                                acatCostSectionDto.section.variety = acatItemDto.variety;

                            if (acatItemDto.seedSource.size() > 0) {
                                acatCostSectionDto.section.seedSources = new ArrayList<>();
                                acatCostSectionDto.section.seedSources.addAll(acatItemDto.seedSource);
                            }
                        }

                        ACATCostSectionResponse updatedSection = aggregateActualValuesToSections(acatItemResponse,
                                acatCostSectionDto.section, acatCostSectionDto.actualCashFlow, acatItemDto.firstExpenseMonth);

                        acatCostSectionDto.section = updatedSection.sections.get(0);

                        acatCostSectionDto.actualCashFlow = updatedSection.actualCashFlows.get(0);
                        acatCostSectionDto.acatApplicationId = acatItemDto.acatApplicationId; // TODO: Added to fix issue. on August 21/2018

                        processed = true;

                        acatCostSectionUpdater.uploadActualValuesForACATCostSection(acatCostSectionDto)
                                //.andThen(returnNextCostSectionId(acatCostSectionDto))
                                .subscribe(costResponse -> acatCostSectionResponse = costResponse);
                    }

                });
        return Observable.just(acatCostSectionResponse);

    }

    public Completable updateEstimatedValueForACATItem(ACATItemDto acatItemDto) {
        //Get the stored cash flow...
        utilities.returnCashFlow(acatItemDto.acatItemId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.ITEM_CASH_FLOW)
                .subscribe(response -> {
                    oldItemCashFlow = response;
                });

        utilities.returnItemTotal(acatItemDto.acatItemId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE)
                .subscribe(response -> {
                    oldTotal = response;
                });

        //ACATItemResponse acatItemResponse = acatCostItemUpdater.updateEstimatedValuesForACATItem(acatItemDto);

        return acatCostItemUpdater.uploadEstimatedValuesForACATItem(acatItemDto)
                .flatMapCompletable(updated -> {
                    acatItemResponse = updated;
                    return uploadNextOnEstimated(acatItemDto);
                });
    }

    public Completable updateActualValueForACATItem(ACATItemDto acatItemDto) {
        utilities.returnCashFlow(acatItemDto.acatItemId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.ITEM_CASH_FLOW)
                .subscribe(response -> {
                    oldItemCashFlow = response;
                });

        utilities.returnItemTotal(acatItemDto.acatItemId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE)
                .subscribe(response -> {
                    oldTotal = response;
                });

        //ACATItemResponse acatItemResponse = acatCostItemUpdater.updateEstimatedValuesForACATItem(acatItemDto);

        return acatCostItemUpdater.uploadActualValuesForACATItem(acatItemDto)
                .flatMapCompletable(updated -> {
                    acatItemResponse = updated;
                    return uploadNextOnActual(acatItemDto);
                });
    }

    @SuppressLint("CheckResult")
    private Completable uploadNextOnEstimated(@NonNull ACATItemDto acatItemDto) {

        uploadEstimatedValuesForRelevantACATCostSections(acatItemDto)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(acatCostSectionResponse -> {
            //.flatMap(() -> {
                    //Update the cropACAT ...............................................................
                    final CropACATDto cropACATDto = new CropACATDto();
                    cropACATDto.acatApplicationId = acatItemDto.acatApplicationId;

                    utilities.returnCropACAT(acatItemDto.cropACATId)
                            .subscribe(response -> {
                                cropACATDto.cropACAT = response;
                            });

                    utilities.returnCropACATCashFlow(cropACATDto.cropACAT._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE)
                            .subscribe(response -> {
                                cropACATDto.estimatedNetCashFlow = response;
                            });

                    CropACATResponse updatedCropACAT = aggregateEstimatedValuesToCropACAT(acatItemResponse,
                            cropACATDto.cropACAT, acatItemDto.firstExpenseMonth);

                    cropACATDto.cropACAT = updatedCropACAT.cropACAT;
                    cropACATDto.estimatedNetCashFlow = updatedCropACAT.estimatedNetCashFlow;


                    //cropACATUpdater.updateEstimatedValuesForCropACAT(cropACATDto);
                    cropACATUpdater.uploadEstimatedValuesForCropACAT(cropACATDto)
                            //.andThen(acatApplicationUpdater.uploadEstimatedValuesForACATApplication(acatApplicationDto))
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())

                            .subscribe(cropACATResponse -> {
                                //Update the ACAT Application................................................................
                                final ACATApplicationDto acatApplicationDto = new ACATApplicationDto();

                                utilities.returnACATApplication(cropACATResponse.cropACAT.acatApplicationID) // revert this back
                                        .subscribe(response -> {
                                            acatApplicationDto.acatApplication = response;
                                        });

                                ACATApplicationResponse updatedACATApp = aggregateEstimatedValuesToACATApp(acatApplicationDto.acatApplication,
                                        acatItemDto.firstExpenseMonth);

                                acatApplicationDto.acatApplication = updatedACATApp.acatApplication;
                                acatApplicationDto.estimatedNetCashFlow = updatedACATApp.estimatedNetCashFlow;
                                acatApplicationUpdater.uploadEstimatedValuesForACATApplication(acatApplicationDto)
                                          .subscribeOn(schedulers.background())
                                          .observeOn(schedulers.ui())
                                          .subscribe(response -> {
                                              acatApplicationResponse = response;
                                          });
                                    });
        });
        return Completable.complete();
    }

    @SuppressLint("CheckResult")
    private Completable uploadNextOnActual(@NonNull ACATItemDto acatItemDto) {

        uploadActualValuesForRelevantACATCostSections(acatItemDto)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(acatCostSectionResponse -> {
                    //.flatMap(() -> {
                    //Update the cropACAT ...............................................................
                    final CropACATDto cropACATDto = new CropACATDto();
                    cropACATDto.acatApplicationId = acatItemDto.acatApplicationId;

                    utilities.returnCropACAT(acatItemDto.cropACATId)
                            .subscribe(response -> {
                                cropACATDto.cropACAT = response;
                            });

                    utilities.returnCropACATCashFlow(cropACATDto.cropACAT._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE)
                            .subscribe(response -> {
                                cropACATDto.actualNetCashFlow = response;
                            });

                    CropACATResponse updatedCropACAT = aggregateActualValuesToCropACAT(acatItemResponse,
                            cropACATDto.cropACAT, acatItemDto.firstExpenseMonth);

                    cropACATDto.cropACAT = updatedCropACAT.cropACAT;
                    cropACATDto.actualNetCashFlow = updatedCropACAT.actualNetCashFlow;


                    //cropACATUpdater.updateEstimatedValuesForCropACAT(cropACATDto);
                    cropACATUpdater.uploadActualValuesForCropACAT(cropACATDto)
                            //.andThen(acatApplicationUpdater.uploadEstimatedValuesForACATApplication(acatApplicationDto))
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())

                            .subscribe(cropACATResponse -> {
                                //Update the ACAT Application................................................................
                                final ACATApplicationDto acatApplicationDto = new ACATApplicationDto();

                                utilities.returnACATApplication(acatItemDto.acatApplicationId)
                                        .subscribe(response -> {
                                            acatApplicationDto.acatApplication = response;
                                        });

                                ACATApplicationResponse updatedACATApp = aggregateActualValuesToACATApp(acatApplicationDto.acatApplication,
                                        acatItemDto.firstExpenseMonth);

                                acatApplicationDto.acatApplication = updatedACATApp.acatApplication;
                                acatApplicationDto.actualNetCashFlow = updatedACATApp.actualNetCashFlow;
                                acatApplicationUpdater.uploadActualValuesForACATApplication(acatApplicationDto)
                                        .subscribeOn(schedulers.background())
                                        .observeOn(schedulers.ui())
                                        .subscribe(response -> {
                                            acatApplicationResponse = response;
                                        });
                            });
                });
        return Completable.complete();
    }

    @SuppressLint("CheckResult")
    private void computeSectionTotal(String sectionId, String type){
        ACATCostSection section = new ACATCostSection();
        sum = 0.0;

        utilities.returnCostSection(sectionId)
                .subscribe(response -> {
                    if (response.subSectionIDs.size() == 0) { //This means the section is an immediate parent of specific items
                        utilities.returnACATItems(sectionId)
                                .subscribe(acatItems -> {
                                    for (int i=0;i<acatItems.size();i++){
                                        utilities.returnItemTotal(acatItems.get(i)._id, type)
                                                .subscribe(total -> sum = sum + total);
                                    }
                                });
                    }
                    else{
                        utilities.returnSubSections(sectionId)
                                .subscribe(sections -> {
                                    for (int i=0;i<sections.size();i++){
                                        if (type == ACATItemValueHelper.ESTIMATED_VALUE_TYPE)
                                            sum += sections.get(i).estimatedSubTotal;
                                        else
                                            sum += sections.get(i).actualSubTotal;

                                    }
                                });
                    }
                    //return sum;
                });
                //.subscribeOn(schedulers.background())
                //.observeOn(schedulers.ui())
                //.subscribe();


    }
    private ACATCostSectionResponse aggregateEstimatedValuesToSections (ACATItemResponse acatItemResponse,
                                                                        ACATCostSection section, CashFlow sectionCashFlow,
                                                                        String firstExpenseMonth) {

        final ACATCostSectionResponse acatCostSectionResponse = new ACATCostSectionResponse();
        acatCostSectionResponse.sections = new ArrayList<>();
        acatCostSectionResponse.estimatedCashFlows = new ArrayList<>();

        //Update section values to be affected
//        section.estimatedSubTotal = section.estimatedSubTotal - oldTotal;
//        section.estimatedSubTotal += acatItemResponse.estimatedAcatItemValue.totalPrice;
        computeSectionTotal(section._id,ACATItemValueHelper.ESTIMATED_VALUE_TYPE); //we can have another helper for sections
        section.estimatedSubTotal = sum;

        CashFlow estimatedSectionCashFlow = utilities.computeSectionCumulativeCashFlow(acatItemResponse.estimatedCashFlow,
                sectionCashFlow, oldItemCashFlow, firstExpenseMonth);

        estimatedSectionCashFlow.referenceId = sectionCashFlow.referenceId;
        estimatedSectionCashFlow.type = sectionCashFlow.type;
        estimatedSectionCashFlow.classification = sectionCashFlow.classification;

        acatCostSectionResponse.sections.add(section);
        acatCostSectionResponse.estimatedCashFlows.add(estimatedSectionCashFlow);

        return acatCostSectionResponse;
    }

    private ACATCostSectionResponse aggregateActualValuesToSections (ACATItemResponse acatItemResponse,
                                                                        ACATCostSection section, CashFlow sectionCashFlow,
                                                                        String firstExpenseMonth) {

        final ACATCostSectionResponse acatCostSectionResponse = new ACATCostSectionResponse();
        acatCostSectionResponse.sections = new ArrayList<>();
        acatCostSectionResponse.actualCashFlows = new ArrayList<>();

        //Update section values to be affected
//        section.estimatedSubTotal = section.estimatedSubTotal - oldTotal;
//        section.estimatedSubTotal += acatItemResponse.estimatedAcatItemValue.totalPrice;
        computeSectionTotal(section._id,ACATItemValueHelper.ACTUAL_VALUE_TYPE); //we can have another helper for sections
        section.actualSubTotal = sum;

        CashFlow actualSectionCashFlow = utilities.computeSectionCumulativeCashFlow(acatItemResponse.actualCashFlow,
                sectionCashFlow, oldItemCashFlow, firstExpenseMonth);

        actualSectionCashFlow.referenceId = sectionCashFlow.referenceId;
        actualSectionCashFlow.type = sectionCashFlow.type;
        actualSectionCashFlow.classification = sectionCashFlow.classification;

        acatCostSectionResponse.sections.add(section);
        acatCostSectionResponse.actualCashFlows.add(actualSectionCashFlow);

        return acatCostSectionResponse;
    }

    @SuppressLint("CheckResult")
    private CropACATResponse aggregateEstimatedValuesToCropACAT(@Nullable ACATItemResponse acatItemResponse, CropACAT cropACAT,
                                                                String firstExpenseMonth) {
        //Get the estimated cumulative Cost Section CashFlow
        //1. Get the estimated cash flow of cost section
        //2. Get the estimated Cash flow of revenue section
        //3. Compute the cumulative cash flow
        final CropACATResponse cropACATResponse = new CropACATResponse();
        if (acatItemResponse != null) {
//            cropACAT.estimatedTotalCost = cropACAT.estimatedTotalCost - oldTotal;
//            cropACAT.estimatedTotalCost += acatItemResponse.estimatedAcatItemValue.totalPrice;

        }
        utilities.returnCostSection(cropACAT.costSectionId)
                .subscribe(section -> {
                    cropACAT.estimatedTotalCost = section.estimatedSubTotal;
                });

        cropACAT.estimatedNetIncome = cropACAT.estimatedTotalRevenue - cropACAT.estimatedTotalCost;


        cropACATResponse.cropACAT = cropACAT;

        CashFlow responseCashFlow = new CashFlow();


        cashFlowLocalSource.get(cropACAT.costSectionId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                .subscribe(costCashFlow -> {
                    try {
                    cashFlowLocalSource.get(cropACAT.revenueSectionId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                            .subscribe(revenueCashFlow -> {
                                if (revenueCashFlow.size() > 0) {
                                    List<Double> costCashFlowValues = utilities.returnCashFlowValues(costCashFlow.get(0));
                                    List<Double> revenueCashFlowValues = utilities.returnCashFlowValues(revenueCashFlow.get(0));
                                    CashFlow estimatedCropACATCashflow = utilities.computeCumulativeCropACATCashFlow(revenueCashFlowValues,
                                            costCashFlowValues, firstExpenseMonth);
                                    estimatedCropACATCashflow.referenceId = cropACAT._id;
                                    estimatedCropACATCashflow.type = CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
                                    estimatedCropACATCashflow.classification = CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE;
                                    cropACATResponse.estimatedNetCashFlow = estimatedCropACATCashflow;
                                }
                            });
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Revenue Cash flow is empty.");
                    }
                });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            return cropACATResponse;


    }

    @SuppressLint("CheckResult")
    private CropACATResponse aggregateActualValuesToCropACAT(@Nullable ACATItemResponse acatItemResponse, CropACAT cropACAT,
                                                                String firstExpenseMonth) {
        //Get the estimated cumulative Cost Section CashFlow
        //1. Get the estimated cash flow of cost section
        //2. Get the estimated Cash flow of revenue section
        //3. Compute the cumulative cash flow
        final CropACATResponse cropACATResponse = new CropACATResponse();
        if (acatItemResponse != null) {
//            cropACAT.estimatedTotalCost = cropACAT.estimatedTotalCost - oldTotal;
//            cropACAT.estimatedTotalCost += acatItemResponse.estimatedAcatItemValue.totalPrice;

        }
        utilities.returnCostSection(cropACAT.costSectionId)
                .subscribe(section -> {
                    cropACAT.actualTotalCost = section.actualSubTotal;
                });

        cropACAT.actualNetIncome = cropACAT.actualTotalRevenue - cropACAT.actualTotalCost;


        cropACATResponse.cropACAT = cropACAT;

        CashFlow responseCashFlow = new CashFlow();


        cashFlowLocalSource.get(cropACAT.costSectionId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                .subscribe(costCashFlow -> {
                    try {
                        cashFlowLocalSource.get(cropACAT.revenueSectionId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                                .subscribe(revenueCashFlow -> {
                                    if (revenueCashFlow.size() > 0) {
                                        List<Double> costCashFlowValues = utilities.returnCashFlowValues(costCashFlow.get(0));
                                        List<Double> revenueCashFlowValues = utilities.returnCashFlowValues(revenueCashFlow.get(0));
                                        CashFlow actualCropACATCashflow = utilities.computeCumulativeCropACATCashFlow(revenueCashFlowValues,
                                                costCashFlowValues, firstExpenseMonth);
                                        actualCropACATCashflow.referenceId = cropACAT._id;
                                        actualCropACATCashflow.type = CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;
                                        actualCropACATCashflow.classification = CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE;
                                        cropACATResponse.actualNetCashFlow = actualCropACATCashflow;
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Revenue Cash flow is empty.");
                    }
                });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return cropACATResponse;


    }

    @SuppressLint("CheckResult")
    private ACATApplicationResponse aggregateEstimatedValuesToACATApp(ACATApplication acatApplication, String firstExpenseMonth){
        //
        //1. Get All cropACATs queried by the acat application id
        //2. Sum up their total cost and assign it to the acat application's total cost
        //3. Sum up their total revenue and assign it to the acat application's total revenue
        //4. Assign the Difference between total revenue and total cost as a net income
        //3. Sum up each month of their cashflow and construct a cash flow to assign it as a cashflow of the acat application

        final ACATApplicationResponse acatApplicationResponse = new ACATApplicationResponse();

        acatApplicationResponse.acatApplication = acatApplication;

        //1. Get All cropACATs queried by the acat application id
        final List<CropACAT> cropACATs = new ArrayList<>();
        utilities.returnCropACATByApp(acatApplication._id)
                .subscribe(response -> {
                    cropACATs.addAll(response);
                });

        //2. Compute total cost, total revenue and net income for the application
        double estimatedACATAppTotalCost = 0.0;
        double estimatedACATAppTotalRevenue = 0.0;
        double estimatedACATAppNetIncome;

        for (int i = 0;i < cropACATs.size(); i++){
            estimatedACATAppTotalCost += cropACATs.get(i).estimatedTotalCost;
            estimatedACATAppTotalRevenue += cropACATs.get(i).estimatedTotalRevenue;
        }

        estimatedACATAppNetIncome =  estimatedACATAppTotalRevenue - estimatedACATAppTotalCost;

        acatApplicationResponse.acatApplication.estimatedTotalCost = estimatedACATAppTotalCost;
        acatApplicationResponse.acatApplication.estimatedTotalRevenue = estimatedACATAppTotalRevenue;
        acatApplicationResponse.acatApplication.estimatedNetIncome = estimatedACATAppNetIncome;

        //3. Construct/compute the net cashflow of the ACAT application
        List<CashFlow> estimatedCropACATNetCashFlows = new ArrayList<>();
        for (int i = 0; i < cropACATs.size(); i++) {
            utilities.returnCashFlow(cropACATs.get(i)._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE)
                    .subscribe(response -> {
                        estimatedCropACATNetCashFlows.add(response);
                    });
        }

        CashFlow estimatedACATAppNetCashFlow = utilities.computeSumOfCashFlows(estimatedCropACATNetCashFlows);

        estimatedACATAppNetCashFlow.referenceId = acatApplication._id;
        estimatedACATAppNetCashFlow.type = CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
        estimatedACATAppNetCashFlow.classification = CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE;

        acatApplicationResponse.estimatedNetCashFlow = estimatedACATAppNetCashFlow;

        return acatApplicationResponse;

    }

    @SuppressLint("CheckResult")
    private ACATApplicationResponse aggregateActualValuesToACATApp(ACATApplication acatApplication, String firstExpenseMonth){
        //
        //1. Get All cropACATs queried by the acat application id
        //2. Sum up their total cost and assign it to the acat application's total cost
        //3. Sum up their total revenue and assign it to the acat application's total revenue
        //4. Assign the Difference between total revenue and total cost as a net income
        //3. Sum up each month of their cashflow and construct a cash flow to assign it as a cashflow of the acat application

        final ACATApplicationResponse acatApplicationResponse = new ACATApplicationResponse();

        acatApplicationResponse.acatApplication = acatApplication;

        //1. Get All cropACATs queried by the acat application id
        final List<CropACAT> cropACATs = new ArrayList<>();
        utilities.returnCropACATByApp(acatApplication._id)
                .subscribe(response -> {
                    cropACATs.addAll(response);
                });

        //2. Compute total cost, total revenue and net income for the application
        double actualACATAppTotalCost = 0.0;
        double actualACATAppTotalRevenue = 0.0;
        double actualACATAppNetIncome;

        for (int i = 0;i < cropACATs.size(); i++){
            actualACATAppTotalCost += cropACATs.get(i).actualTotalCost;
            actualACATAppTotalRevenue += cropACATs.get(i).actualTotalRevenue;
        }

        actualACATAppNetIncome =  actualACATAppTotalRevenue - actualACATAppTotalCost;

        acatApplicationResponse.acatApplication.actualTotalCost = actualACATAppTotalCost;
        acatApplicationResponse.acatApplication.actualTotalRevenue = actualACATAppTotalRevenue;
        acatApplicationResponse.acatApplication.actualNetIncome = actualACATAppNetIncome;

        //3. Construct/compute the net cashflow of the ACAT application
        List<CashFlow> actualCropACATNetCashFlows = new ArrayList<>();
        for (int i = 0; i < cropACATs.size(); i++) {
            utilities.returnCashFlow(cropACATs.get(i)._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE)
                    .subscribe(response -> {
                        actualCropACATNetCashFlows.add(response);
                    });
        }

        CashFlow actualACATAppNetCashFlow = utilities.computeSumOfCashFlows(actualCropACATNetCashFlows);

        actualACATAppNetCashFlow.referenceId = acatApplication._id;
        actualACATAppNetCashFlow.type = CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;
        actualACATAppNetCashFlow.classification = CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE;

        acatApplicationResponse.actualNetCashFlow = actualACATAppNetCashFlow;

        return acatApplicationResponse;

    }


}

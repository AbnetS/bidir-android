package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemHelper;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemRequest;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemRemoteSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation.CashFlowData;

import org.joda.time.DateTime;

import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by abuti on 6/8/2018.
 */

public class ACATCostItemUpdater {
    @Inject ACATItemLocalSource acatItemLocalSource;
    @Inject ACATItemRemoteSource acatItemRemoteSource;

    @Inject ACATItemValueLocalSource acatItemValueLocalSource;

    @Inject CashFlowLocalSource cashFlowLocalSource;

    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    public ACATCostItemUpdater(){
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    public Observable<ACATItemResponse> uploadEstimatedValuesForACATItem(@NonNull ACATItemDto acatItemDto){
        ACATItemResponse responseBackUp = new ACATItemResponse();

        final ACATItemRequest request = new ACATItemRequest();

         request.acatApplicationId = acatItemDto.acatApplicationId;
        //Create the various local entities from the dto
        ACATItem acatItem = createACATItem(acatItemDto);
        ACATItemValue estimatedACATItemValue = createEstimatedACATItemValue(acatItemDto);
        CashFlow estimatedCashFlow = acatItemDto.estimatedCashFlow;
        estimatedCashFlow.referenceId = acatItemDto.acatItemId;

        return acatItemLocalSource.markForUpload(acatItem)
                .flatMap(savedACATItem -> {
                    request.acatItem = savedACATItem;
                    responseBackUp.acatItem = savedACATItem;
                    return acatItemValueLocalSource.put(estimatedACATItemValue);
                })
                .flatMap(savedACATItemValue -> {
                    request.estimatedAcatItemValue = savedACATItemValue;
                    responseBackUp.estimatedAcatItemValue = savedACATItemValue;
                    return cashFlowLocalSource.put(estimatedCashFlow);
                })
                .flatMap(savedCashFlow -> {
                    request.estimatedCashFlow = savedCashFlow;
                    responseBackUp.estimatedCashFlow = savedCashFlow;
                    return acatItemRemoteSource.updateACATItem(acatItemDto.acatItemId, request);
                })
                //.flatMapCompletable(pageResponse -> {
                .flatMap(response -> {
                    response.acatItem.uploaded = true;
                    response.acatItem.modified = false;
                    response.acatItem.updatedAt = new DateTime();

                    acatItemLocalSource.put(response.acatItem);
                    acatItemValueLocalSource.put(response.estimatedAcatItemValue);
                    cashFlowLocalSource.put(response.estimatedCashFlow);
                    return Observable.just(response);

                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatItemDto.acatApplicationId);
                        return Observable.just(responseBackUp);
                    }
                    else
                       throw new Exception();
                });

    }

    public Observable<ACATItemResponse> uploadActualValuesForACATItem(@NonNull ACATItemDto acatItemDto){
        ACATItemResponse responseBackUp = new ACATItemResponse();

        final ACATItemRequest request = new ACATItemRequest();

        request.acatApplicationId = acatItemDto.acatApplicationId;
        //Create the various local entities from the dto
        ACATItem acatItem = createACATItem(acatItemDto);
        ACATItemValue actualACATItemValue = createActualACATItemValue(acatItemDto);
        CashFlow actualCashFlow = acatItemDto.actualCashFlow;
        actualCashFlow.referenceId = acatItemDto.acatItemId;

        return acatItemLocalSource.markForUpload(acatItem)
                .flatMap(savedACATItem -> {
                    request.acatItem = savedACATItem;
                    responseBackUp.acatItem = savedACATItem;
                    return acatItemValueLocalSource.put(actualACATItemValue);
                })
                .flatMap(savedACATItemValue -> {
                    request.actualAcatItemValue = savedACATItemValue;
                    responseBackUp.actualAcatItemValue = savedACATItemValue;
                    return cashFlowLocalSource.put(actualCashFlow);
                })
                .flatMap(savedCashFlow -> {
                    request.actualCashFlow = savedCashFlow;
                    responseBackUp.actualCashFlow = savedCashFlow;
                    return acatItemRemoteSource.updateACATItem(acatItemDto.acatItemId, request);
                })
                //.flatMapCompletable(pageResponse -> {
                .flatMap(response -> {
                    response.acatItem.uploaded = true;
                    response.acatItem.modified = false;
                    response.acatItem.updatedAt = new DateTime();

                    acatItemLocalSource.put(response.acatItem);
                    acatItemValueLocalSource.put(response.actualAcatItemValue);
                    cashFlowLocalSource.put(response.actualCashFlow);
                    return Observable.just(response);

                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatItemDto.acatApplicationId);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });

    }


    public Observable<ACATItemResponse> uploadEstimatedRevenueForACATItem(@NonNull ACATItemDto acatItemDto){
        ACATItemResponse responseBackUp = new ACATItemResponse();

        final ACATItemRequest request = new ACATItemRequest();

        request.acatApplicationId = acatItemDto.acatApplicationId;
        //Create the various local entities from the dto
        ACATItem acatItem = createACATItem(acatItemDto);
        acatItem.category = ACATItemHelper.REVENUE_CATEGORY;
        ACATItemValue estimatedACATItemValue = createEstimatedACATItemValue(acatItemDto);
        CashFlow estimatedCashFlow = acatItemDto.estimatedCashFlow;
        estimatedCashFlow.referenceId = acatItemDto.acatItemId;

        return acatItemLocalSource.markForUpload(acatItem)
                .flatMap(savedACATItem -> {
                    request.acatItem = savedACATItem;
                    responseBackUp.acatItem = savedACATItem;
                    return acatItemValueLocalSource.put(estimatedACATItemValue);
                })
                .flatMap(savedACATItemValue -> {
                    request.estimatedAcatItemValue = savedACATItemValue;
                    responseBackUp.estimatedAcatItemValue = savedACATItemValue;
                    return cashFlowLocalSource.put(estimatedCashFlow);
                })
                .flatMap(savedCashFlow -> {
                    request.estimatedCashFlow = savedCashFlow;
                    responseBackUp.estimatedCashFlow = savedCashFlow;
                    return acatItemRemoteSource.updateRevenueItem(acatItemDto.acatItemId, request);
                })
                //.flatMapCompletable(response -> {
                .flatMap(response -> {
                    response.acatItem.uploaded = true;
                    response.acatItem.modified = false;
                    response.acatItem.updatedAt = new DateTime();

                    acatItemLocalSource.put(response.acatItem);
                    acatItemValueLocalSource.put(response.estimatedAcatItemValue);
                    cashFlowLocalSource.put(response.estimatedCashFlow);
                    return Observable.just(response);

                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatItemDto.acatApplicationId);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();

                });

    }

    public Observable<ACATItemResponse> uploadActualRevenueForACATItem(@NonNull ACATItemDto acatItemDto){
        ACATItemResponse responseBackUp = new ACATItemResponse();

        final ACATItemRequest request = new ACATItemRequest();

        request.acatApplicationId = acatItemDto.acatApplicationId;
        //Create the various local entities from the dto
        ACATItem acatItem = createACATItem(acatItemDto);
        acatItem.category = ACATItemHelper.REVENUE_CATEGORY;

        ACATItemValue actualACATItemValue = createActualACATItemValue(acatItemDto);
        CashFlow actualCashFlow = acatItemDto.actualCashFlow;
        actualCashFlow.referenceId = acatItemDto.acatItemId;

        return acatItemLocalSource.markForUpload(acatItem)
                .flatMap(savedACATItem -> {
                    request.acatItem = savedACATItem;
                    responseBackUp.acatItem = savedACATItem;
                    return acatItemValueLocalSource.put(actualACATItemValue);
                })
                .flatMap(savedACATItemValue -> {
                    request.actualAcatItemValue = savedACATItemValue;
                    responseBackUp.actualAcatItemValue = savedACATItemValue;
                    return cashFlowLocalSource.put(actualCashFlow);
                })
                .flatMap(savedCashFlow -> {
                    request.actualCashFlow = savedCashFlow;
                    responseBackUp.actualCashFlow = savedCashFlow;
                    return acatItemRemoteSource.updateRevenueItem(acatItemDto.acatItemId, request);
                })
                //.flatMapCompletable(response -> {
                .flatMap(response -> {
                    response.acatItem.uploaded = true;
                    response.acatItem.modified = false;
                    response.acatItem.updatedAt = new DateTime();

                    acatItemLocalSource.put(response.acatItem);
                    acatItemValueLocalSource.put(response.actualAcatItemValue);
                    cashFlowLocalSource.put(response.actualCashFlow);
                    return Observable.just(response);

                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatItemDto.acatApplicationId);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });

    }


    public ACATItemResponse updateEstimatedValuesForACATItem (@NonNull ACATItemDto acatItemDto){
        ACATItemResponse response = new ACATItemResponse();

        //Create the various local entities from the dto
        ACATItem acatItem = createACATItem(acatItemDto);
        ACATItemValue estimatedACATItemValue = createEstimatedACATItemValue(acatItemDto);
        CashFlow estimatedCashFlow = acatItemDto.estimatedCashFlow;
        estimatedCashFlow.referenceId = acatItemDto.acatItemId;

        acatItemLocalSource.put(acatItem);
        acatItemValueLocalSource.put(estimatedACATItemValue);
        cashFlowLocalSource.put(estimatedCashFlow);

        response.acatItem = acatItem;
        response.estimatedAcatItemValue = estimatedACATItemValue;
        response.estimatedCashFlow = estimatedCashFlow;

        return response;
    }

    public ACATItemResponse updateActualValuesForACATItem (@NonNull ACATItemDto acatItemDto){
        ACATItemResponse response = new ACATItemResponse();

        //Create the various local entities from the dto
        ACATItem acatItem = createACATItem(acatItemDto);
        ACATItemValue actualACATItemValue = createActualACATItemValue(acatItemDto);
        CashFlow actualCashFlow = acatItemDto.actualCashFlow;

        acatItemLocalSource.put(acatItem);
        acatItemValueLocalSource.put(actualACATItemValue);
        cashFlowLocalSource.put(actualCashFlow);

        response.acatItem = acatItem;
        response.actualAcatItemValue = actualACATItemValue;
        response.actualCashFlow = actualCashFlow;

        return response;
    }



    private ACATItem createACATItem (ACATItemDto acatItemDto){
        ACATItem acatItem = new ACATItem();

        acatItem._id = acatItemDto.acatItemId;
        acatItem.item = acatItemDto.item;
        acatItem.unit = acatItemDto.unit;
        acatItem.remark = acatItemDto.remark;

        //Set references
        acatItem.sectionId = acatItemDto.sectionId;
        acatItem.costListId = acatItemDto.costListId;
        acatItem.groupedListId = acatItemDto.groupListId;

        //Set category
        acatItem.category = ACATItemHelper.COST_CATEGORY;

        acatItem.createdAt = acatItemDto.createdAt;
        acatItem.updatedAt = acatItemDto.updatedAt;
//        acatItem.category = "COST";
        acatItem.pendingCreation = acatItemDto.pendingCreation;

        return acatItem;
    }

    private ACATItemValue createEstimatedACATItemValue(ACATItemDto acatItemDto){
        ACATItemValue acatItemValue = new ACATItemValue();

        acatItemValue.acatItemId = acatItemDto.acatItemId;
        acatItemValue.unitPrice = acatItemDto.estimatedUnitPrice;
        acatItemValue.value = acatItemDto.estimatedValue;
        acatItemValue.totalPrice = acatItemDto.estimatedTotalPrice;
        acatItemValue.type = ACATItemValueHelper.ESTIMATED_VALUE_TYPE;

        return acatItemValue;

    }


    private ACATItemValue createActualACATItemValue(ACATItemDto acatItemDto){
        ACATItemValue acatItemValue = new ACATItemValue();

        acatItemValue.acatItemId = acatItemDto.acatItemId;
        acatItemValue.unitPrice = acatItemDto.actualUnitPrice;
        acatItemValue.value = acatItemDto.actualValue;
        acatItemValue.totalPrice = acatItemDto.actualTotalPrice;
        acatItemValue.type = ACATItemValueHelper.ACTUAL_VALUE_TYPE;

        return acatItemValue;

    }

    private CashFlow createCashFlow(CashFlowData cashFlowData){
        CashFlow cashFlow = new CashFlow();

        cashFlow.referenceId = cashFlowData.referenceId;
        cashFlow.type = cashFlowData.type;
        cashFlow.classification = cashFlowData.classification;

        cashFlow.january = cashFlowData.january;
        cashFlow.february = cashFlowData.february;
        cashFlow.march = cashFlowData.march;
        cashFlow.april = cashFlowData.april;
        cashFlow.may = cashFlowData.may;
        cashFlow.june = cashFlowData.june;
        cashFlow.july = cashFlowData.july;
        cashFlow.august = cashFlowData.august;
        cashFlow.september = cashFlowData.september;
        cashFlow.october = cashFlowData.october;
        cashFlow.november = cashFlowData.november;
        cashFlow.december = cashFlowData.december;

        return cashFlow;

    }
}
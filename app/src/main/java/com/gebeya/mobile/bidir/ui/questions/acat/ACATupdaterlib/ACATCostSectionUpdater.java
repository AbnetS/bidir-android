package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionRequest;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by abuti on 6/9/2018.
 */

public class ACATCostSectionUpdater {
    @Inject ACATCostSectionLocalSource acatCostSectionLocalSource;
    @Inject ACATCostSectionRemoteSource acatCostSectionRemoteSource;
    @Inject CashFlowLocalSource cashFlowLocalSource;
    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    public ACATCostSectionUpdater(){
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    public Observable<ACATCostSectionResponse> uploadEstimatedValuesForACATCostSection (@NonNull ACATCostSectionDto acatCostSectionDto){
        ACATCostSectionResponse responseBackUp = new ACATCostSectionResponse();
        responseBackUp.sections = new ArrayList<>();
        responseBackUp.estimatedCashFlows = new ArrayList<>();

        final ACATCostSectionRequest request = new ACATCostSectionRequest();

        request.acatApplicationId = acatCostSectionDto.acatApplicationId;

        //Create the various local entities from the dto
        ACATCostSection section = acatCostSectionDto.section;
        CashFlow estimatedCashFlow = acatCostSectionDto.estimatedCashFlow;
        estimatedCashFlow.referenceId = acatCostSectionDto.section._id;

        return acatCostSectionLocalSource.markForUpload(section)
                .flatMap(savedSection -> {
                    request.section = savedSection;
                    responseBackUp.sections.add(savedSection);
                    return cashFlowLocalSource.put(estimatedCashFlow);
                })
                .flatMap(savedCashFlow -> {
                    request.estimatedCashFlow = savedCashFlow;
                    responseBackUp.estimatedCashFlows.add(savedCashFlow);
                    return acatCostSectionRemoteSource.updateACATCostSection(acatCostSectionDto.section._id, request);
                })
                //.flatMapCompletable(pageResponse -> {
                .flatMap(response -> {
                    response.sections.get(0).uploaded = true;
                    response.sections.get(0).modified = false;
                    response.sections.get(0).updatedAt = new DateTime();

                    acatCostSectionLocalSource.put(response.sections.get(0));
                    cashFlowLocalSource.put(response.estimatedCashFlows.get(0));

                    //return Completable.complete();
                    return Observable.just(response);
                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatCostSectionDto.acatApplicationId);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });

    }

    public Observable<ACATCostSectionResponse> uploadActualValuesForACATCostSection (@NonNull ACATCostSectionDto acatCostSectionDto) throws Exception{
        ACATCostSectionResponse responseBackUp = new ACATCostSectionResponse();
        responseBackUp.sections = new ArrayList<>();
        responseBackUp.actualCashFlows = new ArrayList<>();

        final ACATCostSectionRequest request = new ACATCostSectionRequest();

        request.acatApplicationId = acatCostSectionDto.acatApplicationId;

        //Create the various local entities from the dto
        ACATCostSection section = acatCostSectionDto.section;
        CashFlow actualCashFlow = acatCostSectionDto.actualCashFlow;
        actualCashFlow.referenceId = acatCostSectionDto.section._id;

        return acatCostSectionLocalSource.markForUpload(section)
                .flatMap(savedSection -> {
                    request.section = savedSection;
                    responseBackUp.sections.add(savedSection);
                    return cashFlowLocalSource.put(actualCashFlow);
                })
                .flatMap(savedCashFlow -> {
                    request.actualCashFlow = savedCashFlow;
                    responseBackUp.actualCashFlows.add(savedCashFlow);
                    return acatCostSectionRemoteSource.updateACATCostSection(acatCostSectionDto.section._id, request);
                })
                //.flatMapCompletable(pageResponse -> {
                .flatMap(response -> {
                    response.sections.get(0).uploaded = true;
                    response.sections.get(0).modified = false;
                    response.sections.get(0).updatedAt = new DateTime();

                    acatCostSectionLocalSource.put(response.sections.get(0));
                    cashFlowLocalSource.put(response.actualCashFlows.get(0));

                    //return Completable.complete();
                    return Observable.just(response);
                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatCostSectionDto.acatApplicationId);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });

    }

    public Observable<ACATCostSectionResponse> updateEstimatedValuesForACATCostSection (@NonNull ACATCostSectionDto acatCostSectionDto){

        //Create the various local entities from the dto
        ACATCostSection section = acatCostSectionDto.section;
        CashFlow estimatedSectionCashFlow = acatCostSectionDto.estimatedCashFlow;

        acatCostSectionLocalSource.put(section);
        cashFlowLocalSource.put(estimatedSectionCashFlow);

        ACATCostSectionResponse response = new ACATCostSectionResponse();
        response.sections = new ArrayList<>();
        response.estimatedCashFlows = new ArrayList<>();

        response.sections.add(section);
        response.estimatedCashFlows.add(estimatedSectionCashFlow);

        return Observable.just(response);
        //return Completable.complete();

    }

    public void updateActualValuesForACATCostSection (@NonNull ACATCostSectionDto acatCostSectionDto){

        //Create the various local entities from the dto
        ACATCostSection section = acatCostSectionDto.section;
        CashFlow actualSectionCashFlow = acatCostSectionDto.actualCashFlow;

        acatCostSectionLocalSource.put(section);
        cashFlowLocalSource.put(actualSectionCashFlow);

    }

}

package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 6/16/2018.
 */

public class ACATApplicationUpdater {
    public ACATApplicationUpdater() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Inject ACATApplicationLocalSource acatApplicationLocalSource;
    @Inject ACATApplicationRemoteSource acatApplicationRemoteSource;

    @Inject ACATApplicationParser parser;

    @Inject CashFlowLocalSource cashFlowLocalSource;
    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    public Observable<ACATApplicationResponse> updateEstimatedValuesForACATApplication(@NonNull ACATApplicationDto acatApplicationDto) {

        //Create the various local entities from the dto
        ACATApplication application = acatApplicationDto.acatApplication;
        CashFlow estimatedACATAppCashFlow = acatApplicationDto.estimatedNetCashFlow;

        acatApplicationLocalSource.put(application);
        cashFlowLocalSource.put(estimatedACATAppCashFlow);

        ACATApplicationResponse response = new ACATApplicationResponse();
        response.acatApplication = application;
        response.estimatedNetCashFlow = estimatedACATAppCashFlow;

        return Observable.just(response);
        //return Completable.complete();

    }

    public Observable<ACATApplicationResponse> uploadEstimatedValuesForACATApplication(@NonNull ACATApplicationDto acatApplicationDto){
        ACATApplicationResponse responseBackUp = new ACATApplicationResponse();

        ACATApplicationRequest request = new ACATApplicationRequest();

        ACATApplication acatApplication = acatApplicationDto.acatApplication;
        CashFlow estimatedACATAppCashFlow = acatApplicationDto.estimatedNetCashFlow;


        return acatApplicationLocalSource.markForUpload(acatApplicationDto.acatApplication)
                .flatMap(savedACATApplication -> {
                    request.acatApplication = savedACATApplication;
                    responseBackUp.acatApplication = savedACATApplication;
                    return cashFlowLocalSource.put(estimatedACATAppCashFlow);
                })
                .flatMap(savedEstimatedCashFlow -> {
                    request.estimatedCashFlow = savedEstimatedCashFlow;
                    responseBackUp.estimatedNetCashFlow = savedEstimatedCashFlow;
                    return acatApplicationRemoteSource.update(acatApplicationDto.acatApplication._id, request);
                    //return cashFlowLocalSource.put(actualACATAppCashFlow);
                })
//                .flatMap(savedActualCashFlow -> {
//                    request.actualCashFlow = savedActualCashFlow;//

                //})
                //.flatMapCompletable(pageResponse -> {
                .flatMap(response -> {
                    response.acatApplication.uploaded = true;
                    response.acatApplication.modified = false;
                    response.acatApplication.updatedAt = new DateTime();

                    acatApplicationLocalSource.put(response.acatApplication);
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    //cashFlowLocalSource.put(pageResponse.actualNetCashFlow);

                    //return Completable.complete();
                    return Observable.just(response);

                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) || error.equals(ApiErrors.CONNECTION_ABORT_ERROR)
                            || error.equals(ApiErrors.NO_ROUTE_TO_HOST)) {
                        acatApplicationSyncLocalSource.markForUpload(acatApplicationDto.acatApplication._id);
                        return Observable.just(responseBackUp);
                    }
                    else{
                        throwable.printStackTrace();
                        throw new Exception();
                    }


                });
    }

    public Observable<ACATApplicationResponse> uploadActualValuesForACATApplication(@NonNull ACATApplicationDto acatApplicationDto){
        ACATApplicationResponse responseBackUp = new ACATApplicationResponse();

        ACATApplicationRequest request = new ACATApplicationRequest();

        ACATApplication acatApplication = acatApplicationDto.acatApplication;
        CashFlow actualACATAppCashFlow = acatApplicationDto.actualNetCashFlow;

        return acatApplicationLocalSource.markForUpload(acatApplicationDto.acatApplication)
                .flatMap(savedACATApplication -> {
                    request.acatApplication = savedACATApplication;
                    responseBackUp.acatApplication = savedACATApplication;
                    return cashFlowLocalSource.put(actualACATAppCashFlow);
                })
                .flatMap(savedActualCashFlow -> {
                    request.actualCashFlow = savedActualCashFlow;
                    responseBackUp.actualNetCashFlow = savedActualCashFlow;
                    request.acatApplication.status = parser.getLocalStatus(ACATApplicationParser.STATUS_API_LOAN_GRANTED);
                    return acatApplicationRemoteSource.update(acatApplicationDto.acatApplication._id, request);
                    //return cashFlowLocalSource.put(actualACATAppCashFlow);
                })
//                .flatMap(savedActualCashFlow -> {
//                    request.actualCashFlow = savedActualCashFlow;//

                //})
                //.flatMapCompletable(pageResponse -> {
                .flatMap(response -> {
                    response.acatApplication.uploaded = true;
                    response.acatApplication.modified = false;
                    response.acatApplication.updatedAt = new DateTime();

                    acatApplicationLocalSource.put(response.acatApplication);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
                    //cashFlowLocalSource.put(pageResponse.actualNetCashFlow);

                    //return Completable.complete();
                    return Observable.just(response);

                })
                .onErrorResumeNext(throwable -> {
                    //TODO: Ignore only Network absence error.
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(acatApplicationDto.acatApplication._id);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });


    }

}

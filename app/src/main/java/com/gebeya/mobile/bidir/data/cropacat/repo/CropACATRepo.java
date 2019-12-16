package com.gebeya.mobile.bidir.data.cropacat.repo;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATRequest;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATRemoteSource;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocationResponse;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

/**
 * Created by Samuel K. on 6/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropACATRepo implements CropACATRepoSource {

    @Inject CropACATRemoteSource remote;
    @Inject CropACATLocalSource local;

    @Inject CashFlowLocalSource cashFlowLocalSource;
    @Inject GPSLocationLocalSource gpsLocationLocalSource;

    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    CropACAT responseBackUp = new CropACAT();

    public CropACATRepo() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }
    @Override
    public Completable updateCropACATInstance(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        final CropACATRequest request = new CropACATRequest();
        return local.get(cropACATId)
                .flatMap(data -> {
                    CropACAT cropACAT = data.get();
                    local.markForUpload(cropACAT); //Mark For Upload for the sync service
                    //TODO: Check out why the above line is inserted (why we need to mark the item for upload, it is already uploaded).
                    request.cropACAT = cropACAT;
                    return cashFlowLocalSource.get(cropACATId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE);
                })
                .flatMap(response -> {
                    request.estimatedCashFlow = response.get(0);
                    return cashFlowLocalSource.get(cropACATId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE);
                })
                .flatMap(response -> {
                    request.actualCashFlow = response.get(0);
                    return gpsLocationLocalSource.getAllCropACATGPSLocations(cropACATId, acatApplicationId);
                })
                .flatMap(response -> {
                    request.gpsLocationResponse = new GPSLocationResponse();
                    request.gpsLocationResponse.gpsLocations = new ArrayList<>();
                    request.gpsLocationResponse.gpsLocations = response;
                    return remote.registerGPS(request);
                })
                .flatMap(response->{
                    return remote.updateCropACATInstance(cropACATId, acatApplicationId, request);
                })
                .flatMapCompletable(response -> {
                    response.cropACAT.uploaded = true;
                    response.cropACAT.modified = false;
                    response.cropACAT.updatedAt = new DateTime();
                    local.put(response.cropACAT); //marked as uploaded
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
                    gpsLocationLocalSource.putAll(response.gpsLocation.gpsLocations,response.cropACAT._id, response.cropACAT.acatApplicationID);
//                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
//                        gpsLocationLocalSource.put(gpsLocation);
//                    }
                    return Completable.complete();
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<CropACAT> updateProgress(@NonNull String cropACATId, @NonNull String acatApplicationId) {


        local.get(cropACATId)
                .subscribe(cropACATData -> {
                    responseBackUp = cropACATData.get();
                });

        return remote.updateProgressStatus(cropACATId, acatApplicationId, "complete")
                .flatMap(response -> {
                    local.put(response.cropACAT); //marked as uploaded
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
//                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
//                        gpsLocationLocalSource.put(gpsLocation);
//                    }
                    gpsLocationLocalSource.putAll(response.gpsLocation.gpsLocations, response.cropACAT._id, response.cropACAT.acatApplicationID);
                    return Observable.just(response.cropACAT);
                })
                .onErrorResumeNext((Function<? super Throwable, ? extends ObservableSource<? extends CropACAT>>) throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException) ) {
                       acatApplicationSyncLocalSource.markForUpload(acatApplicationId);
                       return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception(throwable);
                });
    }

    @Override
    public Observable<Boolean> changeCropACATStatus(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull String status) {
        return remote.updateApiStatus(cropACATId, acatApplicationId, status)
                .flatMap(response -> {
                    local.put(response.cropACAT); //marked as uploaded
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
//                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
//                        gpsLocationLocalSource.put(gpsLocation);
//                    }
                    gpsLocationLocalSource.putAll(response.gpsLocation.gpsLocations, response.cropACAT._id, response.cropACAT.acatApplicationID);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> submitCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        return remote.updateApiStatus(cropACATId, acatApplicationId, ACATApplicationParser.STATUS_API_SUBMITTED)
                .flatMap(response -> {
                    local.put(response.cropACAT); //marked as uploaded
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
//                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
//                        gpsLocationLocalSource.put(gpsLocation);
//                    }
                    gpsLocationLocalSource.putAll(response.gpsLocation.gpsLocations, response.cropACAT._id, response.cropACAT.acatApplicationID);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> approveCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        return remote.updateApiStatus(cropACATId, acatApplicationId, ACATApplicationParser.STATUS_API_AUTHORIZED)
                .flatMap(response -> {
                    local.put(response.cropACAT); //marked as uploaded
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
//                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
//                        gpsLocationLocalSource.put(gpsLocation);
//                    }
                    gpsLocationLocalSource.putAll(response.gpsLocation.gpsLocations, response.cropACAT._id, response.cropACAT.acatApplicationID);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> declineCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        return remote.updateApiStatus(cropACATId, acatApplicationId, ACATApplicationParser.STATUS_API_DECLINED_FOR_REVIEW)
                .flatMap(response -> {
                    local.put(response.cropACAT); //marked as uploaded
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
//                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
//                        gpsLocationLocalSource.put(gpsLocation);
//                    }
                    gpsLocationLocalSource.putAll(response.gpsLocation.gpsLocations, response.cropACAT._id, response.cropACAT.acatApplicationID);
                    return Observable.just(true);
                });
    }
}

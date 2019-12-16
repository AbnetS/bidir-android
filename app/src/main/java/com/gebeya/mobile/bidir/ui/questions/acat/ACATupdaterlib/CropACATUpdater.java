package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;


import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATRequest;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATRemoteSource;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocationResponse;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.CropACATDto;


import org.joda.time.DateTime;

import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 6/16/2018.
 */

public class CropACATUpdater {
    @Inject CropACATLocalSource cropACATLocalSource;
    @Inject CashFlowLocalSource cashFlowLocalSource;
    @Inject CropACATRemoteSource cropACATRemoteSource;
    @Inject GPSLocationLocalSource gpsLocationLocalSource;
    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    public CropACATUpdater(){
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }
    public Observable<CropACATResponse> updateEstimatedValuesForCropACAT(CropACATDto cropACATDto){
        //Create the various local entities from the dto
        CropACAT cropACAT = cropACATDto.cropACAT;
        CashFlow estimatedCropACATCashFlow = cropACATDto.estimatedNetCashFlow;

        cropACATLocalSource.put(cropACAT);
        cashFlowLocalSource.put(estimatedCropACATCashFlow);

        CropACATResponse response = new CropACATResponse();
        response.cropACAT= cropACAT;
        response.estimatedNetCashFlow = estimatedCropACATCashFlow;

        return Observable.just(response);

    }

    public Observable<CropACATResponse> uploadEstimatedValuesForCropACAT (CropACATDto cropACATDto){
        CropACATResponse responseBackUp = new CropACATResponse();

        final CropACATRequest request = new CropACATRequest();


        CropACAT cropACAT = cropACATDto.cropACAT;
        CashFlow estimatedCropACATCashFlow = cropACATDto.estimatedNetCashFlow;
        //CashFlow actualCropACATCashFlow = cropACATDto.actualNetCashFlow;

        return cropACATLocalSource.markForUpload(cropACAT)
                .flatMap(savedCropACAT -> {
                    request.cropACAT = savedCropACAT;
                    responseBackUp.cropACAT = savedCropACAT;
                    return cashFlowLocalSource.put(estimatedCropACATCashFlow);
                })
                .flatMap(savedEstimatedCashFlow -> {
                    request.estimatedCashFlow = savedEstimatedCashFlow;
                    responseBackUp.estimatedNetCashFlow = savedEstimatedCashFlow;
                    return gpsLocationLocalSource.getAllCropACATGPSLocations(cropACATDto.cropACAT._id, cropACATDto.cropACAT.acatApplicationID);
                    //return cashFlowLocalSource.put(actualCropACATCashFlow);
                })
//                .flatMap(savedActualCashFlow -> {
//                    request.actualCashFlow = savedActualCashFlow;
//                    return gpsLocationLocalSource.getAllCropACATGPSLocations(cropACATDto.cropACAT._id, cropACATDto.cropACAT.acatApplicationID);
//                })

                .flatMap(gpsLocations -> {
                    request.gpsLocationResponse = new GPSLocationResponse();
                    request.gpsLocationResponse.gpsLocations = new ArrayList<>();
                    request.gpsLocationResponse.gpsLocations = gpsLocations;

                    responseBackUp.gpsLocation = new GPSLocationResponse();
                    responseBackUp.gpsLocation.gpsLocations = new ArrayList<>();
                    responseBackUp.gpsLocation.gpsLocations = gpsLocations;

                    return cropACATRemoteSource.updateCropACATInstance(cropACATDto.cropACAT._id,
                            cropACATDto.cropACAT.acatApplicationID, request);

                })
                .flatMap(response -> {
                //.flatMapCompletable(pageResponse -> {
                    response.cropACAT.uploaded = true;
                    response.cropACAT.modified = false;
                    response.cropACAT.updatedAt = new DateTime();
                    cropACATLocalSource.put(response.cropACAT);
                    cashFlowLocalSource.put(response.estimatedNetCashFlow);
                    //cashFlowLocalSource.put(pageResponse.actualNetCashFlow);
                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
                        gpsLocationLocalSource.put(gpsLocation);
                    }
                    return Observable.just(response);
                    //return Completable.complete();

                })
                .onErrorResumeNext(throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(cropACATDto.cropACAT.acatApplicationID);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });
    }

    public Observable<CropACATResponse> uploadActualValuesForCropACAT (CropACATDto cropACATDto)
    {
        CropACATResponse responseBackUp = new CropACATResponse();

        final CropACATRequest request = new CropACATRequest();


        CropACAT cropACAT = cropACATDto.cropACAT;
        CashFlow actualCropACATCashFlow = cropACATDto.actualNetCashFlow;
        //CashFlow actualCropACATCashFlow = cropACATDto.actualNetCashFlow;

        return cropACATLocalSource.markForUpload(cropACAT)
                .flatMap(savedCropACAT -> {
                    request.cropACAT = savedCropACAT;
                    responseBackUp.cropACAT = savedCropACAT;
                    return cashFlowLocalSource.put(actualCropACATCashFlow);
                })
                .flatMap(savedActualCashFlow -> {
                    request.actualCashFlow = savedActualCashFlow;
                    responseBackUp.actualNetCashFlow = savedActualCashFlow;
                    return gpsLocationLocalSource.getAllCropACATGPSLocations(cropACATDto.cropACAT._id, cropACATDto.cropACAT.acatApplicationID);
                })
                .flatMap(gpsLocations -> {
                    request.gpsLocationResponse = new GPSLocationResponse();
                    request.gpsLocationResponse.gpsLocations = new ArrayList<>();
                    request.gpsLocationResponse.gpsLocations = gpsLocations;

                    responseBackUp.gpsLocation = new GPSLocationResponse();
                    responseBackUp.gpsLocation.gpsLocations = new ArrayList<>();
                    responseBackUp.gpsLocation.gpsLocations = gpsLocations;

                    return cropACATRemoteSource.updateCropACATInstance(cropACATDto.cropACAT._id,
                            cropACATDto.cropACAT.acatApplicationID, request);

                })
                .flatMap(response -> {
                    //.flatMapCompletable(pageResponse -> {
                    response.cropACAT.uploaded = true;
                    response.cropACAT.modified = false;
                    response.cropACAT.updatedAt = new DateTime();
                    cropACATLocalSource.put(response.cropACAT);
                    cashFlowLocalSource.put(response.actualNetCashFlow);
                    //cashFlowLocalSource.put(pageResponse.actualNetCashFlow);
                    for (GPSLocation gpsLocation: response.gpsLocation.gpsLocations){
                        gpsLocationLocalSource.put(gpsLocation);
                    }
                    return Observable.just(response);
                    //return Completable.complete();

                })
                .onErrorResumeNext(throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(cropACATDto.cropACAT.acatApplicationID);
                        return Observable.just(responseBackUp);
                    }
                    else
                        throw new Exception();
                });
    }
}


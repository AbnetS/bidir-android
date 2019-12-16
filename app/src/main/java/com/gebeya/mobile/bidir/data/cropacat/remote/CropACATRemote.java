package com.gebeya.mobile.bidir.data.cropacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATRequest;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 6/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropACATRemote extends BaseRemoteSource<CropACATService> implements CropACATRemoteSource {

    @Inject CropACATResponseParser cropACATResponseParser;
    @Inject CropACATParser parser;

    public CropACATRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.ACAT, CropACATService.class);
    }

    @Override
    public Observable<CropACATResponse> updateCropACATInstance(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull CropACATRequest request) {
        return build().call(service.updateCropACATInstance(cropACATId, createCropACATRequest(request, acatApplicationId)))
                .map(cropACATResponse->
                        cropACATResponseParser.parse(cropACATResponse, acatApplicationId));
    }

    @Override
    public Observable<CropACATResponse> registerGPS(@NonNull CropACATRequest request) {
        return build().call(service.registerGPSLocation(request.cropACAT._id, createGPSRequest(request)))
                .map(response -> cropACATResponseParser.parse(response, request.cropACAT.acatApplicationID));
                //.flatMapCompletable(object -> Completable.complete());
    }

    @Override
    public Observable<CropACATResponse> getCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        return build().call(service.getCropACAT(cropACATId))
                .map(response -> cropACATResponseParser.parse(response, acatApplicationId));
    }

    @Override
    public Observable<CropACATResponse> updateProgressStatus(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull String status) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", status);

        return build()
                .call(service.updateCropACATInstance(cropACATId, request))
                .map(cropACATResponse -> cropACATResponseParser.parse(cropACATResponse, acatApplicationId));
    }

    @Override
    public Observable<CropACATResponse> updateApiStatus(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull String status) {
        final JsonObject request = new JsonObject();
        request.addProperty("is_client_acat", true);
        request.addProperty("client_acat", acatApplicationId);
        request.addProperty("status", status);

        return build()
                .call(service.updateCropACATInstance(cropACATId, request))
                .map(cropACATResponse -> cropACATResponseParser.parse(cropACATResponse, acatApplicationId));
    }

    private JsonObject createCropACATRequest(@NonNull CropACATRequest request, @NonNull String acatApplicationId) {
        final JsonObject object = new JsonObject();

        final CropACAT cropACAT = request.cropACAT;
        object.addProperty("is_client_acat", true);
        object.addProperty("client_acat", acatApplicationId);
        object.addProperty("first_expense_month", cropACAT.firstExpenseMonth);
        object.add("non_financial_resources", toJson(cropACAT.NonFinServices));
        object.addProperty("access_to_non_financial_resources", cropACAT.accessToNonFinServices);
        object.addProperty("cropping_area_size", cropACAT.croppingArea);
        object.addProperty("status", parser.getApiStatus(cropACAT.status));

        // Adding GPS location.
        createGPSRequest(request);

        object.add("estimated", createEstimatedValues(request));
        object.add("achieved", createActualValues(request));

        return object;

    }

    private JsonObject createGPSRequest(@NonNull CropACATRequest request) {
        JsonObject object = new JsonObject();

        if (request.gpsLocationResponse != null){
            int noOfGPSLocations = 0;
            noOfGPSLocations = request.gpsLocationResponse.gpsLocations.size();
            final JsonObject singlePointGps = new JsonObject();
            final JsonArray polygonGps = new JsonArray();

            final JsonObject single = new JsonObject();
            final JsonObject polygon = new JsonObject();

            if (noOfGPSLocations > 0) {
                if (noOfGPSLocations == 1) {
                    singlePointGps.addProperty("longitude", request.gpsLocationResponse.gpsLocations.get(0).longitude);
                    singlePointGps.addProperty("latitude", request.gpsLocationResponse.gpsLocations.get(0).latitude);
                    single.add("single_point", singlePointGps);
                    object.addProperty("type", "single_point");
                    object.add("gps_location", single);
                } else if (noOfGPSLocations > 1) {
                    for (int j = 0; j < noOfGPSLocations; j++) {
                        final JsonObject temp = new JsonObject();
                        temp.addProperty("longitude", request.gpsLocationResponse.gpsLocations.get(j).longitude);
                        temp.addProperty("latitude", request.gpsLocationResponse.gpsLocations.get(j).latitude);
                        polygonGps.add(temp);
                        polygon.add("polygon", polygonGps);
                        //object.add("gps_location", polygon);
                        object.addProperty("type", "polygon");
                        object.add("gps_location", polygon);
                    }
                }
            }
        }

        return object;
    }

    private JsonArray toJson(@NonNull List<String> items) {
        final JsonArray array = new JsonArray();
        for(int i = 0; i < items.size(); i++) {
            array.add(items.get(i));
        }

        return array;
    }

    private JsonObject createEstimatedValues(@NonNull CropACATRequest request) {
        final JsonObject object = new JsonObject();

        object.addProperty("total_cost", request.cropACAT.estimatedTotalCost);
        object.addProperty("total_revenue", request.cropACAT.estimatedTotalRevenue);
        object.addProperty("net_income", request.cropACAT.estimatedNetIncome);

        if (request.estimatedCashFlow != null) {
            JsonObject estimatedCashFlow = createCashFlow(request.estimatedCashFlow);
            object.add("net_cash_flow", estimatedCashFlow);
        }

        return object;
    }

    private JsonObject createActualValues(@NonNull CropACATRequest request) {
        final JsonObject object = new JsonObject();

        object.addProperty("total_cost", request.cropACAT.actualTotalCost);
        object.addProperty("total_revenue", request.cropACAT.actualTotalRevenue);
        object.addProperty("net_income", request.cropACAT.actualNetIncome);

        if (request.actualCashFlow != null) {
            JsonObject actualCashFlow = createCashFlow(request.actualCashFlow);
            object.add("net_cash_flow", actualCashFlow);
        }

        return object;
    }

    private JsonObject createCashFlow(@NonNull CashFlow cashFlow){
        final JsonObject object =  new JsonObject();

        object.addProperty("jan", cashFlow.january);
        object.addProperty("feb", cashFlow.february);
        object.addProperty("mar", cashFlow.march);
        object.addProperty("apr", cashFlow.april);
        object.addProperty("may", cashFlow.may);
        object.addProperty("june", cashFlow.june);
        object.addProperty("july", cashFlow.july);
        object.addProperty("aug", cashFlow.august);
        object.addProperty("sep", cashFlow.september);
        object.addProperty("oct", cashFlow.october);
        object.addProperty("nov", cashFlow.november);
        object.addProperty("dec", cashFlow.december);

        return object;
    }
}

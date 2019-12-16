package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by abuti on 8/29/2018.
 */

public interface YieldConsumptionService {
    @PUT("yieldConsumptions/{id}")
    Observable<JsonObject> update(@Path("id") String yieldConsumptionId, @Body JsonObject request);
}

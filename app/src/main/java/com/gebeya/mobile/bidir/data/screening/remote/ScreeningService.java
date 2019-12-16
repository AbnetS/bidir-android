package com.gebeya.mobile.bidir.data.screening.remote;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Retrofit Screening service interface contract
 */
public interface ScreeningService {

    @PUT("{id}")
    Observable<JsonObject> update(@Path("id") String screeningId, @Body JsonObject request);

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @GET("{id}")
    Observable<JsonObject> getOne(@Path("id") String screeningId);

}
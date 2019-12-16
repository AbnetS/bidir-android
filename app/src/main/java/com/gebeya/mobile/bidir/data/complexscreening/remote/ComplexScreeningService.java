package com.gebeya.mobile.bidir.data.complexscreening.remote;

import android.support.annotation.NonNull;

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
 * Retrofit interface for remote {@link com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening}
 * objects.
 */
public interface ComplexScreeningService {

    @PUT("{id")
    Observable<JsonObject> update(@Path("id") String screeningId, @Body JsonObject request);

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> downloadAll();

    @GET("{id}")
    Observable<JsonObject> download(@Path("id") String screeningId);

    @PUT("{id}")
    Observable<JsonObject> uploadQuestions(@NonNull @Path("id") String screeningId, @Body JsonObject request);

    @PUT("{id}")
    Observable<JsonObject> uploadScreening(@NonNull @Path("id") String screeningId, @Body JsonObject request);

    @POST("create")
    Observable<JsonObject> create(@NonNull @Body JsonObject request);
}
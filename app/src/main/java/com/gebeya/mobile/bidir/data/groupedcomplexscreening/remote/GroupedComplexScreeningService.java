package com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupedComplexScreeningService {

    @GET("screenings/" + Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @GET("{id}/screenings")
    Observable<JsonObject> get(@Path("id") String groupId);

    @PUT("{id}/screenings/submit")
    Observable<JsonObject> submitGroupScreening(@NonNull @Path("id") String groupId);

    @PUT("{id}/screenings/approve")
    Observable<JsonObject> approveGroupScreening(@NonNull @Path("id") String groupId);

    @PUT("{id}/screenings/status")
    Observable<JsonObject> updateStatus(@NonNull @Path("id") String groupId);

    @POST("screenings/create")
    Observable<JsonObject> createNewLoan(@NonNull @Body JsonObject request);

}

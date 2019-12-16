package com.gebeya.mobile.bidir.data.groupedacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupedACATService {

    @GET("acats/" + Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @GET("{id}/acats")
    Observable<JsonObject> get(@Path("id") String groupId);

    @PUT("{id}/acats/submit")
    Observable<JsonObject> submitGroupACAT(@NonNull @Path("id") String groupId);

    @PUT("{id}/acats/approve")
    Observable<JsonObject> approveGroupACAT(@NonNull @Path("id") String groupId);

    @PUT("{id}/acats/status")
    Observable<JsonObject> updateStatus(@NonNull @Path("id") String groupId);

    @POST("acats/create")
    Observable<JsonObject> create(@NonNull @Body JsonObject request);

    @POST("{id}/acats/members/initialize")
    Observable<JsonObject> initialize(@NonNull @Path("id") String groupId, @NonNull @Body JsonObject request);

}

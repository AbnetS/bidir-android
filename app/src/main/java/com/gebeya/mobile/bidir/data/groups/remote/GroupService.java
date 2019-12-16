package com.gebeya.mobile.bidir.data.groups.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Group service retrofit contract.
 */
public interface GroupService {

    @POST("create")
    Observable<JsonObject> register(@Body JsonObject request);

    @PUT("{id}")
    Observable<JsonObject> update(@Path("id") String groupId, @Body JsonObject request);

    @PUT("{id}/members")
    Observable<JsonObject> updateMembers(@Path("id") String groupId, @Body JsonObject request);

    @PUT("{id}/leader")
    Observable<JsonObject> updateLeader(@Path("id") String groupId, @Body JsonObject request);

    @PUT("{id}/status")
    Observable<JsonObject> updateStatus(@Path("id") String groupId);

    @PUT("{id}/status")
    Observable<JsonObject> updateGroupStatus(@Path("id") String groupId, @Body JsonObject request);

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @GET("{id}")
    Observable<JsonObject> getOne(@Path("id") String groupId);

    @POST("screenings/create")
    Observable<JsonObject> createNewLoan(@NonNull @Body JsonObject request);
}

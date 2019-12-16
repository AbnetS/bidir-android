package com.gebeya.mobile.bidir.data.groupedcomplexloan.remote;


import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupedComplexLoanService {

    @GET("loans/" + Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @GET("{id}/loans")
    Observable<JsonObject> get(@Path("id") String groupId);

    @PUT("{id}/loans/submit")
    Observable<JsonObject> submitGroupLoan(@NonNull @Path("id") String groupId);

    @PUT("{id}/loans/approve")
    Observable<JsonObject> approveGroupLoan(@NonNull @Path("id") String groupId);

    @PUT("{id}/loans/status")
    Observable<JsonObject> updateStatus(@NonNull @Path("id") String groupId);

    @POST("{id}/loans/initialize")
    Observable<JsonObject> create(@NonNull @Path("id") String groupId);
}

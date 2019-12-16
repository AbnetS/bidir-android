package com.gebeya.mobile.bidir.data.loanapplication.remote;

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
 * API Retrofit interface for the {@link com.gebeya.mobile.bidir.data.loanapplication.LoanApplication}
 *
 */
public interface LoanApplicationService {

    @PUT("{id}")
    Observable<JsonObject> update(@Path("id") String loanApplicationId, @Body JsonObject request);

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @POST("create")
    Observable<JsonObject> create(@Body Map<String, String> request);

    @GET("{id}")
    Observable<JsonObject> getOne(@Path("id") String loanApplicationId);
}
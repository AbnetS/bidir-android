package com.gebeya.mobile.bidir.data.loanProposal.remote;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Samuel K. on 8/16/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface LoanProposalService {

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @POST("create")
    Observable<JsonObject> create(@Body JsonObject request);

    @GET("clients/{id}")
    Observable<JsonObject> getOneByClient(@Path("id") String clientId);

    @GET("{id}")
    Observable<JsonObject> get(@Path("id") String loanProposalId);

    @PUT("{id}")
    Observable<JsonObject> update(@Path("id") String loanProposalId, @Body JsonObject request);


}

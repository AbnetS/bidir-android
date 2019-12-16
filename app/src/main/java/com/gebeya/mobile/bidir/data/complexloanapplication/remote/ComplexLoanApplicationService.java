package com.gebeya.mobile.bidir.data.complexloanapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for remote {@link com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication}
 * application objects.
 */
public interface ComplexLoanApplicationService {

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> downloadAll();

    @GET(Constants.PAGINATE_BASE)
    Call<JsonObject> getPaginationData();

    @GET(Constants.PAGINATE_BASE)
    Call<JsonObject> getAllByPage(@Query("page") int currentPage);

    @GET("{id}")
    Observable<JsonObject> download(@NonNull @Path("id") String applicationId);

    @POST("create")
    Observable<JsonObject> create(@NonNull @Body JsonObject request);

    @PUT("{id}")
    Observable<JsonObject> uploadApplication(@NonNull @Path("id") String applicationId, @Body JsonObject request);
}
package com.gebeya.mobile.bidir.data.acatapplication.remote;

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
 * Created by abuti on 5/17/2018.
 */

public interface ACATApplicationService {

    @GET(Constants.PAGINATE_URL)
    Observable<JsonObject> getAll();

    @GET("{id}")
    Observable<JsonObject> getOne(@Path("id") String clientId);

    @POST("initialize")
    Observable<JsonObject> initializeClientACAT(@Body JsonObject object);

    @PUT("{id}")
    Observable<JsonObject> update(@Path("id") String acatApplicationId, @Body JsonObject object);

    @GET(Constants.PAGINATE_BASE)
    Call<JsonObject> getPaginationData();

    @GET(Constants.PAGINATE_BASE)
    Call<JsonObject> getAllByPage(@Query("page") int currentPage);
}

package com.gebeya.mobile.bidir.data.acatform.remote;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service for a single ACAT form
 */
public interface ACATFormService {

    @GET(Constants.PAGINATE_URL)
    Observable<JsonObject> getAll();

    @GET(Constants.PAGINATE_BASE)
    Call<JsonObject> getAllByPage(@Query("page") int currentPage);

    @GET(Constants.PAGINATE_BASE)
    Call<JsonObject> getPaginationData();
}

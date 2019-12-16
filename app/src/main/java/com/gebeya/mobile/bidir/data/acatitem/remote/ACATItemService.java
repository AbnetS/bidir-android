package com.gebeya.mobile.bidir.data.acatitem.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by abuti on 6/5/2018.
 */

public interface ACATItemService {

    @PUT("costLists/{id}")
    Observable<JsonObject> updateCostListItem(@Path("id") String costListItemId, @Body JsonObject object);

    @POST("costLists/add")
    Observable<JsonObject> addCostListItem(@Body JsonObject object);


}
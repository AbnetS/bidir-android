package com.gebeya.mobile.bidir.data.cropacat.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Samuel K. on 6/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface CropACATService {

    @PUT("{id}")
    Observable<JsonObject> updateCropACATInstance(@Path("id") String cropACATId, @Body JsonObject object);

    @PUT("{id}" + "/geolocation")
    Observable<JsonObject> registerGPSLocation(@Path("id") String cropACATId, @Body JsonObject object);

    @GET("{id}")
    Observable<JsonObject> getCropACAT(@Path("id") String cropACATId);
}

package com.gebeya.mobile.bidir.data.user.remote;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Retrofit service definition for a remote {@link com.gebeya.mobile.bidir.data.user.User} and the
 * associated {@link com.gebeya.mobile.bidir.data.permission.Permission} objects.
 */
public interface UserService {

    @POST("login")
    Observable<JsonObject> login(@Body Map<String, String> request);
}
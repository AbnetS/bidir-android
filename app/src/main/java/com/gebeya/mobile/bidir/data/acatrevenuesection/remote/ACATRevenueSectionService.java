package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;


import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Samuel K. on 8/29/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ACATRevenueSectionService {

    @PUT("sections/{id}")
    Observable<JsonObject> update(@Path("id") String sectionId, @Body JsonObject request);
}

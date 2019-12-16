package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ACATCostSectionService {

    @PUT("sections/{id}")
    Observable<JsonObject> updateSection(@Path("id") String sectionId, @Body JsonObject object);
}

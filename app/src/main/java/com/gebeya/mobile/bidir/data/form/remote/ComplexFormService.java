package com.gebeya.mobile.bidir.data.form.remote;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * {@link retrofit2.Retrofit} service for the API endpoints to do with forms.
 */
public interface ComplexFormService {

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> downloadAll();

    @GET("{id}")
    Observable<JsonObject> download(@Path("id") String formId);
}
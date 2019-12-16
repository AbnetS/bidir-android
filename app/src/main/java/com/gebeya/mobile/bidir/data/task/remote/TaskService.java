package com.gebeya.mobile.bidir.data.task.remote;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Retrofit API service for a Task
 */
public interface TaskService {

    @GET(Constants.PAGINATE_URL)
    Observable<JsonObject> getAll();

    @PUT("{id}/status")
    Observable<JsonObject> updateStatus(@Path("id") String taskId, @Body Map<String, String> request);
}
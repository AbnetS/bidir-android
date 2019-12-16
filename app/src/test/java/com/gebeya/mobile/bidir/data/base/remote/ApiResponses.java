package com.gebeya.mobile.bidir.data.base.remote;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Helper class for generating API responses, such as errors
 */
public final class ApiResponses {

    public static Throwable createOfflineError() {
        return createError(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION);
    }

    public static Throwable createError(@NonNull String message) {
        return createError(400, message);
    }

    public static Throwable createError(int code, @NonNull String message) {
        JsonObject object = createJsonError(message);
        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), object.toString());
        return new HttpException(Response.error(code, body));
    }

    private static JsonObject createJsonError(@NonNull String message) {
        JsonObject object = new JsonObject();

        JsonObject errorObject = new JsonObject();
        errorObject.addProperty("message", message);
        object.add("error", errorObject);

        return object;
    }
}

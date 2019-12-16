package com.gebeya.mobile.bidir.impl.util.error;

import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Base implementation for the {@link ErrorHandler} network code handler.
 */
public class BaseErrorHandler implements ErrorHandler {

    @Override
    public Function<Throwable, ObservableSource<JsonObject>> checkErrorObject() {
        return throwable -> {
            JsonObject body = getBody(throwable);
            if (body != null) {
                String message = getError(body);
                if (message != null) {
                    return Observable.error(new Throwable(message));
                }
            }
            return Observable.error(throwable);
        };
    }

    @Nullable
    @Override
    public String getError(JsonObject object) throws Exception {
        if (object.has("error")) {
            JsonObject errorObject = object.getAsJsonObject("error");
            return errorObject.get("message").getAsString();
        }
        return null;
    }

    @Nullable
    @Override
    public JsonObject getBody(Throwable throwable) throws Exception {
        if (throwable instanceof HttpException) {
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            JsonParser parser = new JsonParser();
            return parser.parse(body.string()).getAsJsonObject();
        }
        return null;
    }
}
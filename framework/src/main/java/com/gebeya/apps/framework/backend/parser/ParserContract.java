package com.gebeya.apps.framework.backend.parser;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.backend.api.response.ResponseWrapperContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface ParserContract<T> {

    interface ParserItemCallback<T> {
        void onItemParsed(T item);
        void onItemParsingFailed(Exception e);
    }

    void parseItems(@NonNull ResponseWrapperContract response, @NonNull ParserContract.ParserItemsCallback<T> callback);

    interface ParserItemsCallback<T> {
        void onItemsParsed(List<T> items);
        void onItemsParsingFailed(Exception e);
    }

    void parseItem(@NonNull JSONObject object, @NonNull ParserContract.ParserItemCallback<T> callback);

    T getItem(JSONObject object) throws JSONException;
}

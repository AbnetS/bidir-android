package com.gebeya.mobile.bidir.data.pagination.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.google.gson.JsonObject;

/**
 * Interface definition for a class that should parse a {@link PageResponse}, given the JSON data.
 */
public interface PageParser {

    /**
     * Parses the given {@link JsonObject} object and returns a {@link PageResponse} object.
     *
     * @param object JSON object to parse from.
     * @param type Type of service to parse.
     * @return parsed PageResponse.
     * @throws Exception if there was an error during the parsing process.
     */
    PageResponse parse(@NonNull JsonObject object, @NonNull Service type) throws Exception;
}

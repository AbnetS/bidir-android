package com.gebeya.mobile.bidir.data.acatform.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/12/2018.
 */

public interface ACATFormParser {
    /**
     * Parse for an ACAT Form
     *
     * @param object to parseRespose from.
     * @return ACAT Form object parsed.
     * @throws Exception throws error if one occur during parsing.
     */
    ACATForm parse(@NonNull JsonObject object) throws Exception;

    List<String> toList(@NonNull JsonArray array);
}
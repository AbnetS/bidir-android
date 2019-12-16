package com.gebeya.mobile.bidir.data.section.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.section.Section;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Interface for a {@link com.gebeya.mobile.bidir.data.section.Section} parser.
 */
public interface SectionParser {

    /**
     * Parse the given JsonObject and return a Section object.
     *
     * @param object        JsonObject to parseResponse from, as input.
     * @param referenceId   Reference ID to which this Section belongs to (could be screening or
     *                      loan application).
     * @param referenceType Reference type to which this Section belongs to (could be screening or
     *                      loan application).
     * @return Section that has been parsed.
     * @throws Exception thrown if there was an error during the parsing process.
     */
    Section parse(@NonNull JsonObject object,
                  @NonNull String referenceId,
                  @NonNull String referenceType) throws Exception;

    /**
     * Get a list of String question IDs from the given JsonArray array.
     *
     * @param array JsonArray to parseResponse List of question String IDs from.
     * @return List of parsed String question IDs.
     */
    List<String> getQuestionIds(@NonNull JsonArray array);
}

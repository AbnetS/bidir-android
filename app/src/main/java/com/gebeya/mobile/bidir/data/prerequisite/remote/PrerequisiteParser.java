package com.gebeya.mobile.bidir.data.prerequisite.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Interface contract for a {@link com.gebeya.mobile.bidir.data.prerequisite.Prerequisite} parser.
 */
public interface PrerequisiteParser {

    /**
     * Parse the given JsonObject and return a {@link Prerequisite} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @param parentQuestionId Parent ComplexQuestion that this Prerequisite belongs to.
     * @return Parsed Prerequisite object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    Prerequisite parse(@NonNull JsonObject object, @NonNull String parentQuestionId) throws Exception;

    /**
     * Create a JsonArray array of prerequisites given a list of prerequisite objects.
     *
     * @param prerequisites List of prerequisite objects to initializeACAT.
     * @return JsonArray of all the objects.
     */
    JsonArray createPrerequisites(@NonNull List<Prerequisite> prerequisites);

    /**
     * Create a prerequisite given a single {@link Prerequisite} object.
     *
     * @param prerequisite Prerequisite object to initializeACAT.
     * @return JsonObject created from the prerequisite.
     */
    JsonObject createPrerequisite(@NonNull Prerequisite prerequisite);
}

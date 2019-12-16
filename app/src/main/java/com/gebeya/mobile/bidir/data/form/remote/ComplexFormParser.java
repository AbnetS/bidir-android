package com.gebeya.mobile.bidir.data.form.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.google.gson.JsonObject;

/**
 * Interface definition for a {@link com.gebeya.mobile.bidir.data.form.ComplexForm} parser.
 */
public interface ComplexFormParser {

    String TYPE_LOCAL_ACAT = "acat";
    String TYPE_API_ACAT = "ACAT";

    String TYPE_LOCAL_TEST = "test";
    String TYPE_API_TEST = "TEST";

    String TYPE_LOCAL_GROUP_APPLICATION = "group_application";
    String TYPE_API_GROUP_APPLICATION = "GROUP_APPLICATION";

    String TYPE_LOCAL_LOAN_APPLICATION = "loan_application";
    String TYPE_API_LOAN_APPLICATION = "LOAN_APPLICATION";

    String TYPE_LOCAL_SCREENING = "screening";
    String TYPE_API_SCREENING = "SCREENING";

    String TYPE_UNKNOWN = "UNKNOWN";

    String LAYOUT_LOCAL_TWO_COLUMNS = "TWO_COLUMNS";
    String LAYOUT_API_TWO_COLUMNS = "TWO_COLUMNS";

    String LAYOUT_LOCAL_THREE_COLUMNS = "THREE_COLUMNS";
    String LAYOUT_API_THREE_COLUMNS = "THREE_COLUMNS";

    String LAYOUT_UNKNOWN = "LAYOUT_UNKNOWN";

    /**
     * Parse the given JsonObject object and return a {@link ComplexForm} object.
     *
     * @param object JsonObject to parse from.
     * @return ComplexForm object parsed.
     * @throws Exception thrown if there was an error during the parsing process.
     */
    ComplexForm parse(@NonNull JsonObject object) throws Exception;

    /**
     * Get the local form type, given the API form type.
     *
     * @param apiType API form type.
     * @return Local form type.
     */
    String getLocalType(@NonNull String apiType);

    /**
     * Get the API form type, given the local form type.
     *
     * @param localType Local form type.
     * @return API form type.
     */
    String getApiType(@NonNull String localType);

    /**
     * Get the local complex form layout, given the API version.
     *
     * @param apiLayout API layout whose local version is needed.
     * @return local layout version for the given API layout version.
     */
    String getLocalLayout(@NonNull String apiLayout);

    /**
     * Get the API complex form layout, given the local version.
     * @param localLayout local layout whose API version is needed.
     * @return API layout version for the given local layout version.
     */
    String getApiLayout(@NonNull String localLayout);
}
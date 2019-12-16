package com.gebeya.mobile.bidir.data.complexquestion.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Interface for a parseResponse for a {@link com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion}
 * object.
 */
public interface ComplexQuestionParser {

    String VALIDATION_FACTOR_LOCAL_NONE = "NONE";
    String VALIDATION_FACTOR_API_NONE = "NONE";

    String VALIDATION_FACTOR_LOCAL_ALPHANUMERIC = "ALPHANUMERIC";
    String VALIDATION_FACTOR_API_ALPHANUMERIC = "ALPHANUMERIC";

    String VALIDATION_FACTOR_LOCAL_NUMERIC = "NUMERIC";
    String VALIDATION_FACTOR_API_NUMERIC = "NUMERIC";

    String VALIDATION_FACTOR_LOCAL_ALPHABETIC = "ALPHABETIC";
    String VALIDATION_FACTOR_API_ALPHABETIC = "ALPHABETIC";

    String VALIDATION_FACTOR_UNKNOWN = "VALIDATION_FACTOR_UNKNOWN";

    String TYPE_LOCAL_YES_NO = "YES_NO";
    String TYPE_API_YES_NO = "YES_NO";

    String TYPE_LOCAL_FILL_IN_BLANK = "FILL_IN_BLANK";
    String TYPE_API_FILL_IN_BLANK = "FILL_IN_BLANK";

    String TYPE_LOCAL_MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
    String TYPE_API_MULTIPLE_CHOICE = "MULTIPLE_CHOICE";

    String TYPE_LOCAL_SINGLE_CHOICE = "SINGLE_CHOICE";
    String TYPE_API_SINGLE_CHOICE = "SINGLE_CHOICE";

    String TYPE_LOCAL_GROUPED = "GROUPED";
    String TYPE_API_GROUPED = "GROUPED";

    String TYPE_UNKNOWN = "TYPE_UNKNOWN";

    /**
     * Parse the given JsonObject and return a {@link ComplexQuestion} question.
     *
     * @param object        JsonObject to parseResponse from.
     * @param referenceId   Reference ID to which this question belongs to.
     * @param referenceType Reference type of this question.
     * @return parsed {@link ComplexQuestion} object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ComplexQuestion parse(@NonNull JsonObject object,
                          @NonNull String referenceId,
                          @NonNull String referenceType) throws Exception;

    /**
     * Parses the given JsonArray object for {@link ComplexQuestion} IDs
     *
     * @param array JsonArray to parseResponse from.
     * @return String List of parsed {@link ComplexQuestion} IDs.
     */
    List<String> getSubQuestionIds(@NonNull JsonArray array);

    /**
     * Convert the given List of Strings into a JsonArray.
     *
     * @param list Input list to convert.
     * @return JsonArray containing the list of Strings.
     */
    JsonArray toArray(@NonNull List<String> list);

    /**
     * Convert the given JsonArray of Strings into a List of Strings.
     *
     * @param array Input array to convert.
     * @return List of Strings containing the array contents.
     */
    List<String> toList(@NonNull JsonArray array);

    /**
     * Get the local validation factor, given the API version.
     *
     * @param apiValidationFactor API version to convert from.
     * @return Local equivalent version.
     */
    String getLocalValidationFactor(@NonNull String apiValidationFactor);

    /**
     * Get the API validation factor, given the local version.
     *
     * @param localValidationFactor local version to convert from.
     * @return API equivalent version.
     */
    String getApiValidationFactor(@NonNull String localValidationFactor);

    /**
     * Get the local type, given the API version.
     *
     * @param apiType API type whose local type is needed.
     * @return local type found.
     */
    String getLocalType(@NonNull String apiType);

    /**
     * Get the API type, given the local version.
     *
     * @param localType local type whose API type is needed.
     * @return API type found.
     */
    String getApiType(@NonNull String localType);

    /**
     * Create a JsonObject from the given {@link ComplexQuestion} object.
     *
     * @param question Question to initializeACAT the JSON data from.
     * @return JsonObject created from the given complex object argument.
     */
    JsonObject createQuestion(@NonNull ComplexQuestion question);
}
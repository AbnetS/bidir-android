package com.gebeya.mobile.bidir.data.form.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.gebeya.mobile.bidir.data.form.ComplexFormResponse;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.section.Section;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Remote source interface definition for complex forms.
 */
public interface ComplexFormRemoteSource {

    /**
     * Download all the complex form components along with all their data, each as a pageResponse object.
     *
     * @return Observable List of all the complex form responses.
     */
    Observable<List<ComplexFormResponse>> downloadAll();

    /**
     * Download a single complex form with the given ID along with its components inside a pageResponse
     * object.
     *
     * @param formId Form ID to use for downloading.
     * @return Observable complex form pageResponse.
     */
    Observable<ComplexFormResponse> download(@NonNull String formId);

    /**
     * Parse the given JsonObject object and return a complex form pageResponse object.
     *
     * @param object JsonObject object to parse from
     * @return ComplexFormResponse pageResponse object.
     * @throws Exception thrown if there was an error during the parsing process.
     */
    ComplexFormResponse parseResponse(@NonNull JsonObject object) throws Exception;

    /**
     * Parse the given JsonArray object and return a List of complex question responses.
     *
     * @param array         JsonArray to parse from as input object.
     * @param referenceId   reference ID to which the questions belong to.
     * @param referenceType Reference type to which this question belongs to.
     * @return List of parsed complex question pageResponse objects.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ComplexQuestionResponse> parseQuestionResponses(@NonNull JsonArray array,
                                                         @NonNull String referenceId,
                                                         @NonNull String referenceType) throws Exception;

    /**
     * Parses the given JsonArray object and returns a list of Sections belonging to the given
     * reference ID value.
     *
     * @param array         JsonArray array to parse Sections from.
     * @param referenceId   Reference ID to which the Sections belong to.
     * @param referenceType Reference type to which the Sections belong to.
     * @return List of parsed Sections.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<Section> parseSections(@NonNull JsonArray array,
                                @NonNull String referenceId,
                                @NonNull String referenceType) throws Exception;

    /**
     * Parse the given JsonArray object and return a List of complex sub-question responses.
     *
     * @param array         JsonArray to parse from as input object.
     * @param referenceId   reference ID to which the sub-questions belong to.
     * @param referenceType Reference type to which this sub-questions belong to.
     * @return List of parsed complex sub-question pageResponse objects.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ComplexQuestionResponse> parseSubQuestionResponses(@NonNull JsonArray array,
                                                            @NonNull String referenceId,
                                                            @NonNull String referenceType) throws Exception;

    /**
     * Parse the given JsonObject and return a single {@link ComplexQuestionResponse} pageResponse.
     *
     * @param object        JsonObject to parse from, as input.
     * @param referenceId   Reference ID to which this question belongs to.
     * @param referenceType Reference type to which this question belong to.
     * @return Parsed single {@link ComplexQuestionResponse} object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ComplexQuestionResponse parseQuestionResponse(@NonNull JsonObject object,
                                                  @NonNull String referenceId,
                                                  @NonNull String referenceType) throws Exception;

    /**
     * Return a list of prerequisites belonging to a specific (parent) question with the provided parent question ID.
     *
     * @param array            JsonArray to parse from, as an input.
     * @param parentQuestionId Parent question String ID value.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<Prerequisite> parsePrerequisites(@NonNull JsonArray array,
                                          @NonNull String parentQuestionId) throws Exception;
}

package com.gebeya.mobile.bidir.data.complexscreening.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningRequest;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningResponse;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.section.Section;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface defining remote source for a
 * {@link com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening} object.
 */
public interface ComplexScreeningRemoteSource {

    /**
     * Download all the screening components and return them as a list of
     * {@link ComplexScreeningResponse} objects.
     *
     * @return Observable List of ComplexScreeningResponse pageResponse objects.
     */
    Observable<List<ComplexScreeningResponse>> downloadAll();

    /**
     * Download a single screening along with all its components with the given screening ID.
     *
     * @param screeningId Screening ID to use as the lookup parameter.
     * @return Observable of screening object as a ComplexScreeningResponse pageResponse object.
     */
    Observable<ComplexScreeningResponse> download(@NonNull String screeningId);

    /**
     * Parse the given JsonObject and return a {@link ComplexScreeningResponse} object.
     *
     * @param object JsonObject to parseResponse from, as input.
     * @return parsed {@link ComplexScreeningResponse} object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ComplexScreeningResponse parseResponse(@NonNull JsonObject object) throws Exception;

    /**
     * Parses the given JsonArray object and returns a list of Sections belonging to the given
     * reference ID value.
     *
     * @param array         JsonArray array to parseACATApplication Sections from.
     * @param referenceId   Reference ID to which the Sections belong to.
     * @param referenceType Reference type to which the Sections belong to.
     * @return List of parsed Sections.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<Section> parseSections(@NonNull JsonArray array,
                                @NonNull String referenceId,
                                @NonNull String referenceType) throws Exception;

    /**
     * Parse the given JsonObject and return a single {@link ComplexQuestionResponse} pageResponse.
     *
     * @param object      JsonObject to parseACATApplication from, as input.
     * @param referenceId Reference ID to which this question belongs to.
     * @return Parsed single {@link ComplexQuestionResponse} object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ComplexQuestionResponse parseQuestionResponse(@NonNull JsonObject object,
                                                  @NonNull String referenceId) throws Exception;

    /**
     * Parse the given JsonArray object and return a List of complex question responses.
     *
     * @param array       JsonArray to parseACATApplication from as input object.
     * @param referenceId reference ID to which the questions belong to.
     * @return List of parsed complex question pageResponse objects.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ComplexQuestionResponse> parseQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId) throws Exception;

    /**
     * Parse the given JsonArray object and return a List of complex sub-question responses.
     *
     * @param array       JsonArray to parseACATApplication from as input object.
     * @param referenceId reference ID to which the sub-questions belong to.
     * @return List of parsed complex sub-question pageResponse objects.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ComplexQuestionResponse> parseSubQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId) throws Exception;

    /**
     * Return a list of prerequisites belonging to a specific (parent) question with the provided parent question ID.
     *
     * @param array            JsonArray to parseACATApplication from, as an input.
     * @param parentQuestionId Parent question String ID value.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<Prerequisite> parsePrerequisites(@NonNull JsonArray array,
                                          @NonNull String parentQuestionId) throws Exception;

    /**
     * Upload a screening with the with the Screening ID and use the given complex screening request
     * data as the payload to upload.
     *
     * @param screeningId Screening ID to upload the data to.
     * @param request     Request information to upload to the API.
     * @return Observable of the uploaded screening pageResponse.
     */
    Observable<ComplexScreeningResponse> uploadQuestions(@NonNull String screeningId, @NonNull ComplexScreeningRequest request);

    /**
     * Upload the given screening data to the remote API, along with the given request data and
     * possible remark.
     *
     * @param screeningId Screening ID to upload to.
     * @param request     Screening request data to upload.
     * @param remark      Remark (if any) that should be sent.
     * @return Observable of the uploaded screening pageResponse.
     */
    Observable<ComplexScreeningResponse> uploadScreening(@NonNull String screeningId,
                                                         @NonNull ComplexScreeningRequest request,
                                                         @Nullable String remark);

    /**
     * Create a screening application for clients that are eligible to multiple loans.
     *
     * @param clientId Client for whom screening is going to be created.
     * @return Created Screening.
     */
    Observable<ComplexScreeningResponse> create(@NonNull String clientId, boolean isGrouped);
}
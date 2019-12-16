package com.gebeya.mobile.bidir.data.complexloanapplication.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationRequest;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.section.Section;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Remote source interface definition for {@link com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication}
 * objects.
 */
public interface ComplexLoanApplicationRemoteSource {

    /**
     * Download all the complex loan applications from the remote source (API).
     *
     * @return Observable list of all the complex loan applications.
     */
    Observable<List<ComplexLoanApplicationResponse>> downloadAll();

    /***
     * Downloads and returns the {@link PageResponse} pageResponse data, used for further pagination
     * downloads.
     *
     * @return downloaded pagination information of the endpoint, in the form of a
     * {@link PageResponse} object.
     */
    PageResponse downloadPageData() throws Exception;

    /**
     * Download a list of complex loan application pageResponse objects, along with pagination info.
     *
     * @param currentPage The page to download from, mapping to a respective page on the API.
     * @return {@link PaginatedComplexLoanApplicationResponse} pageResponse object, along with
     * pagination data.
     * @throws Exception if there was an error during the download.
     */
    PaginatedComplexLoanApplicationResponse getAllPaginated(int currentPage) throws Exception;


    /**
     * Download a complex loan application with the given ID, from the API.
     *
     * @param applicationId Complex loan application ID to download.
     * @return Observable pageResponse for the complex loan application.
     */
    Observable<ComplexLoanApplicationResponse> download(@NonNull String applicationId);

    /**
     * Returns a {@link ComplexLoanApplicationResponse} object parsed from the given input JSON
     * object.
     *
     * @param object JsonObject to parse from.
     * @return Parsed complex loan application pageResponse.
     * @throws Exception thrown if there was an error during parsing.
     */
    ComplexLoanApplicationResponse parseResponse(@NonNull JsonObject object) throws Exception;

    /**
     * Create a complex loan application belonging to the given client.
     *
     * @param client Client to use for the creation of the complex loan application.
     * @return Created loan application pageResponse, as an observable.
     */
    Observable<ComplexLoanApplicationResponse> create(@NonNull Client client);

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
     * Parse the given JsonObject and return a single {@link ComplexQuestionResponse} pageResponse.
     *
     * @param object      JsonObject to parse from, as input.
     * @param referenceId Reference ID to which this question belongs to.
     * @return Parsed single {@link ComplexQuestionResponse} object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ComplexQuestionResponse parseQuestionResponse(@NonNull JsonObject object,
                                                  @NonNull String referenceId) throws Exception;

    /**
     * Parse the given JsonArray object and return a List of complex question responses.
     *
     * @param array       JsonArray to parse from as input object.
     * @param referenceId reference ID to which the questions belong to.
     * @return List of parsed complex question pageResponse objects.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ComplexQuestionResponse> parseQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId) throws Exception;

    /**
     * Parse the given JsonArray object and return a List of complex sub-question responses.
     *
     * @param array       JsonArray to parse from as input object.
     * @param referenceId reference ID to which the sub-questions belong to.
     * @return List of parsed complex sub-question pageResponse objects.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ComplexQuestionResponse> parseSubQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId) throws Exception;

    /**
     * Return a list of prerequisites belonging to a specific (parent) question with the provided parent question ID.
     *
     * @param array            JsonArray to parse from, as an input.
     * @param parentQuestionId Parent question String ID value.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<Prerequisite> parsePrerequisites(@NonNull JsonArray array,
                                          @NonNull String parentQuestionId) throws Exception;

    /**
     * Upload the given loan application data to the remote API.
     *
     * @param applicationId Target loan application to upload to.
     * @param request       complex loan application request data.
     * @param remark        Remark that might go along with the payload data.
     * @return Parsed pageResponse from the API.
     */
    Observable<ComplexLoanApplicationResponse> uploadApplication(@NonNull String applicationId,
                                                                 @NonNull ComplexLoanApplicationRequest request,
                                                                 @Nullable String remark);
}
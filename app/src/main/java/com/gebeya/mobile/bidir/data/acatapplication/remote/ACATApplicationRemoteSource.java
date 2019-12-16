package com.gebeya.mobile.bidir.data.acatapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */

public interface ACATApplicationRemoteSource {
    /**
     * Retrieve a list of all the ACAT Forms form the API
     *
     * @return List of {@link ACATApplicationResponse} objects.
     */
    Observable<List<ACATApplicationResponse>> getAll();

    /***
     * Downloads and returns the {@link PageResponse} pageResponse data, used for further pagination
     * downloads.
     *
     * @return downloaded pagination information of the endpoint, in the form of a
     * {@link PageResponse} object.
     */
    PageResponse downloadPageData() throws Exception;

    /**
     * Download a list of {@link ACATApplicationResponse} objects along with pagination data,
     * inside the {@link PageResponse} object.
     *
     * @param currentPage The current page to fetch the data / information from.
     * @return {@link PaginatedACATApplicationResponse} response object.
     * @throws Exception if there was an error during the download process.
     */
    PaginatedACATApplicationResponse getAllPaginated(int currentPage) throws Exception;

    /**
     * Retrieve Client ACAT using client Id.
     * @param clientId client Id used to fetch the ACAT application.
     * @return ACAT application for the specified client.
     */
    Observable<ACATApplicationResponse> fetchACATApplication(@NonNull String clientId);

    /**
     * Retrieve Client ACAT using ACAT Id.
     *
     * @param acatId ACAT Id used to fetch the ACAT Application.
     * @return ACAT Application for the specified Id.
     */
    Observable<ACATApplicationResponse> getACAT(@NonNull String acatId);

    /**
     * Parse the provided JsonObject into a {@link ACATApplicationResponse} object
     *
     * @param object source JSON object
     * @return parsed {@link ACATApplicationResponse} object
     * @throws Exception if there is error during the parsing process
     */
    ACATApplicationResponse parseACATApplication(@NonNull JsonObject object, @NonNull String acatApplicationID) throws Exception;

    ACATApplicationResponse parse(@NonNull JsonObject object) throws Exception;

    Observable<ACATApplicationResponse> initializeACAT(@NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs);

    Observable<ACATApplicationResponse> update(@NonNull String acatApplicationId, @NonNull ACATApplicationRequest request);

    Observable<ACATApplicationResponse> updateApiStatus(@NonNull ACATApplication acatApplication, @NonNull String status);


}

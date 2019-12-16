package com.gebeya.mobile.bidir.data.acatform.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.gebeya.mobile.bidir.data.crop.Crop;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;


/**
 * Interface for the remote ACAT Form API acess
 */

public interface ACATFormRemoteSource {

    /**
     * Retrieve a list of all the ACAT Forms form the API
     *
     * @return List of {@link ACATFormResponse} objects.
     */
    Observable<List<ACATFormResponse>> getAll();

    /***
     * Downloads and returns the {@link PageResponse} pageResponse data, used for further pagination
     * downloads.
     *
     * @return downloaded pagination information of the endpoint, in the form of a
     * {@link PageResponse} object.
     */
    PageResponse downloadPageData() throws Exception;

    /**
     * Retrieve a list of all the ACAT form objects from the API in a paginated form, with the
     * given page form.
     *
     * @param currentPage The current page that the data should be fetched from.
     * @return {@link PaginatedACATFormResponse} object also containing the pagination
     * info.
     */
    PaginatedACATFormResponse getAllPaginated(int currentPage) throws Exception;

    /**
     * Parse the provided JsonObject into a {@link ACATFormResponse} object
     *
     * @param object source JSON object
     * @return parsed {@link ACATFormResponse} object
     * @throws Exception if there is error during the parsing process
     */
    ACATFormResponse parse(@NonNull JsonObject object) throws Exception;

    /**
     * Parse for an ACAT Form
     *
     * @param object to parseRespose from.
     * @return ACAT Form object parsed.
     * @throws Exception throws error if one occur during parsing.
     */
    ACATForm parseForm(@NonNull JsonObject object) throws Exception;

    /**
     * parseACATApplication for ACAT's Crop object.
     * @param object JsonObject to be parsed from.
     * @return Crop Object parsed.
     * @throws Exception thrown if error occurs during the parsing process.
     */
    Crop parseCrop(@NonNull JsonObject object) throws Exception;
}

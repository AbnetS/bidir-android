package com.gebeya.mobile.bidir.data.screening.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.screening.Screening;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * API contract for the screening service
 */
public interface ScreeningRemoteSource {

    /**
     * Update a Screening and all its answers.
     *
     * @param response ScreeningResponse object to that contains the screening data.
     * @return Observable for screening.
     */
    Observable<Screening> update(@NonNull ScreeningResponse response);

    /**
     * Update the status of a screening.
     *
     * @param screening screening whose status needs to be updated.
     * @param status    new status to saveLoanProposal the screening to.
     * @param remark    comment/remark to post along with the status.
     * @return Screening object produced during the screening.
     */
    Observable<Screening> updateApiStatus(@NonNull Screening screening,
                                          @NonNull String status,
                                          @Nullable String remark);

    /**
     * Retrieve all the Screenings.
     *
     * @return Observable list of screenings responses.
     */
    Observable<List<ScreeningResponse>> getAll();

    /**
     * Retrieve a single screening
     *
     * @param screeningId Screening to be retrieved.
     * @return Observable screening pageResponse.
     */
    Observable<ScreeningResponse> getOne(@NonNull String screeningId);

    /**
     * Parse the JsonObject into a {@link ScreeningResponse} object
     *
     * @param object input JSON object.
     * @return Parsed {@link ScreeningResponse} that contains the Screening and List of answers.
     * @throws Exception if there is an issue during the parsing.
     */
    ScreeningResponse parse(@NonNull JsonObject object) throws Exception;

    /**
     * Parses the JsonObject into a {@link Screening} and a {@link Client} object.
     *
     * @param object input JSON object.
     * @return Parsed {@link Screening} object.
     * @throws Exception if there was an issue during the parsing.
     */
    Screening parseScreeningAndClient(@NonNull JsonObject object) throws Exception;

    Screening parseScreeningOnly(@NonNull JsonObject object) throws Exception;

}
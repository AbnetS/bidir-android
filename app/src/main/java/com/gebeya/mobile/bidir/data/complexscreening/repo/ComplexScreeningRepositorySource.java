package com.gebeya.mobile.bidir.data.complexscreening.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningResponse;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Contract definition for the repository for ComplexScreening.
 */
public interface ComplexScreeningRepositorySource extends
        FetchOne<ComplexScreening>,
        FetchMany<ComplexScreening> {

    /**
     * Save the given screening's data to the remote API.
     *
     * @param screeningId Screening ID to save.
     * @return Completable, indicating whether the process has completed or not.
     */
    Completable saveScreening(@NonNull String screeningId);

    /**
     * Submit the given screening's data to the remote API.
     *
     * @param screeningId Screening ID to submit.
     * @return Completable, indicating whether the process has completed or not.
     */
    Completable submitScreening(@NonNull String screeningId);

    /**
     * Approve the screening on the remote API side with the given screening ID.
     *
     * @param screeningId Screening ID to approve.
     * @return Completable, indicating whether the process has completed or not.
     */
    Completable approveScreening(@NonNull String screeningId);

    /**
     * Decline the screening to the remote API with the given screening ID.
     *
     * @param screeningId Screening ID to decline.
     * @param isFinal     indicates whether the decline is final or not.
     * @return Completable, indicating whether the process has completed or not.
     */
    Completable declineScreening(@NonNull String screeningId, boolean isFinal, @Nullable String remark);

    /**
     * Create a screening using client Id.
     *
     * @param clientId Client to use for the creation process.
     */
    Observable<ComplexScreeningResponse> createScreening(@NonNull String clientId, boolean isGrouped);
}
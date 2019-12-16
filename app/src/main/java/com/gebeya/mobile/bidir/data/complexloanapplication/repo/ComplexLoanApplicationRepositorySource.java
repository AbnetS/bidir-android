package com.gebeya.mobile.bidir.data.complexloanapplication.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;

import io.reactivex.Completable;

/**
 * Contract definition for the repository for {@link com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication}
 * objects.
 */
public interface ComplexLoanApplicationRepositorySource extends
        FetchOne<ComplexLoanApplication>,
        FetchMany<ComplexLoanApplication> {

    /**
     * Create a loan application on the API side, with the given client ID.
     *
     * @param client client to use for the creation process.
     */
    Completable createLoanApplication(@NonNull Client client);

    /**
     * Save the given application to the remote server API.
     *
     * @param applicationId Application ID whose data to upload/save.
     * @return Completable, indicating if the action finished or not.
     */
    Completable saveApplication(@NonNull String applicationId);

    /**
     * Submit the given application to the remote server API.
     *
     * @param applicationId Application ID whose data to submit.
     * @return Completable, indicating if the action finished or not.
     */
    Completable submitApplication(@NonNull String applicationId);

    /**
     * Accept the given application ID and submit the data to the API.
     *
     * @param applicationId Application ID to approve.
     * @return Completable, indicating if the action finished or not.
     */
    Completable acceptApplication(@NonNull String applicationId);

    /**
     * Decline the given application.
     *
     * @param applicationId Application ID to decline.
     * @param isFinal       boolean indicating whether the decline is final or not.
     * @param remark        Optional remark to accompany the decline.
     * @return Completable, indicating if the action finished or not.
     */
    Completable declineApplication(@NonNull String applicationId, boolean isFinal, @Nullable String remark);
}
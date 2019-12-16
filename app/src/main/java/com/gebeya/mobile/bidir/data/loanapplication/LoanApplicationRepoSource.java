package com.gebeya.mobile.bidir.data.loanapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.client.Client;

import io.reactivex.Completable;

/**
 * Repository contract for the loan application
 */
public interface LoanApplicationRepoSource extends
        ReadableSource<LoanApplication>,
        FetchOne<LoanApplication>,
        FetchMany<LoanApplication>,
        ReadSize {

    /**
     * Update the remote API questions belonging to the given LoanApplication.
     */
    Completable pushQuestions(@NonNull String loanApplicationId);

    /**
     * Update the LoanApplication status on the API side, with the given one.
     */
    Completable pushStatus(@NonNull LoanApplication application, @NonNull String status, @Nullable String remark);

    /**
     * Create a loan application on the API side.
     */
    Completable createLoanApplication(@NonNull Client client);
}
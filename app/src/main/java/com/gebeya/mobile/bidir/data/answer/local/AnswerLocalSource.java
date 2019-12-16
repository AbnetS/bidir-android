package com.gebeya.mobile.bidir.data.answer.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteOne;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Contract for a local source for answers
 */
public interface AnswerLocalSource extends WriteOne<Answer> {

    /**
     * Save a list of {@link Answer} objects, for a specific Screening or LoanApplication.
     * object.
     *
     * @param answers       List of answer objects to save.
     * @param referenceType The reference type to use (either screening or loan application).
     * @param referenceId   The reference id to save to.
     * @return true if the list was saved, false otherwise.
     */
    Observable<List<Answer>> putAll(@NonNull List<Answer> answers, @NonNull String referenceType, @NonNull String referenceId);

    /**
     * Get a list of all the {@link Answer} objects belonging to a specific Screening or LoanApplication
     *
     * @return List of all the answers retrieved.
     */
    Observable<List<Answer>> getAll(@NonNull String referenceType, @NonNull String referenceId);

    /**
     * Remove a list of {@link Answer} objects belonging to a specific Screening or LoanApplication
     */
    Completable removeAll(@NonNull String referenceType, @NonNull String referenceId);


    /**
     * Get the size of the for a specific Screening or LoanApplication.
     *
     * @return size of the answers.
     */
    int size(@NonNull String referenceType, @NonNull String referenceId);
}
package com.gebeya.mobile.bidir.data.screening;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;

import io.reactivex.Completable;

/**
 * Contract for the Screening repository
 */
public interface ScreeningRepoSource extends
        ReadableSource<Screening>,
        FetchOne<Screening>,
        FetchMany<Screening>,
        DeleteOne,
        ReadSize {

    /**
     * Update the remote API questions belonging to the given Screening.
     */
    Completable pushQuestions(@NonNull String screeningId);

    /**
     * Update the Screening status on the API side, with the given one.
     */
    Completable pushStatus(@NonNull Screening screening, @NonNull String status, @Nullable String remark);
}
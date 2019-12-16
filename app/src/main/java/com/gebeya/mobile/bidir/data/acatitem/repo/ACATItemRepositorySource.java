package com.gebeya.mobile.bidir.data.acatitem.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemDto;

import io.reactivex.Completable;

/**
 * Created by abuti on 6/5/2018.
 */

public interface ACATItemRepositorySource {
    Completable updateEstimatedValueOfACATItem (@NonNull ACATItemDto acatItemDto, @NonNull String acatItemId);
}

package com.gebeya.mobile.bidir.data.acatitem.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemDto;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemRequest;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 6/5/2018.
 */

public interface ACATItemRemoteSource {

    Observable<ACATItemResponse> updateACATItem
            (@NonNull String acatItemId, @NonNull ACATItemRequest request) ;

    Observable<ACATItemResponse> updateRevenueItem (@NonNull String acatItemId, @NonNull ACATItemRequest request);
    Observable<ACATItemResponse> registerACATItem
            (@NonNull ACATItemRequest request) ;

//    Observable<ACATItemResponse> updateEstimatedValueOfACATItem
//            (@NonNull ACATItemDto acatItemItemDto, @NonNull String acatItemId);
//
//    Observable<ACATItemResponse> updateActualValueOfACATItem
//            (@NonNull ACATItemDto acatItemItemDto, @NonNull String acatItemId);

}

package com.gebeya.mobile.bidir.data.acatapplication.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/18/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ACATApplicationRepositorySource extends
        ReadableSource<ACATApplication>,
        FetchOne<ACATApplication>,
        FetchMany<ACATApplication>,
        ReadSize {

    Completable initializeClientACAT(@NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs);

    Completable updateACATApplication(@NonNull String acatApplciationId, @NonNull ACATApplicationRequest request);

    Observable<ACATApplication> changeClientACATStatus(@NonNull ACATApplication acatApplication, @NonNull String status);

    Observable<Boolean> submitACATApplication(@NonNull ACATApplication acatApplication);

    Observable<Boolean> approveACATApplication(@NonNull ACATApplication acatApplication);

    Observable<Boolean> declineACATApplication(@NonNull ACATApplication acatApplication, @NonNull String remark);

    Observable<ACATApplication> fetchByACATId(@NonNull String clientACATId);

    Observable<ACATApplication> fetchForceByACATId(@NonNull String clientACATId);

    Completable saveACATCostComponentsLocally(@NonNull ACATApplicationResponse response);

    Completable saveACATRevenueComponentsLocally(@NonNull ACATApplicationResponse response);

    Completable saveACATApplicationSync(@NonNull ACATApplicationResponse response);
}

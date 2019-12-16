package com.gebeya.mobile.bidir.data.acatitem.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemDto;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemRemoteSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocal;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by abuti on 6/5/2018.
 */

public class ACATItemRepository implements ACATItemRepositorySource{

    @Inject
    ACATItemRemoteSource remote;

    @Inject
    ACATItemLocalSource acatItemLocalSource;

    @Inject
    ACATItemValueLocalSource acatItemValueLocalSource;

    @Inject
    CashFlowLocalSource cashFlowLocalSource;

    @Override
    public Completable updateEstimatedValueOfACATItem (@NonNull ACATItemDto acatItemDto, @NonNull String acatItemId){
//        return remote.updateEstimatedValueOfACATItem(acatItemDto,acatItemId)
//                .flatMapCompletable(updated -> {
//                    acatItemLocalSource.put(updated.acatItem);
//                    acatItemValueLocalSource.put(updated.estimatedAcatItemValue);
//                    cashFlowLocalSource.put(updated.estimatedCashFlow);
//                    return Completable.complete();
//                });
        return null;
    }
}

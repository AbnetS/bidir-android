package com.gebeya.mobile.bidir.data.cashflow.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;

import java.util.List;

import io.reactivex.Observable;


/**
 * Interface class for a local storage for the {@link CashFlow} object.
 */

public interface CashFlowLocalSource extends ReadableSource<CashFlow>, WritableSource<CashFlow> {

    /**
     * Get a single cash flow, under the given referenceId and referenceType.
     * @param referenceId Reference ID to act as a filter.
     * @param type Reference Type to act as a filter between estimated, achieved and net.
     * @return Observable CashFlow Item that match the given parameters.
     */
    Observable<List<CashFlow>> get(@NonNull String referenceId, @NonNull String type, @NonNull String classification);

    Observable<CashFlow> put(@NonNull String referenceId, @NonNull String referenceType);

    /**
     * Put newly created cash flow using ACAT Item object box Id.
     * @param cashFlow cash flow object to be created.
     * @return Cash Flow.
     */
    Observable<CashFlow> putNewCashFlow(@NonNull CashFlow cashFlow);

    Observable<CashFlow> updateACATItemRefId(@NonNull CashFlow cashflow, String acatItemId);

}

package com.gebeya.mobile.bidir.data.marketdetails.local;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.marketdetails.MarketDetails;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by abuti on 5/15/2018.
 */

public interface MarketDetailsLocalSource extends ReadableSource<MarketDetails>, WritableSource<MarketDetails> {
    public Observable<List<MarketDetails>> getByYieldConsumption(String yieldConsumptionId, String type);

}

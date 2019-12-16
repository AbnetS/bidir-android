package com.gebeya.mobile.bidir.data.acatapplication;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;

import java.util.List;

/**
 * Created by abuti on 5/17/2018.
 */

public class ACATApplicationResponse {
    public ACATApplication acatApplication;
    public CashFlow estimatedNetCashFlow;
    public CashFlow actualNetCashFlow;
    public List<CropACATResponse> cropACATs;
}

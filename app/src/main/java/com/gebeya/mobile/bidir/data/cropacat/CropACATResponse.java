package com.gebeya.mobile.bidir.data.cropacat;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocationResponse;

/**
 * Created by abuti on 5/17/2018.
 */


public class CropACATResponse {
    public CropACAT cropACAT;

    public CashFlow estimatedNetCashFlow;
    public CashFlow actualNetCashFlow;

    public ACATCostSectionResponse costSection;
    public ACATRevenueSectionResponse revenueSection;

    public GPSLocationResponse gpsLocation;


}

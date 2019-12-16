package com.gebeya.mobile.bidir.data.marketdetails.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.gebeya.mobile.bidir.data.marketdetails.MarketDetails;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/15/2018.
 */

public interface MarketDetailsParser {

    /**
     * Parse the given JsonObject and return a {@link LoanProductItem} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @param type Estimated/Actual
     * @param yieldConsumptionID the ID of the yield consumption
     * @return Parsed Market details object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    MarketDetails parse(@NonNull JsonObject object, @NonNull String yieldConsumptionID, @NonNull String type)
            throws Exception;
}

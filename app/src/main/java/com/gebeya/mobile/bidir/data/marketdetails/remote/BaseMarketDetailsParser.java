package com.gebeya.mobile.bidir.data.marketdetails.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.marketdetails.MarketDetails;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/15/2018.
 */
public class BaseMarketDetailsParser implements MarketDetailsParser {

    @Override
    public MarketDetails parse(@NonNull JsonObject object, @NonNull String yieldConsumptionID, @NonNull String type)
            throws Exception{
        try{
            final MarketDetails marketDetails = new MarketDetails();

            marketDetails.toWhom = object.get("to_whom").getAsString();
            marketDetails.amount = object.get("amount").getAsDouble();

            if (object.has("remark"))
                marketDetails.remark = object.get("remark").getAsString();

            marketDetails.yieldConsumptionID = yieldConsumptionID;
            marketDetails.type = type;

            return marketDetails;

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Market Details: " + e.getMessage());
        }
    }
}

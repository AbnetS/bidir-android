package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.marketdetails.remote.BaseMarketDetailsResponseParser;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by abuti on 5/15/2018.
 */

public class BaseYieldConsumptionResponseParser implements YieldConsumptionResponseParser {
    @Inject
    YieldConsumptionParser yieldConsumptionParser;

    @Inject
    BaseMarketDetailsResponseParser marketDetailsResponseParser;

    @Inject
    public BaseYieldConsumptionResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public YieldConsumptionResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception {

        try {
            final YieldConsumptionResponse yieldConsumptionResponse = new YieldConsumptionResponse();

            yieldConsumptionResponse.yieldConsumption = yieldConsumptionParser.parse(object, parentSectionID);

            String yieldConsumptionID = object.get("_id").getAsString();
            JsonObject estimated = object.get("estimated").getAsJsonObject();
            yieldConsumptionResponse.estimatedMarketDetails =
                    marketDetailsResponseParser.parse(estimated.get("market_details").getAsJsonArray(), yieldConsumptionID, "ESTIMATED");

            JsonObject actual = object.get("achieved").getAsJsonObject();
            yieldConsumptionResponse.actualMarketDetails =
                    marketDetailsResponseParser.parse(actual.get("market_details").getAsJsonArray(),yieldConsumptionID,"ACTUAL");


            return yieldConsumptionResponse;


        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Yield Consumption Response: " + e.getMessage());
        }
    }
}

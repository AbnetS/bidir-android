package com.gebeya.mobile.bidir.data.marketdetails.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.marketdetails.MarketDetailsResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by abuti on 5/15/2018.
 */

public class BaseMarketDetailsResponseParser implements MarketDetailsResponseParser{
    @Inject MarketDetailsParser marketDetailsParser;

    @Inject
    public BaseMarketDetailsResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }
    @Override
    public MarketDetailsResponse parse(@NonNull JsonArray array, @NonNull String yieldConsumptionID, @NonNull String type)
            throws Exception{
        try{
            final MarketDetailsResponse marketDetailsResponse = new MarketDetailsResponse();
            marketDetailsResponse.marketDetails = new ArrayList<>();

            for (int i = 0;i < array.size();i++){
                JsonObject object = array.get(i).getAsJsonObject();
                marketDetailsResponse.marketDetails.add(marketDetailsParser.parse(object,yieldConsumptionID,type));

            }

            return marketDetailsResponse;

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Market Details Response: " + e.getMessage());
        }

    }
}

package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

/**
 * Created by abuti on 5/15/2018.
 */

public class BaseYieldConsumptionParser implements YieldConsumptionParser {

    @Override
    public YieldConsumption parse(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception{
        try{
            final YieldConsumption yieldConsumption = new YieldConsumption();

            yieldConsumption._id = object.get ("_id").getAsString();
            yieldConsumption.remark = object.get ("remark").getAsString();
            yieldConsumption.parentSectionID = parentSectionID;

            JsonObject estimated = object.get("estimated").getAsJsonObject();
            yieldConsumption.estimatedOwnCons = estimated.get("own_consumption").getAsDouble();
            yieldConsumption.estimatedSeedReserve = estimated.get("seed_reserve").getAsDouble();
            yieldConsumption.estimatedForMarket = estimated.get("for_market").getAsDouble();

            JsonObject actual = object.get("achieved").getAsJsonObject();
            yieldConsumption.actualOwnCons = actual.get("own_consumption").getAsDouble();
            yieldConsumption.actualSeedReserve = actual.get("seed_reserve").getAsDouble();
            yieldConsumption.actualForMarket = actual.get("for_market").getAsDouble();

            yieldConsumption.createdAt = new DateTime(object.get("date_created").getAsString());
            yieldConsumption.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return yieldConsumption;

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Yield Consumption: " + e.getMessage());
        }
    }
}

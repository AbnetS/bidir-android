package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by abuti on 8/29/2018.
 */

public class YieldConsumptionRemote extends BaseRemoteSource<YieldConsumptionService> implements YieldConsumptionRemoteSource{
    @Inject YieldConsumptionParser parser;

    public YieldConsumptionRemote(){
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.ACAT, YieldConsumptionService.class);
    }

    public Observable<YieldConsumption> updateYieldConsumption(@NonNull String yieldConsumptionId,
                                                                 @NonNull YieldConsumptionRequest request){
        return build()
                .call(service.update(yieldConsumptionId, createYieldConsumptionObject(request)))
                .map(object -> parser.parse(object, request.yieldConsumption.parentSectionID));
    }

    private JsonObject createYieldConsumptionObject(YieldConsumptionRequest request){
        final JsonObject object = new JsonObject();

        object.addProperty("remark", request.yieldConsumption.remark);

        JsonObject detail = new JsonObject();

        detail.addProperty("own_consumption", request.yieldConsumption.estimatedOwnCons);
        detail.addProperty("seed_reserve", request.yieldConsumption.estimatedSeedReserve);
        detail.addProperty("for_market", request.yieldConsumption.estimatedForMarket);
        object.add("estimated", detail);
        if (request.estimatedMarketDetails != null){
            JsonArray marketDetailArray = new JsonArray();
            JsonObject marketDetailObject = new JsonObject();
            if (request.estimatedMarketDetails.marketDetails.size() != 0){
                for (int i=0;i < request.estimatedMarketDetails.marketDetails.size(); i++){
                    marketDetailObject.addProperty("to_whom", request.estimatedMarketDetails.marketDetails.get(i).toWhom);
                    marketDetailObject.addProperty("amount", request.estimatedMarketDetails.marketDetails.get(i).amount);
                    marketDetailObject.addProperty("remark", request.estimatedMarketDetails.marketDetails.get(i).remark);

                    marketDetailArray.add(marketDetailObject);
                    marketDetailObject = new JsonObject();

                }
                detail.add("market_details", marketDetailArray);


            }
        }

        else if (request.actualMarketDetails != null){
            detail.addProperty("own_consumption", request.yieldConsumption.actualOwnCons);
            detail.addProperty("seed_reserve", request.yieldConsumption.actualSeedReserve);
            detail.addProperty("for_market", request.yieldConsumption.actualForMarket);

            JsonArray marketDetailArray = new JsonArray();
            JsonObject marketDetailObject = new JsonObject();
            if (request.actualMarketDetails.marketDetails.size() != 0){
                for (int i=0;i < request.actualMarketDetails.marketDetails.size(); i++){
                    marketDetailObject.addProperty("to_whom", request.actualMarketDetails.marketDetails.get(i).toWhom);
                    marketDetailObject.addProperty("amount", request.actualMarketDetails.marketDetails.get(i).amount);
                    marketDetailObject.addProperty("remark", request.actualMarketDetails.marketDetails.get(i).remark);

                    marketDetailArray.add(marketDetailObject);
                    marketDetailObject = new JsonObject();

                }
                detail.add("market_details", marketDetailArray);

                object.add("achieved", detail);
            }
        }
        return object;
    }
}

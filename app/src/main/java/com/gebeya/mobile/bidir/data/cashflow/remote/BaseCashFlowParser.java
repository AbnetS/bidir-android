package com.gebeya.mobile.bidir.data.cashflow.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by abuti on 5/11/2018.
 */

public class BaseCashFlowParser implements CashFlowParser {

    @Inject
    public BaseCashFlowParser() {
    }

    @Override
    public CashFlow parse(@NonNull JsonObject object, @NonNull String referenceId, @NonNull String type,
                          @NonNull String classification) throws Exception{
        try{
            final CashFlow cashFlow = new CashFlow();

            cashFlow.january = object.get("jan").getAsDouble();
            cashFlow.february = object.get("feb").getAsDouble();
            cashFlow.march = object.get("mar").getAsDouble();
            cashFlow.april = object.get("apr").getAsDouble();
            cashFlow.may = object.get("may").getAsDouble();
            cashFlow.june = object.get("june").getAsDouble();
            cashFlow.july = object.get("july").getAsDouble();
            cashFlow.august = object.get("aug").getAsDouble();
            cashFlow.september = object.get("sep").getAsDouble();
            cashFlow.october = object.get("oct").getAsDouble();
            cashFlow.november = object.get("nov").getAsDouble();
            cashFlow.december = object.get("dec").getAsDouble();

            cashFlow.referenceId = referenceId;
            cashFlow.type = type;
            cashFlow.classification = classification;

            return cashFlow;


        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Error parsing Cashflow: " + e.getMessage());
        }
        
    }
}

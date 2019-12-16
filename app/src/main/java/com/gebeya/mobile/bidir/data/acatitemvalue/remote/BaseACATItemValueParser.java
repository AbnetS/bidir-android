package com.gebeya.mobile.bidir.data.acatitemvalue.remote;


import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Concrete implementation for the {@link ACATItemValueParser} interface.
 */

public class BaseACATItemValueParser implements ACATItemValueParser{

    @Inject
    public BaseACATItemValueParser() {
    }

    @Override
    public ACATItemValue parse (@NonNull JsonObject object, @NonNull String acatItemId, @NonNull String type) throws Exception {
        try{
            final ACATItemValue acatItemValue = new ACATItemValue();

            acatItemValue.value = object.get("value").getAsDouble();
            acatItemValue.unitPrice = object.get("unit_price").getAsDouble();
            acatItemValue.totalPrice = object.get("total_price").getAsDouble();

            acatItemValue.acatItemId = acatItemId;
            acatItemValue.type = type;

            return acatItemValue;

        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Item: " + e.getMessage());
        }
    }
}

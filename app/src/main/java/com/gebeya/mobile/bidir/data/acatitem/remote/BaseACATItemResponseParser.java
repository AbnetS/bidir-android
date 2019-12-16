package com.gebeya.mobile.bidir.data.acatitem.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.remote.BaseACATItemValueParser;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.remote.BaseCashFlowParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by abuti on 5/13/2018.
 */

public class BaseACATItemResponseParser implements ACATItemResponseParser {

    @Inject
    BaseACATItemParser acatItemParser;

    @Inject
    BaseCashFlowParser cashFlowParser;

    @Inject
    BaseACATItemValueParser acatItemValueParser;

    @Inject
    public BaseACATItemResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
    }


    @Override
    public ACATItemResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID,
                                  @Nullable String costListID, @Nullable String groupedListID, @NonNull String category) throws Exception {
        final ACATItemResponse response = new ACATItemResponse();

        try {
            response.acatItem = acatItemParser.parse(object, category, parentSectionID, costListID, groupedListID);

            final String acatItemId = object.get("_id").getAsString();

            //getByType the embeded estimated object from the ACATItem object
            final JsonObject estimated = object.get("estimated").getAsJsonObject();
            //getByType the embeded cash flow object within 'estimated'
            final JsonObject estimatedCashFlow = estimated.get("cash_flow").getAsJsonObject();

            //parseACATApplication the estimated cashflow passing the item id as a reference
            response.estimatedCashFlow = cashFlowParser.parse(estimatedCashFlow, acatItemId,
                    CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.ITEM_CASH_FLOW);
            //parseACATApplication the estimated item values passing the item id as a reference
            response.estimatedAcatItemValue = acatItemValueParser.parse(estimated, acatItemId,
                    ACATItemValueHelper.ESTIMATED_VALUE_TYPE);


            final JsonObject actual = object.get("achieved").getAsJsonObject();
            final JsonObject actualCashFlow = actual.get("cash_flow").getAsJsonObject();

            //parseACATApplication the actual cashflow passing the item id as a reference
            response.actualCashFlow = cashFlowParser.parse(actualCashFlow, acatItemId,
                    CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.ITEM_CASH_FLOW);

            //parseACATApplication the actual item values passing the item id as a reference
            response.actualAcatItemValue = acatItemValueParser.parse(actual, acatItemId,
                    ACATItemValueHelper.ACTUAL_VALUE_TYPE);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}

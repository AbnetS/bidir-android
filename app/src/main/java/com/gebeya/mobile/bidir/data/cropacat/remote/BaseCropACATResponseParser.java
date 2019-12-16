package com.gebeya.mobile.bidir.data.cropacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.remote.BaseACATCostSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.BaseACATRevenueSectionResponseParser;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.remote.CashFlowParser;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.gebeya.mobile.bidir.data.gpslocation.remote.GPSLocationResponseParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by abuti on 5/17/2018.
 */


public class BaseCropACATResponseParser implements CropACATResponseParser {

    @Inject
    CropACATParser cropACATParser;
    @Inject
    BaseACATCostSectionResponseParser acatCostSectionResponseParser;
    @Inject
    BaseACATRevenueSectionResponseParser acatRevenueSectionResponseParser;
    @Inject
    CashFlowParser cashFlowParser;
    @Inject
    GPSLocationResponseParser gpsLocationParser;

    @Inject
    public BaseCropACATResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }


    @Override
    public CropACATResponse parse(@NonNull JsonObject object, @NonNull String acatApplciationID) throws Exception {

        final CropACATResponse response = new CropACATResponse();
        try {
            response.cropACAT = cropACATParser.parse(object, acatApplciationID);

            //Parse the sections
            final JsonArray cropACATSections = object.get("sections").getAsJsonArray();

            final JsonObject costSection = cropACATSections.get(0).getAsJsonObject(); //Get the first section as the cost section
            final JsonObject revenueSection = cropACATSections.get(1).getAsJsonObject(); //Get the second section as the revenue section

//            for (int i = 0; i < cropACATSections.size(); i++){
//                final JsonObject sections = cropACATSections.getByType(i).getAsJsonObject();
//                final JsonArray cropACATSubSections = sections.getByType("sub_sections").getAsJsonArray();
//                final JsonObject costSection = cropACATSubSections.getByType(0).getAsJsonObject(); //Get the first section as the cost section
//                final JsonObject revenueSection = cropACATSubSections.getByType(1).getAsJsonObject();
//                pageResponse.costSection = acatCostSectionResponseParser.parse (costSection);
//                pageResponse.revenueSection = acatRevenueSectionResponseParser.parse (revenueSection);
//            }
            response.costSection = acatCostSectionResponseParser.parse(costSection);
            response.revenueSection = acatRevenueSectionResponseParser.parse(revenueSection);
//////////////////////////////////////////////////////////////////////////////////////////////

//Parse the cashflows///////////////////////////////////////////
            final JsonObject estimated = object.get("estimated").getAsJsonObject();
            response.estimatedNetCashFlow =  cashFlowParser.parse (estimated.get("net_cash_flow").getAsJsonObject(),
                    object.get("_id").getAsString(), CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE);

            final JsonObject actual = object.get("achieved").getAsJsonObject();
            response.actualNetCashFlow =  cashFlowParser.parse (actual.get("net_cash_flow").getAsJsonObject(),
                    object.get("_id").getAsString(), CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //pageResponse.gpsLocation =
            JsonObject gpsObject = object.get("gps_location").getAsJsonObject();
            response.gpsLocation = gpsLocationParser.parse(gpsObject, acatApplciationID, object.get("_id").getAsString());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}







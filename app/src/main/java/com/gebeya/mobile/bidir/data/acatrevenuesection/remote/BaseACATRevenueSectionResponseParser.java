package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemHelper;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitem.remote.BaseACATItemResponseParser;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.remote.CashFlowParser;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.BaseYieldConsumptionResponseParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abuti on 5/15/2018.
 */

public class BaseACATRevenueSectionResponseParser implements ACATRevenueSectionResponseParser {

    @Inject
    ACATRevenueSectionParser acatRevenueSectionParser;

    @Inject
    CashFlowParser cashFlowParser;

    @Inject
    BaseACATItemResponseParser acatItemResponseParser;

    @Inject
    BaseYieldConsumptionResponseParser yieldConsumptionResponseParser;

    @Inject
    public BaseACATRevenueSectionResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public ACATRevenueSectionResponse parse(@NonNull JsonObject object) throws Exception {
        try {
            final ACATRevenueSectionResponse acatRevenueSectionResponse = new ACATRevenueSectionResponse();

            acatRevenueSectionResponse.sections = parseAllSections(object);
            acatRevenueSectionResponse.estimatedCashFlows = parseAllCashFlows(object, "estimated_cash_flow");
            acatRevenueSectionResponse.actualCashFlows = parseAllCashFlows(object, "achieved_cash_flow");

            acatRevenueSectionResponse.yields = parseAllYields(object);
            acatRevenueSectionResponse.yieldConsumptions = parseAllYieldConsumptions(object);

            return acatRevenueSectionResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Revenue Section: " + e.getMessage());
        }
    }

    @Override
    public List<ACATRevenueSection> parseAllSections(@NonNull JsonObject object) throws Exception {
        final List<ACATRevenueSection> sections = new ArrayList<>();
        sections.add(acatRevenueSectionParser.parse(object, null));

        //This is to parseACATApplication the subsections
        final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();

        if (subSectionsArray.size() != 0) {
            for (int i = 0; i < subSectionsArray.size(); i++) {
                final JsonObject sectionObject = subSectionsArray.get(i).getAsJsonObject();
                sections.add(acatRevenueSectionParser.parse(sectionObject, object.get("_id").getAsString()));
            }


        }
        return sections;
    }

    @Override
    public List<CashFlow> parseAllCashFlows(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception {
        List<CashFlow> cashFlows = new ArrayList<>();

        cashFlows.add(parseSectionCashFlow(object, whatToParse));

        //This is to parseACATApplication the cash flow of subsection
        final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();

        if (subSectionsArray.size() != 0) {
            for (int i = 0; i < subSectionsArray.size(); i++) {
                final JsonObject subSectionObject = subSectionsArray.get(i).getAsJsonObject();
                cashFlows.add(parseSectionCashFlow(subSectionObject, whatToParse));
            }
        }

        return cashFlows;
    }

    @Override
    public CashFlow parseSectionCashFlow(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception {
        CashFlow cashFlow = new CashFlow();

        JsonObject cashFlowObject = object.get(whatToParse).getAsJsonObject();
        String sectionID = object.get("_id").getAsString();
        if (whatToParse.equals("estimated_cash_flow"))
            cashFlow = cashFlowParser.parse(cashFlowObject, sectionID, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
        else
            cashFlow = cashFlowParser.parse(cashFlowObject, sectionID, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);

        return cashFlow;
    }

    @Override
    public List<ACATItemResponse> parseAllYields(@NonNull JsonObject object) throws Exception {
        List<ACATItemResponse> yields = new ArrayList<>();

        if (object.has("yield")) {
            String sectionID = object.get("_id").getAsString();
            yields.add(acatItemResponseParser.parse(object, sectionID, null, null, ACATItemHelper.REVENUE_CATEGORY));
        }

        //This is to parse the yield parameter of subsections
        final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();

        if (subSectionsArray.size() != 0) {
            for (int i = 0; i < subSectionsArray.size(); i++) {
                final JsonObject subSectionsObject = subSectionsArray.get(i).getAsJsonObject();
                if (subSectionsObject.has("yield")) {
                    String sectionID = subSectionsObject.get("_id").getAsString();
                    final JsonObject yieldObject = subSectionsObject.get("yield").getAsJsonObject();
                    yields.add(acatItemResponseParser.parse(yieldObject, sectionID, null, null, ACATItemHelper.REVENUE_CATEGORY));
                }
            }
        }

        return yields;
    }

    @Override
    public List<YieldConsumptionResponse> parseAllYieldConsumptions(@NonNull JsonObject object) throws Exception {
        List<YieldConsumptionResponse> yieldConsumptions = new ArrayList<>();

        if (object.has("yield_consumption")) {
            String sectionID = object.get("_id").getAsString();
            JsonObject yieldConsumptionObject = object.get("yield_consumption").getAsJsonObject();
            yieldConsumptions.add(yieldConsumptionResponseParser.parse
                    (yieldConsumptionObject, sectionID));
        }

        //This is to parseACATApplication the yield consumption parameter of subsections
        final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();

        if (subSectionsArray.size() != 0) {
            for (int i = 0; i < subSectionsArray.size(); i++) {
                final JsonObject subSectionsObject = subSectionsArray.get(i).getAsJsonObject();
                if (subSectionsObject.has("yield_consumption")) {
                    String sectionID = subSectionsObject.get("_id").getAsString();
                    JsonObject yieldConsumptionObject = subSectionsObject.get("yield_consumption").getAsJsonObject();
                    yieldConsumptions.add(yieldConsumptionResponseParser.parse
                            (yieldConsumptionObject, sectionID));
                }
            }
        }

        return yieldConsumptions;
    }


}

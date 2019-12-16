package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.remote.BaseCashFlowParser;
import com.gebeya.mobile.bidir.data.costlist.CostListResponse;
import com.gebeya.mobile.bidir.data.costlist.remote.BaseCostListResponseParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abuti on 5/12/2018.
 */

public class BaseACATCostSectionResponseParser implements ACATCostSectionResponseParser {

    @Inject
    BaseACATCostSectionParser acatCostSectionParser;

    @Inject
    BaseCashFlowParser cashFlowParser;

    @Inject
    BaseCostListResponseParser costListResponseParser;

    @Inject
    public BaseACATCostSectionResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public ACATCostSectionResponse parse(@NonNull JsonObject object) throws Exception {
        try {
            final ACATCostSectionResponse response = new ACATCostSectionResponse();

            response.sections = parseAllSections(object);

            response.estimatedCashFlows = parseAllCashFlows(object, "estimated_cash_flow");
            response.actualCashFlows = parseAllCashFlows(object, "achieved_cash_flow");

            response.costLists = parseAllCostLists(object);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Cost Section Response: " + e.getMessage());
        }
    }


    @Override
    public List<ACATCostSection> parseAllSections(@NonNull JsonObject object) throws Exception {
        final List<ACATCostSection> sections = new ArrayList<>();
        sections.add(acatCostSectionParser.parse(object, null));

        //This is to parseACATApplication the subsections
        final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();
//        final JsonObject subSectionsObject = object.getByType("sub_sections").getAsJsonObject();

        if (subSectionsArray.size() != 0) {
            sections.addAll(parseSubSections(subSectionsArray, object.get("_id").getAsString()));

        }

        return sections;
    }

    @Override
    public ACATCostSectionResponse parseSingleSection(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception{

        try {
            final ACATCostSectionResponse response = new ACATCostSectionResponse();
            response.sections = new ArrayList<>();
            response.estimatedCashFlows = new ArrayList<>();
            response.actualCashFlows = new ArrayList<>();

            final List<ACATCostSection> sections = new ArrayList<>();

            sections.add(acatCostSectionParser.parse(object,parentSectionID));

            response.sections = sections;

            response.estimatedCashFlows.add(parseSectionCashFlow(object, "estimated_cash_flow"));
            response.actualCashFlows.add(parseSectionCashFlow(object, "achieved_cash_flow"));

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Cost Section Response: " + e.getMessage());
        }


    }

    //    @Override
//    public List<ACATCostSection> parseSubSections(@NonNull JsonArray array, @NonNull String parentSectionID) throws Exception {
//        final List<ACATCostSection> responses = new ArrayList<>();
//        final int length = array.size();
//        for (int i = 0; i < length; i++) {
//            final JsonObject sectionObject = array.getByType(i).getAsJsonObject();
//            responses.add (acatCostSectionParser.parse (sectionObject, parentSectionID));
//
//            final JsonObject subSectionObject = sectionObject.getAsJsonObject();
//            if (!subSectionObject.isJsonNull()){
//                //parseACATApplication the nested subsection
//                responses.add (acatCostSectionParser.parse (subSectionObject, sectionObject.getByType("_id").getAsString()));
//
//            }
//        }
//
//        return responses;
//    }
    @Override
    public List<ACATCostSection> parseSubSections(@NonNull JsonArray array, @NonNull String parentSectionID) throws Exception {
        final List<ACATCostSection> responses = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject sectionObject = array.get(i).getAsJsonObject();
            responses.add(acatCostSectionParser.parse(sectionObject, parentSectionID));

            final JsonArray jsonArray = sectionObject.get("sub_sections").getAsJsonArray();
            if (jsonArray.size() != 0)
                responses.addAll(parseNestedSubSections(jsonArray, sectionObject.get("_id").getAsString()));
        }

//            final JsonObject subSectionObject = sectionObject.getAsJsonObject();
//            if (!subSectionObject.isJsonNull()){
//                //parseACATApplication the nested subsection
//                try {
//                    final JsonArray jsonArray = subSectionObject.getByType("sub_sections").getAsJsonArray();
//                    if (jsonArray.size() != 0)
//                        responses.addAll(parseNestedSubSections(jsonArray, subSectionObject.getByType("_id").getAsString()));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                responses.add (acatCostSectionParser.parse (subSectionObject, sectionObject.getByType("_id").getAsString()));
//
//            }
        //}

        return responses;
    }

    private List<ACATCostSection> parseNestedSubSections(@NonNull JsonArray array, @NonNull String parentSectionID) throws Exception {
        final List<ACATCostSection> responses = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject sectionObject = array.get(i).getAsJsonObject();
            responses.add(acatCostSectionParser.parse(sectionObject, parentSectionID));
//            final JsonObject subSectionObject = sectionObject.getByType("sub_sections").getAsJsonObject();
//            if (!subSectionObject.isJsonNull()){
//                //parseACATApplication the nested subsection
//                responses.add (acatCostSectionParser.parse (subSectionObject, sectionObject.getByType("_id").getAsString()));
//
//            }
        }

        return responses;
    }

    @Override
    public List<CashFlow> parseAllCashFlows(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception {
        List<CashFlow> CashFlows = new ArrayList<>();

        CashFlows.add(parseSectionCashFlow(object, whatToParse));

        //This is to parseACATApplication the cash flow of subsection
        final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();
        //final JsonObject subSectionsObject = object.getByType("sub_sections").getAsJsonObject();

        if (subSectionsArray.size() != 0) {
            for (int i = 0; i < subSectionsArray.size(); i++) {
                final JsonObject subSectionsObject = subSectionsArray.get(i).getAsJsonObject();
                CashFlows.add(parseSectionCashFlow(subSectionsObject, whatToParse));

                //To parseACATApplication the estimated cashflow of nested subsection, like in the case of the Input section
                final JsonArray nestedSubSectionArray = subSectionsObject.get("sub_sections").getAsJsonArray();
                if (nestedSubSectionArray.size() != 0) {
                    for (int j = 0; j < (nestedSubSectionArray.size()); j++)
                        CashFlows.add(parseSectionCashFlow(nestedSubSectionArray.get(j).getAsJsonObject(), whatToParse));
                }
            }

        }

        return CashFlows;
    }

    @Override
    public CashFlow parseSectionCashFlow(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception {
        CashFlow cashFlow;

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
    public List<CostListResponse> parseAllCostLists(@NonNull JsonObject object) throws Exception {
        final List<CostListResponse> costLists = new ArrayList<>();
        try {

            final String sectionID = object.get("_id").getAsString();
            if (object.has("cost_list")) {
                costLists.add(costListResponseParser.parse(object.get("cost_list").getAsJsonObject(), sectionID));
            }

            //This is to parseACATApplication the cost list of subsections if any
            final JsonArray subSectionsArray = object.get("sub_sections").getAsJsonArray();
            //final JsonObject subSectionsObject = object.getByType("sub_sections").getAsJsonObject();

            if (subSectionsArray.size() != 0) {
                for (int i = 0; i < subSectionsArray.size(); i++) {
                    final JsonObject subSectionObject = subSectionsArray.get(i).getAsJsonObject();
                    if (subSectionObject.has("cost_list")) {
                        //getByType the embeded cost list object from the section object
                        /*
                         * This is where sub_sections like "Labor cost" are parsed.
                         */
                        final JsonObject costList = subSectionObject.get("cost_list").getAsJsonObject();
                        final String subSectionID = subSectionObject.get("_id").getAsString();

                        costLists.add(costListResponseParser.parse(costList, subSectionID));
                    }

                    //To parseACATApplication the cost list of nested subsection, like in the case of the Input section
                    final JsonArray nestedSubSectionArray = subSectionObject.get("sub_sections").getAsJsonArray();
                    if (nestedSubSectionArray.size() != 0) {
                        for (int j = 0; j < (nestedSubSectionArray.size()); j++) {
                            final JsonObject nestedSubSectionObject = nestedSubSectionArray.get(j).getAsJsonObject();
                            if (nestedSubSectionObject.has("cost_list")) {
                                final String nestedSubSectionID = nestedSubSectionObject.get("_id").getAsString();
                                final JsonObject costListObject = nestedSubSectionObject.get("cost_list").getAsJsonObject();
                                costLists.add(costListResponseParser.parse(costListObject, nestedSubSectionID));
                            }
                        }
                    }

                }
            }


            return costLists;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Cost List: " + e.getMessage());
        }

    }


}

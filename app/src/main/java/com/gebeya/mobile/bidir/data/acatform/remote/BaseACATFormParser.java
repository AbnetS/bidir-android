package com.gebeya.mobile.bidir.data.acatform.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abuti on 5/12/2018.
 */
public class BaseACATFormParser implements ACATFormParser{

    @Override
    public ACATForm parse(@NonNull JsonObject object) throws Exception {
        try {
            final ACATForm acatForm = new ACATForm();

            acatForm._id = object.get("_id").getAsString();
            acatForm.type = object.get("type").getAsString();
            acatForm.createdBy = object.get("created_by").getAsString();
            acatForm.title = object.get("title").getAsString();
            acatForm.layout = object.get("layout").getAsString();

            final JsonObject estimated = object.get("estimated").getAsJsonObject();
            acatForm.estimatedTotalRevenue = estimated.get ("total_revenue").getAsDouble();
            acatForm.estimatedTotalCost = estimated.get ("total_cost").getAsDouble();
            acatForm.estimatedNetIncome = estimated.get ("net_income").getAsDouble();

            final JsonObject actual = object.get("achieved").getAsJsonObject();
            acatForm.actualTotalCost = actual.get ("total_revenue").getAsDouble();
            acatForm.actualTotalCost = actual.get ("total_cost").getAsDouble();
            acatForm.actualNetIncome = actual.get ("net_income").getAsDouble();

            acatForm.firstExpenseMonth = object.get("first_expense_month").getAsString();
            acatForm.accessToNonFinServices = object.get("access_to_non_financial_resources").getAsBoolean();

            acatForm.NonFinServices = toList (object.get("non_financial_resources").getAsJsonArray());
            acatForm.croppingArea = object.get("cropping_area_size").getAsString();

            final JsonArray sections = object.get("sections").getAsJsonArray();
            acatForm.costSectionId = (sections.get(0).getAsJsonObject()).get("_id").getAsString(); //the first section is always the cost section
            acatForm.costSectionId = (sections.get(1).getAsJsonObject()).get("_id").getAsString(); //The second section is always the revenue section


            return acatForm;
        } catch (Exception e) {
            throw new Exception("Error parsing ACAT Form: " + e);
        }
    }

    @Override
    public List<String> toList(@NonNull JsonArray array) {
        final List<String> list = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonElement element = array.get(i);
            if (element != null && !element.isJsonNull()) {
                final String s = element.getAsString();
                list.add(s);
            }
        }
        return list;
    }


}

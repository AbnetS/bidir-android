package com.gebeya.mobile.bidir.data.form.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

/**
 * Implementation for the {@link ComplexFormParser} parser.
 */
public class BaseComplexFormParser implements ComplexFormParser {

    @Override
    public ComplexForm parse(@NonNull JsonObject object) throws Exception {
        try {
            final ComplexForm form = new ComplexForm();

            form._id = object.get("_id").getAsString();
            final String apiType = object.get("type").getAsString();
            final String localType = getLocalType(apiType);
            if (localType.equals(TYPE_UNKNOWN)) throw new Exception("Unknown API ComplexForm type: " + apiType);
            form.type = localType;

            form.createdAt = new DateTime(object.get("date_created").getAsString());
            form.updatedAt  = new DateTime(object.get("last_modified").getAsString());

            form.createdBy = object.get("created_by").getAsString();
            form.hasSections = object.get("has_sections").getAsBoolean();

            final String apiLayout = object.get("layout").getAsString();
            final String localLayout = getLocalLayout(apiLayout);
            if (localLayout.equals(LAYOUT_UNKNOWN)) throw new Exception("Unknown API ComplexForm layout: " + apiLayout);
            form.layout = localLayout;

            form.purpose = object.get("purpose").getAsString();
            form.title = object.get("title").getAsString();
            form.subtitle = object.get("subtitle").getAsString();

            return form;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ComplexForm: " + e.getMessage());
        }
    }

    @Override
    public String getLocalType(@NonNull String apiType) {
        return getType(apiType, false);
    }

    @Override
    public String getApiType(@NonNull String localType) {
        return getType(localType, true);
    }

    private String getType(@NonNull String value, boolean localValue) {
        switch (value) {
            case TYPE_LOCAL_ACAT:
            case TYPE_API_ACAT:
                return localValue ? TYPE_API_ACAT : TYPE_LOCAL_ACAT;
            case TYPE_LOCAL_TEST:
            case TYPE_API_TEST:
                return localValue ? TYPE_API_TEST : TYPE_LOCAL_TEST;
            case TYPE_LOCAL_GROUP_APPLICATION:
            case TYPE_API_GROUP_APPLICATION:
                return localValue ? TYPE_API_GROUP_APPLICATION : TYPE_LOCAL_GROUP_APPLICATION;
            case TYPE_LOCAL_LOAN_APPLICATION:
            case TYPE_API_LOAN_APPLICATION:
                return localValue ? TYPE_API_LOAN_APPLICATION : TYPE_LOCAL_LOAN_APPLICATION;
            case TYPE_LOCAL_SCREENING:
            case TYPE_API_SCREENING:
                return localValue ? TYPE_API_SCREENING : TYPE_LOCAL_SCREENING;
                default:
                    return TYPE_UNKNOWN;
        }
    }

    @Override
    public String getLocalLayout(@NonNull String apiLayout) {
        switch (apiLayout) {
            case LAYOUT_API_TWO_COLUMNS:
                return LAYOUT_LOCAL_TWO_COLUMNS;
            case LAYOUT_API_THREE_COLUMNS:
                return LAYOUT_LOCAL_THREE_COLUMNS;
            default:
                return LAYOUT_UNKNOWN;
        }
    }

    @Override
    public String getApiLayout(@NonNull String localLayout) {
        switch (localLayout) {
            case LAYOUT_LOCAL_TWO_COLUMNS:
                return LAYOUT_API_TWO_COLUMNS;
            case LAYOUT_LOCAL_THREE_COLUMNS:
                return LAYOUT_API_THREE_COLUMNS;
            default:
                return LAYOUT_UNKNOWN;
        }
    }
}

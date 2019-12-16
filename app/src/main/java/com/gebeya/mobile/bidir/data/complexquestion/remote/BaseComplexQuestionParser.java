package com.gebeya.mobile.bidir.data.complexquestion.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation for a {@link ComplexQuestionParser} parser.
 */
public class BaseComplexQuestionParser implements ComplexQuestionParser {

    @Override
    public ComplexQuestion parse(@NonNull JsonObject object,
                                 @NonNull String referenceId,
                                 @NonNull String referenceType) throws Exception {
        try {
            final ComplexQuestion question = new ComplexQuestion();

            question._id = object.get("_id").getAsString();
            question.referenceId = referenceId;
            question.referenceType = referenceType;
            question.text = object.get("question_text").getAsString();

            final JsonArray prerequisitesArray = object.get("prerequisites").getAsJsonArray();
            question.hasPrerequisites = prerequisitesArray.size() != 0;

            question.show = object.get("show").getAsBoolean();

            final JsonArray valuesArray = object.get("values").getAsJsonArray();
            question.values = toList(valuesArray);

            final JsonArray subQuestionsArray = object.get("sub_questions").getAsJsonArray();
            question.subQuestions = getSubQuestionIds(subQuestionsArray);

            final JsonArray optionsArray = object.get("options").getAsJsonArray();
            question.options = toList(optionsArray);

            final JsonElement measurementUnitObject = object.get("measurement_unit");
            question.measurementUnit = measurementUnitObject == null ||
                    measurementUnitObject.isJsonNull() ? "-" : measurementUnitObject.getAsString();

            final String apiValidationFactor = object.get("validation_factor").getAsString();
            final String localValidationFactor = getLocalValidationFactor(apiValidationFactor);
            if (localValidationFactor.equals(VALIDATION_FACTOR_UNKNOWN)) {
                throw new Exception("Unknown API ComplexQuestion validation factor: " + apiValidationFactor);
            }
            question.validationFactor = localValidationFactor;

            question.required = object.get("required").getAsBoolean();

            final String apiType = object.get("type").getAsString();
            final String localType = getLocalType(apiType);
            if (localType.equals(TYPE_UNKNOWN))
                throw new Exception("Unknown API ComplexQuestion type: " + apiType);
            question.type = localType;

            question.remark = object.get("remark").getAsString();
            question.number = object.get("number").getAsInt();

            return question;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ComplexQuestion: " + e.getMessage());
        }
    }

    @Override
    public List<String> getSubQuestionIds(@NonNull JsonArray array) {
        final List<String> ids = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final String id = object.get("_id").getAsString();
            ids.add(id);
        }
        return ids;
    }

    @Override
    public JsonArray toArray(@NonNull List<String> list) {
        final JsonArray array = new JsonArray();
        final int length = list.size();
        for (int i = 0; i < length; i++) {
            final String s = list.get(i).trim();
            if (!s.isEmpty()) {
                array.add(s);
            }
        }
        return array;
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

    @Override
    public String getLocalValidationFactor(@NonNull String apiValidationFactor) {
        switch (apiValidationFactor) {
            case VALIDATION_FACTOR_API_NONE:
                return VALIDATION_FACTOR_LOCAL_NONE;
            case VALIDATION_FACTOR_API_ALPHANUMERIC:
                return VALIDATION_FACTOR_LOCAL_ALPHANUMERIC;
            case VALIDATION_FACTOR_API_NUMERIC:
                return VALIDATION_FACTOR_LOCAL_NUMERIC;
            case VALIDATION_FACTOR_API_ALPHABETIC:
                return VALIDATION_FACTOR_LOCAL_ALPHABETIC;
            default:
                return VALIDATION_FACTOR_UNKNOWN;
        }
    }

    @Override
    public String getApiValidationFactor(@NonNull String localValidationFactor) {
        switch (localValidationFactor) {
            case VALIDATION_FACTOR_LOCAL_NONE:
                return VALIDATION_FACTOR_API_NONE;
            case VALIDATION_FACTOR_LOCAL_ALPHANUMERIC:
                return VALIDATION_FACTOR_API_ALPHANUMERIC;
            case VALIDATION_FACTOR_LOCAL_NUMERIC:
                return VALIDATION_FACTOR_API_NUMERIC;
            case VALIDATION_FACTOR_LOCAL_ALPHABETIC:
                return VALIDATION_FACTOR_API_ALPHABETIC;
            default:
                return VALIDATION_FACTOR_UNKNOWN;
        }
    }

    @Override
    public String getLocalType(@NonNull String apiType) {
        switch (apiType) {
            case TYPE_API_YES_NO:
                return TYPE_LOCAL_YES_NO;
            case TYPE_API_FILL_IN_BLANK:
                return TYPE_LOCAL_FILL_IN_BLANK;
            case TYPE_API_MULTIPLE_CHOICE:
                return TYPE_LOCAL_MULTIPLE_CHOICE;
            case TYPE_API_SINGLE_CHOICE:
                return TYPE_LOCAL_SINGLE_CHOICE;
            case TYPE_API_GROUPED:
                return TYPE_LOCAL_GROUPED;
            default:
                return TYPE_UNKNOWN;
        }
    }

    @Override
    public String getApiType(@NonNull String localType) {
        switch (localType) {
            case TYPE_LOCAL_YES_NO:
                return TYPE_API_YES_NO;
            case TYPE_LOCAL_FILL_IN_BLANK:
                return TYPE_API_FILL_IN_BLANK;
            case TYPE_LOCAL_MULTIPLE_CHOICE:
                return TYPE_API_MULTIPLE_CHOICE;
            case TYPE_LOCAL_SINGLE_CHOICE:
                return TYPE_API_SINGLE_CHOICE;
            case TYPE_LOCAL_GROUPED:
                return TYPE_API_GROUPED;
            default:
                return TYPE_UNKNOWN;
        }
    }

    @Override
    public JsonObject createQuestion(@NonNull ComplexQuestion question) {
        final JsonObject object = new JsonObject();

        object.addProperty("_id", question._id);
        object.addProperty("question_text", question.text);
        object.addProperty("show", question.show);
        object.add("values", toArray(question.values));
        object.add("options", toArray(question.options));

        final JsonElement measurementUnit = question.measurementUnit.equals("-") ? JsonNull.INSTANCE : new JsonPrimitive(question.measurementUnit);
        object.add("measurement_unit", measurementUnit);
        object.addProperty("validation_factor", getApiValidationFactor(question.validationFactor));
        object.addProperty("required", question.required);
        object.addProperty("type", getApiType(question.type));
        object.addProperty("remark", question.remark);
        object.addProperty("number", question.number);

        return object;
    }
}
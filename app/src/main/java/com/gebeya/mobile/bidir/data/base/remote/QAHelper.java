package com.gebeya.mobile.bidir.data.base.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.question.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Interface for a class that is used to help in the conversion of a Question/Answer to pojo/json
 * and vice versa.
 */
public interface QAHelper {

    int TYPE_INPUT = 100;
    String API_TYPE_INPUT = "Fill In Blank";

    int TYPE_MULTIPLE_CHOICE_SINGLE = 101;
    String API_TYPE_MULTIPLE_CHOICE_SINGLE = "Yes/No";

    int TYPE_MULTIPLE_CHOICE_MULTIPLE = 102;
    String API_TYPE_MULTIPLE_CHOICE_MULTIPLE = "Multiple Choice";

    String REF_TYPE_SCREENING = "screening";
    String REF_TYPE_LOAN_APPLICATION = "loan_application";

    String V_ID = "_id";
    String V_QUESTION_TEXT = "question_text";
    String V_TYPE = "type";
    String V_VALUE = "value";
    String V_SUB_QUESTIONS = "sub_questions";
    String V_MULTIPLE_CHOICE = "multiple_choice";
    String V_SINGLE_CHOICE = "single_choice";
    String V_OPTIONS = "options";
    String V_REQUIRED = "required";
    String V_REMARK = "remark";

    /**
     * Parse a given JsonObject into a Question object.
     *
     * @param object source object.
     * @param formId Used for identifying which form the question belongs to
     * @return parsed Question object.
     * @throws Exception thrown if there was a problem during the parsing.
     */
    Question toQuestionPojo(@NonNull JsonObject object, @NonNull String formId) throws Exception;

    /**
     * Converts a question object into a JsonObject
     *
     * @param question source object.
     * @return JsonObject created from the question
     */
    JsonObject toQuestionJson(@NonNull Question question);

    /**
     * Parse a given JsonObject into an Answer object.
     *
     * @param object      source object.
     * @param referenceId Used for identifying which Screening the answer belongs to
     * @return parsed Answer object.
     * @throws Exception thrown if there was a problem during the parsing.
     */
    Answer toAnswerPojo(@NonNull JsonObject object, @NonNull String referenceId) throws Exception;
    /**
     * Converts an answer object into a JsonObject
     *
     * @param answer source object.
     * @return JsonObject created from the answer
     */
    JsonObject toAnswerJson(@NonNull Answer answer);

    /**
     * Return a string API representation of the question/answer type.
     *
     * @param type input local type
     * @return converted type.
     */
    String getApiType(int type);

    /**
     * Return a local integer representation of the question/answer API type.
     *
     * @param type input API type
     * @return converted local type.
     */
    int getLocalType(@NonNull String type);

    /**
     * Converts a given List of strings into a JsonArray
     *
     * @param list list to convert.
     * @return JsonArray of converted strings
     */
    JsonArray toJsonArray(@NonNull List<String> list) throws Exception;

    /**
     * Converts a given JsonArray into a List of Strings
     *
     * @param array JSON array to convert.
     * @return parsed/converted List of strings.
     */
    List<String> toList(@NonNull JsonArray array) throws Exception;
}

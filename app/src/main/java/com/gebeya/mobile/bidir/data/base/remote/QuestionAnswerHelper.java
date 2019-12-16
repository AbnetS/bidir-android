package com.gebeya.mobile.bidir.data.base.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.question.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpful class for converting questions and answers into their Json and POJO counterparts
 */
public final class QuestionAnswerHelper implements QAHelper {

    private static QAHelper instance;

    public static QAHelper getInstance() {
        if (instance == null) {
            instance = new QuestionAnswerHelper();
        }
        return instance;
    }

    @Override
    public Question toQuestionPojo(@NonNull JsonObject object, @NonNull String formId) throws Exception {
        try {
            Question question = new Question();

            question._id = object.get(V_ID).getAsString();
            question.formId = formId;
            question.message = object.get(V_QUESTION_TEXT).getAsString();

            final int type = getLocalType(object.get(V_TYPE).getAsString());
            question.type = type;

            final List<String> choices = toList(object.getAsJsonArray(V_OPTIONS));
            question.choices.addAll(choices);

            if (type == TYPE_INPUT) {
                final String value = object.get(V_VALUE).getAsString();
                if (!value.trim().isEmpty()) {
                    question.answers.add(value);
                }
            } else {
                final JsonArray array = object.getAsJsonArray(type == TYPE_MULTIPLE_CHOICE_SINGLE ?
                        V_MULTIPLE_CHOICE : V_SINGLE_CHOICE
                );
                final List<String> answers = toList(array);
                question.answers.addAll(answers);
            }

            question.remark = object.get(V_REMARK).getAsString();
            question.required = object.get(V_REQUIRED).getAsBoolean();

            return question;
        } catch (Exception e) {
            throw new Exception("Error parsing Question: " + e.toString());
        }
    }

    @Override
    public JsonObject toQuestionJson(@NonNull Question question) {
        JsonObject object = new JsonObject();

        object.addProperty(V_QUESTION_TEXT, question.message);
        object.addProperty(V_TYPE, getApiType(question.type));
        object.addProperty(V_REMARK, question.remark);
        object.addProperty(V_REQUIRED, question.required);

        final int type = question.type;
        final JsonArray array = new JsonArray();

        if (type == TYPE_INPUT) {
            String value = question.answers.isEmpty() ? "" : question.answers.get(0);
            object.addProperty(V_VALUE, value);
        } else {
            boolean singleChoice = type == TYPE_MULTIPLE_CHOICE_SINGLE;
            object.add(V_SINGLE_CHOICE, singleChoice ? toJsonArray(question.answers) : array);
            object.add(V_MULTIPLE_CHOICE, singleChoice ? array : toJsonArray(question.answers));
        }
        object.add(V_OPTIONS, question.choices.isEmpty() ? array : toJsonArray(question.choices));
        object.add(V_SUB_QUESTIONS, array);

        return object;
    }

    @Override
    public Answer toAnswerPojo(@NonNull JsonObject object, @NonNull String referenceId) throws Exception {
        try {
            Answer answer = new Answer();

            answer._id = object.get(V_ID).getAsString();
            answer.referenceId = referenceId;
            answer.message = object.get(V_QUESTION_TEXT).getAsString();

            final int type = getLocalType(object.get(V_TYPE).getAsString());
            answer.type = type;

            final List<String> choices = toList(object.getAsJsonArray(V_OPTIONS));
            answer.choices.addAll(choices);

            if (type == TYPE_INPUT) {
                final String value = object.get(V_VALUE).getAsString();
                if (!value.trim().isEmpty()) {
                    answer.answers.add(value);
                }
            } else {
                final JsonArray array = object.getAsJsonArray(type == TYPE_MULTIPLE_CHOICE_SINGLE ?
                        V_SINGLE_CHOICE : V_MULTIPLE_CHOICE
                );
                final List<String> answers = toList(array);
                answer.answers.addAll(answers);
            }

            answer.remark = object.get(V_REMARK).getAsString();
            answer.required = object.get(V_REQUIRED).getAsBoolean();

            return answer;
        } catch (Exception e) {
            throw new Exception("Error parsing Answer: " + e.toString());
        }
    }

    @Override
    public JsonObject toAnswerJson(@NonNull Answer answer) {
        JsonObject object = new JsonObject();

        object.addProperty(V_ID, answer._id);
        object.addProperty(V_QUESTION_TEXT, answer.message);
        object.addProperty(V_TYPE, getApiType(answer.type));
        object.addProperty(V_REMARK, answer.remark);
        object.addProperty(V_REQUIRED, answer.required);

        final int type = answer.type;
        final JsonArray array = new JsonArray();

        if (type == TYPE_INPUT) {
            String value = answer.answers.isEmpty() ? "" : answer.answers.get(0);
            object.addProperty(V_VALUE, value);
        } else {
            boolean singleChoice = type == TYPE_MULTIPLE_CHOICE_SINGLE;
            object.add(V_SINGLE_CHOICE, singleChoice ? toJsonArray(answer.answers) : array);
            object.add(V_MULTIPLE_CHOICE, singleChoice ? array : toJsonArray(answer.answers));
        }
        object.add(V_OPTIONS, answer.choices.isEmpty() ? array : toJsonArray(answer.choices));
        object.add(V_SUB_QUESTIONS, array);

        return object;
    }

    @Override
    public String getApiType(int type) {
        switch (type) {
            case TYPE_INPUT:
                return API_TYPE_INPUT;
            case TYPE_MULTIPLE_CHOICE_MULTIPLE:
                return API_TYPE_MULTIPLE_CHOICE_MULTIPLE;
            default:
                return API_TYPE_MULTIPLE_CHOICE_SINGLE;
        }
    }

    @Override
    public int getLocalType(@NonNull String type) {
        switch (type) {
            case API_TYPE_INPUT:
                return TYPE_INPUT;
            case API_TYPE_MULTIPLE_CHOICE_MULTIPLE:
                return TYPE_MULTIPLE_CHOICE_MULTIPLE;
            default:
                return TYPE_MULTIPLE_CHOICE_SINGLE;
        }
    }

    public JsonArray toJsonArray(@NonNull List<String> list) {
        JsonArray array = new JsonArray();
        final int length = list.size();
        for (int i = 0; i < length; i++) {
            final String s = list.get(i);
            if (!s.trim().isEmpty()) {
                array.add(s);
            }
        }
        return array;
    }

    public List<String> toList(@NonNull JsonArray array) throws Exception {
        final List<String> list = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            String s = array.get(i).getAsString();
            if (s != null && !s.trim().isEmpty()) {
                list.add(s);
            }
        }
        return list;
    }
}
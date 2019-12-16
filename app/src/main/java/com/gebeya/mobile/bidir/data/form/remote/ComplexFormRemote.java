package com.gebeya.mobile.bidir.data.form.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.gebeya.mobile.bidir.data.form.ComplexFormResponse;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.prerequisite.remote.PrerequisiteParser;
import com.gebeya.mobile.bidir.data.section.Section;
import com.gebeya.mobile.bidir.data.section.remote.SectionParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ComplexFormRemote extends BaseRemoteSource<ComplexFormService> implements ComplexFormRemoteSource {

    @Inject ComplexFormParser formParser;
    @Inject ComplexQuestionParser complexQuestionParser;
    @Inject PrerequisiteParser prerequisiteParser;
    @Inject SectionParser sectionParser;

    public ComplexFormRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.FORMS, ComplexFormService.class);
    }

    @Override
    public Observable<List<ComplexFormResponse>> downloadAll() {
        return build().call(service.downloadAll())
                .map(root -> {
                    final JsonArray array = root.get("docs").getAsJsonArray();

                    final int length = array.size();
                    final List<ComplexFormResponse> responses = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        final JsonObject object = array.get(i).getAsJsonObject();
                        final ComplexFormResponse response = parseResponse(object);
                        responses.add(response);
                    }

                    return responses;
                });
    }

    @Override
    public Observable<ComplexFormResponse> download(@NonNull String formId) {
        return build().call(service.download(formId)).map(this::parseResponse);
    }

    @Override
    public ComplexFormResponse parseResponse(@NonNull JsonObject object) throws Exception {
        final ComplexFormResponse response = new ComplexFormResponse();
        final ComplexForm form = formParser.parse(object);
        response.form = form;
        final JsonArray questionsArray = object.get("questions").getAsJsonArray();

        response.questionResponses = parseQuestionResponses(questionsArray, form._id, form.type);

        final JsonArray sectionsArray = object.get("sections").getAsJsonArray();
        if (response.questionResponses.size() == 0) {
            final int length = sectionsArray.size();
            for (int i = 0; i < length; i++) {
                final JsonObject sectionObject = sectionsArray.get(i).getAsJsonObject();
                final JsonArray innerQuestionsArray = sectionObject.get("questions").getAsJsonArray();
                response.questionResponses.addAll(parseQuestionResponses(innerQuestionsArray, form._id, form.type));
            }
        }

        response.sections = parseSections(sectionsArray, form._id, form.type);

        return response;
    }

    @Override
    public List<Section> parseSections(@NonNull JsonArray array, @NonNull String referenceId, @NonNull String referenceType) throws Exception {
        final List<Section> sections = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final Section section = sectionParser.parse(object, referenceId, referenceType);
            sections.add(section);
        }
        return sections;
    }

    @Override
    public List<ComplexQuestionResponse> parseSubQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId, @NonNull String referenceType) throws Exception {
        final List<ComplexQuestionResponse> responses = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject questionObject = array.get(i).getAsJsonObject();
            final ComplexQuestionResponse response = parseQuestionResponse(questionObject, referenceId, referenceType);
            responses.add(response);
        }

        return responses;
    }

    @Override
    public List<ComplexQuestionResponse> parseQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId, @NonNull String referenceType) throws Exception {
        final List<ComplexQuestionResponse> responses = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject questionObject = array.get(i).getAsJsonObject();
            final ComplexQuestionResponse response = parseQuestionResponse(questionObject, referenceId, referenceType);

            final JsonArray subQuestionsArray = questionObject.get("sub_questions").getAsJsonArray();
            if (subQuestionsArray.size() != 0) {
                final List<ComplexQuestionResponse> subQuestions = parseSubQuestionResponses(subQuestionsArray, referenceId, referenceType);
                responses.addAll(subQuestions);
            }
            responses.add(response);
        }

        return responses;
    }

    @Override
    public ComplexQuestionResponse parseQuestionResponse(@NonNull JsonObject object, @NonNull String referenceId, @NonNull String referenceType) throws Exception {
        final ComplexQuestionResponse response = new ComplexQuestionResponse();
        final ComplexQuestion question = complexQuestionParser.parse(object, referenceId, referenceType);
        response.question = question;

        final JsonArray prerequisitesArray = object.get("prerequisites").getAsJsonArray();
        response.prerequisites = parsePrerequisites(prerequisitesArray, question._id);

        return response;
    }

    @Override
    public List<Prerequisite> parsePrerequisites(@NonNull JsonArray array, @NonNull String parentQuestionId) throws Exception {
        final List<Prerequisite> prerequisites = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject prerequisiteObject = array.get(i).getAsJsonObject();
            final Prerequisite prerequisite = prerequisiteParser.parse(prerequisiteObject, parentQuestionId);
            prerequisites.add(prerequisite);
        }
        return prerequisites;
    }
}

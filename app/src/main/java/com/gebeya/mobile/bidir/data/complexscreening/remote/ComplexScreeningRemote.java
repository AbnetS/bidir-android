package com.gebeya.mobile.bidir.data.complexscreening.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningRequest;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningResponse;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.prerequisite.remote.PrerequisiteParser;
import com.gebeya.mobile.bidir.data.section.Section;
import com.gebeya.mobile.bidir.data.section.SectionRequest;
import com.gebeya.mobile.bidir.data.section.remote.SectionParser;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link ComplexScreeningRemote} remote source.
 */
public class ComplexScreeningRemote extends BaseRemoteSource<ComplexScreeningService>
        implements ComplexScreeningRemoteSource {

    @Inject ComplexScreeningParser complexScreeningParser;
    @Inject ComplexQuestionParser complexQuestionParser;
    @Inject PrerequisiteParser prerequisiteParser;
    @Inject SectionParser sectionParser;

    public ComplexScreeningRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.SCREENINGS, ComplexScreeningService.class);
    }

    @Override
    public Observable<List<ComplexScreeningResponse>> downloadAll() {
        return build().call(service.downloadAll())
                .map(root -> {
                    final JsonArray array = root.get("docs").getAsJsonArray();

                    final List<ComplexScreeningResponse> responses = new ArrayList<>();
                    final int length = array.size();
                    for (int i = 0; i < length; i++) {
                        final JsonObject object = array.get(i).getAsJsonObject();
                        final ComplexScreeningResponse response = parseResponse(object);
                        responses.add(response);
                    }

                    return responses;
                });
    }

    @Override
    public Observable<ComplexScreeningResponse> download(@NonNull String screeningId) {
        return build().call(service.download(screeningId)).map(this::parseResponse);
    }

    @Override
    public ComplexScreeningResponse parseResponse(@NonNull JsonObject object) throws Exception {
        final ComplexScreeningResponse response = new ComplexScreeningResponse();
        final ComplexScreening screening = complexScreeningParser.parse(object);
        response.screening = screening;

        final JsonArray questionsArray = object.get("questions").getAsJsonArray();
        response.questionResponses = parseQuestionResponses(questionsArray, screening._id);

        final JsonArray sectionsArray = object.get("sections").getAsJsonArray();
        response.sections = parseSections(sectionsArray, screening._id, Constants.REF_TYPE_SCREENING);

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
    public List<ComplexQuestionResponse> parseQuestionResponses(@NonNull JsonArray array,
                                                                @NonNull String referenceId) throws Exception {
        final List<ComplexQuestionResponse> responses = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject questionObject = array.get(i).getAsJsonObject();
            final ComplexQuestionResponse response = parseQuestionResponse(questionObject, referenceId);

            final JsonArray subQuestionsArray = questionObject.get("sub_questions").getAsJsonArray();
            if (subQuestionsArray.size() != 0) {
                final List<ComplexQuestionResponse> subQuestions = parseSubQuestionResponses(subQuestionsArray, referenceId);
                responses.addAll(subQuestions);
            }

            responses.add(response);
        }

        return responses;
    }

    @Override
    public List<ComplexQuestionResponse> parseSubQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId) throws Exception {
        final List<ComplexQuestionResponse> responses = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject questionObject = array.get(i).getAsJsonObject();
            final ComplexQuestionResponse response = parseQuestionResponse(questionObject, referenceId);
            responses.add(response);
        }

        return responses;
    }

    @Override
    public ComplexQuestionResponse parseQuestionResponse(@NonNull JsonObject object, @NonNull String referenceId) throws Exception {
        final ComplexQuestionResponse response = new ComplexQuestionResponse();
        final ComplexQuestion question = complexQuestionParser.parse(object, referenceId, Constants.REF_TYPE_SCREENING);
        response.question = question;

        final JsonArray prerequisitesArray = object.get("prerequisites").getAsJsonArray();
        response.prerequisites = parsePrerequisites(prerequisitesArray, question._id);

        return response;
    }

    @Override
    public List<Prerequisite> parsePrerequisites(@NonNull JsonArray array,
                                                 @NonNull String parentQuestionId) throws Exception {
        final List<Prerequisite> prerequisites = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject prerequisiteObject = array.get(i).getAsJsonObject();
            final Prerequisite prerequisite = prerequisiteParser.parse(prerequisiteObject, parentQuestionId);
            prerequisites.add(prerequisite);
        }
        return prerequisites;
    }

    @Override
    public Observable<ComplexScreeningResponse> uploadQuestions(@NonNull String screeningId, @NonNull ComplexScreeningRequest request) {
        final JsonObject data = new JsonObject();
        data.addProperty("status", request.status);
        data.addProperty("comment", "");

        final JsonArray questionsArray = createQuestionRequests(request.requests);
        data.add("questions", questionsArray);

        return build().call(service.uploadQuestions(screeningId, data)).map(this::parseResponse);
    }

    @Override
    public Observable<ComplexScreeningResponse> uploadScreening(@NonNull String screeningId,
                                                                @NonNull ComplexScreeningRequest request,
                                                                @Nullable String remark) {
        final JsonObject object = new JsonObject();
        object.addProperty("status", request.status);
        if (remark != null) {
            object.addProperty("comment", remark);
        }

        final JsonArray questionsArray = createQuestionRequests(request.requests);
        object.add("questions", questionsArray);

        final JsonArray sectionsArray = createSectionsArray(request.sectionRequests);
        object.add("sections", sectionsArray);

        return build().call(service.uploadScreening(screeningId, object)).map(this::parseResponse);
    }

    @Override
    public Observable<ComplexScreeningResponse> create(@NonNull String clientId, boolean isGrouped) {
        final JsonObject request = new JsonObject();
        request.addProperty("client", clientId);
        if (isGrouped) request.addProperty("for_group", true);
        else request.addProperty("for_group", false);
        return build().call(service.create(request)).map(this::parseResponse);
    }

    private JsonArray createQuestionRequests(@NonNull List<ComplexQuestionRequest> requests) {
        final JsonArray array = new JsonArray();
        final int length = requests.size();

        for (int i = 0; i < length; i++) {
            final ComplexQuestionRequest request = requests.get(i);
            final JsonObject object = createQuestionRequest(request);
            array.add(object);
        }

        return array;
    }

    private JsonObject createQuestionRequest(@NonNull ComplexQuestionRequest request) {
        final ComplexQuestion question = request.question;
        final JsonObject object = complexQuestionParser.createQuestion(request.question);

        final List<Prerequisite> prerequisites = request.prerequisites;
        final JsonArray prerequisitesArray = prerequisiteParser.createPrerequisites(prerequisites);
        object.add("prerequisites", prerequisitesArray);

        final String type = question.type;
        final JsonArray childQuestions;

        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
            childQuestions = createChildQuestions(request.childQuestions);
        } else {
            childQuestions = new JsonArray();
        }
        object.add("sub_questions", childQuestions);

        return object;
    }

    private JsonArray createChildQuestions(@NonNull List<ComplexQuestion> questions) {
        final JsonArray array = new JsonArray();
        final int length = questions.size();

        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = questions.get(i);
            final JsonObject object = complexQuestionParser.createQuestion(question);
            object.add("prerequisites", new JsonArray());
            object.add("sub_questions", new JsonArray());
            array.add(object);
        }

        return array;
    }

    private JsonArray createSectionsArray(@NonNull List<SectionRequest> sectionRequests) {
        final JsonArray array = new JsonArray();

        final int length = sectionRequests.size();
        for (int i = 0; i < length; i++) {
            final SectionRequest request = sectionRequests.get(i);
            final JsonObject sectionObject = createSection(request);
            array.add(sectionObject);
        }

        return array;
    }

    private JsonObject createSection(@NonNull SectionRequest request) {
        final JsonObject object = new JsonObject();

        final Section section = request.section;
        object.addProperty("_id", section._id);
        object.addProperty("last_modified", section.updatedAt.toString());
        object.addProperty("date_created", section.createdAt.toString());

        final JsonArray questionsArray = createQuestionRequests(request.requests);
        object.add("questions", questionsArray);

        object.addProperty("number", section.number);
        object.addProperty("title", section.title);

        return object;
    }
}

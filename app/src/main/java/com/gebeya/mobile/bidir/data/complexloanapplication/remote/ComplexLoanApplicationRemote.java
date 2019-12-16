package com.gebeya.mobile.bidir.data.complexloanapplication.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationRequest;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.pagination.remote.PageParser;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
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
import retrofit2.Response;

/**
 * Concrete implementation for the {@link ComplexLoanApplicationRemoteSource} data source.
 */
public class ComplexLoanApplicationRemote extends BaseRemoteSource<ComplexLoanApplicationService> implements
        ComplexLoanApplicationRemoteSource {

    @Inject ComplexLoanApplicationParser complexLoanApplicationParser;
    @Inject ComplexQuestionParser complexQuestionParser;
    @Inject PrerequisiteParser prerequisiteParser;
    @Inject SectionParser sectionParser;
    @Inject PageParser pageParser;

    @Inject
    public ComplexLoanApplicationRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.LOAN_APPLICATIONS, ComplexLoanApplicationService.class);
    }

    @Override
    public Observable<List<ComplexLoanApplicationResponse>> downloadAll() {
        return build().call(service.downloadAll())
                .map(root -> {
                    final JsonArray array = root.get("docs").getAsJsonArray();

                    final List<ComplexLoanApplicationResponse> responses = new ArrayList<>();
                    final int length = array.size();
                    for (int i = 0; i < length; i++) {
                        final JsonObject object = array.get(i).getAsJsonObject();
                        final ComplexLoanApplicationResponse response = parseResponse(object);
                        responses.add(response);
                    }

                    return responses;
                });
    }

    @Override
    public PageResponse downloadPageData() throws Exception {
        build();
        final Response<JsonObject> apiResponse = service.getPaginationData().execute();
        final JsonObject object = apiResponse.body();

        return pageParser.parse(object, Service.LOAN_APPLICATIONS);
    }

    @Override
    public PaginatedComplexLoanApplicationResponse getAllPaginated(int currentPage) throws Exception {
        build();

        final Response<JsonObject> apiResponse = service.getAllByPage(currentPage).execute();
        final JsonObject object = apiResponse.body();

        final PaginatedComplexLoanApplicationResponse paginatedResponse = new PaginatedComplexLoanApplicationResponse();

        final ComplexLoanApplicationResponse loanApplicationResponse = parseResponse(object);
        final PageResponse pageResponse = pageParser.parse(object, Service.LOAN_APPLICATIONS);

        paginatedResponse.loanApplicationResponse = loanApplicationResponse;
        paginatedResponse.pageResponse = pageResponse;

        return paginatedResponse;
    }

    @Override
    public Observable<ComplexLoanApplicationResponse> download(@NonNull String applicationId) {
        return build().call(service.download(applicationId)).map(this::parseResponse);
    }

    @Override
    public ComplexLoanApplicationResponse parseResponse(@NonNull JsonObject object) throws Exception {
        final ComplexLoanApplicationResponse response = new ComplexLoanApplicationResponse();
        final ComplexLoanApplication application = complexLoanApplicationParser.parse(object);
        response.application = application;

        final JsonArray questionsArray = object.get("questions").getAsJsonArray();
        response.questionResponses = parseQuestionResponses(questionsArray, application._id);

        final JsonArray sectionsArray = object.get("sections").getAsJsonArray();
        if (response.questionResponses.size() == 0) {
            final int length = sectionsArray.size();
            for (int i = 0; i < length; i++) {
                final JsonObject sectionObject = sectionsArray.get(i).getAsJsonObject();
                final JsonArray innerQuestionsArray = sectionObject.get("questions").getAsJsonArray();
                response.questionResponses.addAll(parseQuestionResponses(innerQuestionsArray, application._id));
            }
        }

        response.sections = parseSections(sectionsArray, application._id, Constants.REF_TYPE_LOAN_APPLICATION);

        return response;
    }

    @Override
    public Observable<ComplexLoanApplicationResponse> create(@NonNull Client client) {
        final JsonObject request = new JsonObject();
        request.addProperty("client", client._id);
        return build().call(service.create(request)).map(this::parseResponse);
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
    public ComplexQuestionResponse parseQuestionResponse(@NonNull JsonObject object, @NonNull String referenceId) throws Exception {
        final ComplexQuestionResponse response = new ComplexQuestionResponse();
        final ComplexQuestion question = complexQuestionParser.parse(object, referenceId, Constants.REF_TYPE_LOAN_APPLICATION);
        response.question = question;

        final JsonArray prerequisitesArray = object.get("prerequisites").getAsJsonArray();
        response.prerequisites = parsePrerequisites(prerequisitesArray, question._id);

        return response;
    }

    @Override
    public List<ComplexQuestionResponse> parseQuestionResponses(@NonNull JsonArray array, @NonNull String referenceId) throws Exception {
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

    @Override
    public Observable<ComplexLoanApplicationResponse> uploadApplication(@NonNull String applicationId,
                                                                        @NonNull ComplexLoanApplicationRequest request,
                                                                        @Nullable String remark) {
        final JsonObject object = new JsonObject();
        object.addProperty("status", request.status);
        if (remark != null) {
            object.addProperty("comment", remark);
        }
        final JsonArray sectionsArray = createSectionsArray(request.sectionRequests);
        object.add("sections", sectionsArray);

        final JsonArray questionsArray = createQuestionRequests(request.requests);
        object.add("questions", questionsArray);

        return build().call(service.uploadApplication(applicationId, object)).map(this::parseResponse);
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
}
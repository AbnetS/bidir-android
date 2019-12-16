package com.gebeya.mobile.bidir.data.loanapplication.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplicationStatusHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Concrete implementation for the remote source for {@link com.gebeya.mobile.bidir.data.loanapplication.LoanApplication}
 */
public class LoanApplicationRemote extends BaseRemoteSource<LoanApplicationService> implements LoanApplicationRemoteSource {

    private LoanApplicationResponse response;   // TODO: Deal with this thing

    @Inject QAHelper helper;
    @Inject LoanApplicationStatusHelper statusHelper;

    public LoanApplicationRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.LOAN_APPLICATIONS, LoanApplicationService.class);
    }

    @Override
    public Observable<LoanApplication> update(@NonNull LoanApplicationResponse response) {
        final JsonObject request = new JsonObject();
        final int size = response.answers.size();
        final JsonArray array = new JsonArray();
        for (int i = 0; i < size; i++) {
            array.add(helper.toAnswerJson(response.answers.get(i)));
        }

        request.addProperty("status", LoanApplicationStatusHelper.API_IN_PROGRESS);
        request.add("questions", array);

        return build()
                .call(service.update(response.application._id, request))
                .map(object -> parseLoanApplicationOnly(object));
    }

    @Override
    public Observable<LoanApplication> updateApiStatus(@NonNull LoanApplication application, @NonNull String status, @Nullable String remark) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", status);

        return build()
                .call(service.update(application._id, request))
                .map(this::parseLoanApplicationOnly);
    }

    @Override
    public Observable<LoanApplication> declineAcceptFinal(@NonNull LoanApplication application, @NonNull String status) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", statusHelper.getApiStatus(status));

        return build()
                .call(service.update(application._id, request))
                .map(this::parseLoanApplicationOnly);
    }

    @Override
    public Observable<List<LoanApplicationResponse>> getAll() {
        return build()
                .call(service.getAll())
                .map(object -> {
                    final List<LoanApplicationResponse> responses = new ArrayList<>();
                    final JsonArray array = object.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        JsonObject loanApplicationObject = array.get(i).getAsJsonObject();
                        responses.add(parse(loanApplicationObject));
                    }
                    return responses;
                });
    }

    @Override
    public Observable<LoanApplicationResponse> downloadApplication(@NonNull String applicationId) {
        return build()
                .call(service.getOne(applicationId))
                .map(this::parse);
    }

    @Override
    public Observable<LoanApplicationResponse> create(@NonNull Client client) {
        final Map<String, String> request = new HashMap<>();
        request.put("client", client._id);

        return build()
                .call(service.create(request))
                .map(this::parse);
    }

    @Override
    public LoanApplicationResponse parse(@NonNull JsonObject object) throws Exception {
        response = new LoanApplicationResponse();
        final LoanApplication application = parseLoanApplicationAndClient(object);

        response.application = application;
        final JsonArray array = object.getAsJsonArray("questions");
        final List<Answer> answers = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final Answer answer = helper.toAnswerPojo(array.get(i).getAsJsonObject(), application._id);
            answer.number = i + 1;
            answer.referenceType = QAHelper.REF_TYPE_LOAN_APPLICATION;
            answers.add(answer);
        }
        response.answers = answers;

        return response;
    }

    @Override
    public LoanApplication parseLoanApplicationAndClient(@NonNull JsonObject object) throws Exception {
        try {
            LoanApplication application = new LoanApplication();
            Client client = new Client();

            application._id = object.get("_id").getAsString();
            JsonObject c = object.get("client").getAsJsonObject();

            application.clientId = c.get("_id").getAsString();

            client._id = application.clientId;
            client.firstName = c.get("first_name").getAsString();
            client.lastName = c.get("last_name").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = statusHelper.getLocalStatus(apiStatus);
            if (localStatus.equals(LoanApplicationStatusHelper.UNKNOWN_STATUS)) {
                throw new Exception("Unknown API Loan Application status: " + apiStatus);
            }
            application.status = localStatus;

            application.title = object.get("title").getAsString();
           // application.type = object.getByType("type").getAsString();

            response.client = client;
            return application;
        } catch (Exception e) {
            throw new Exception("Error parsing LoanApplication: " + e.toString());
        }
    }

    @Override
    public LoanApplication parseLoanApplicationOnly(@NonNull JsonObject object) throws Exception {
        try {
            LoanApplication application = new LoanApplication();

            application._id = object.get("_id").getAsString();
            JsonObject client = object.get("client").getAsJsonObject();

            application.clientId = client.get("_id").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = statusHelper.getLocalStatus(apiStatus);
            if (localStatus.equals(LoanApplicationStatusHelper.UNKNOWN_STATUS)) {
                throw new Exception("Unknown API Loan Application status: " + apiStatus);
            }
            application.status = localStatus;

            application.title = object.get("title").getAsString();
            application.type = object.get("type").getAsString();

            return application;
        } catch (Exception e) {
            throw new Exception("Error parsing LoanApplication: " + e.toString());
        }
    }
}
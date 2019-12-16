package com.gebeya.mobile.bidir.data.screening.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.screening.Screening;
import com.gebeya.mobile.bidir.data.screening.ScreeningStatusHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * API implementation for the {@link ScreeningRemoteSource} interface
 */
public class ScreeningRemote extends BaseRemoteSource<ScreeningService> implements ScreeningRemoteSource {

    private ScreeningResponse response; // TODO: Address this thing

    @Inject QAHelper helper;
    @Inject ScreeningStatusHelper statusHelper;

    public ScreeningRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.SCREENINGS, ScreeningService.class);
    }

    @Override
    public Observable<Screening> update(@NonNull ScreeningResponse response) {
        final JsonObject request = new JsonObject();
        final int size = response.answers.size();
        final JsonArray array = new JsonArray();
        for (int i = 0; i < size; i++) {
            array.add(helper.toAnswerJson(response.answers.get(i)));
        }

        request.addProperty("status", ScreeningStatusHelper.API_IN_PROGRESS);
        request.add("questions", array);

        return build().call(service.update(response.screening._id, request)).map(this::parseScreeningOnly);
    }

    @Override
    public Observable<Screening> updateApiStatus(@NonNull Screening screening,
                                                 @NonNull String status,
                                                 @Nullable String remark) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", statusHelper.getApiStatus(status));

        if (remark != null) {
            request.addProperty("comment", remark);     // TODO: Add proper implementation once API is ready
        }

        return build().call(service.update(screening._id, request)).map(this::parseScreeningOnly);
    }

    @Override
    public Observable<List<ScreeningResponse>> getAll() {
        return build().call(service.getAll())
                .map(object -> {
                    final List<ScreeningResponse> responses = new ArrayList<>();
                    final JsonArray array = object.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        JsonObject screeningObject = array.get(i).getAsJsonObject();
                        responses.add(parse(screeningObject));
                    }
                    return responses;
                });
    }

    @Override
    public Observable<ScreeningResponse> getOne(@NonNull String screeningId) {
        return build().call(service.getOne(screeningId)).map(this::parse);
    }

    @Override
    public ScreeningResponse parse(@NonNull JsonObject object) throws Exception {
        response = new ScreeningResponse();
        final Screening screening = parseScreeningAndClient(object);

        response.screening = screening;
        final JsonArray array = object.getAsJsonArray("questions");
        final List<Answer> answers = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final Answer answer = helper.toAnswerPojo(array.get(i).getAsJsonObject(), screening._id);
            answer.number = i + 1;
            answer.referenceType = QAHelper.REF_TYPE_SCREENING;
            answers.add(answer);
        }
        response.answers = answers;

        return response;
    }

    @Override
    public Screening parseScreeningAndClient(@NonNull JsonObject object) throws Exception {
        try {
            Screening screening = new Screening();
            Client client = new Client();

            screening._id = object.get("_id").getAsString();

            JsonObject c = object.get("client").getAsJsonObject();

            screening.clientId = c.get("_id").getAsString();
            client._id = screening.clientId;
            client.firstName = c.get("first_name").getAsString();
            client.lastName = c.get("last_name").getAsString();
            client.createdBy = c.get("created_by").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStats = statusHelper.getLocalStatus(apiStatus);
            if (localStats.equals(ScreeningStatusHelper.UNKNOWN_STATUS)) {
                throw new Exception("Unknown API Screening status: " + apiStatus);
            }
            screening.status = localStats;

            screening.title = object.get("title").getAsString();

            response.client = client;
            return screening;
        } catch (Exception e) {
            throw new Exception("Error parsing Screening: " + e.toString());
        }
    }

    @Override
    public Screening parseScreeningOnly(@NonNull JsonObject object) throws Exception {
        try {
            Screening screening = new Screening();

            screening._id = object.get("_id").getAsString();

            JsonObject c = object.get("client").getAsJsonObject();
            screening.clientId = c.get("_id").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStats = statusHelper.getLocalStatus(apiStatus);
            if (localStats.equals(ScreeningStatusHelper.UNKNOWN_STATUS)) {
                throw new Exception("Unknown API Screening status: " + apiStatus);
            }
            screening.status = localStats;

            screening.title = object.get("title").getAsString();

            return screening;
        } catch (Exception e) {
            throw new Exception("Error parsing Screening: " + e.toString());
        }
    }
}
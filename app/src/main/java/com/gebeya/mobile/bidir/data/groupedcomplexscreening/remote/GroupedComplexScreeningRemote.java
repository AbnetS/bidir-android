package com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningRemote;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreeningResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GroupedComplexScreeningRemote extends BaseRemoteSource<GroupedComplexScreeningService> implements
        GroupedComplexScreeningRemoteSource{

    @Inject GroupedComplexScreeningParser parser;
    @Inject ComplexScreeningRemote complexScreeningRemote;

    @Inject
    public GroupedComplexScreeningRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.GROUPS, GroupedComplexScreeningService.class);
    }

    @Override
    public Observable<List<GroupedComplexScreening>> getAll() {
        return build()
                .call(service.getAll())
                .map(response -> {
                    final List<GroupedComplexScreening> screenings = new ArrayList<>();
                    final JsonArray array = response.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        GroupedComplexScreening screening = parser.parse(array.get(i).getAsJsonObject());
                        if (screening != null)
                            screenings.add(screening);
                    }
                    return screenings;
                });
    }

    @Override
    public Observable<GroupedComplexScreening> download(@NonNull String groupId) {
        return build()
                .call(service.get(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedComplexScreening> submitGroupScreening(@NonNull String groupId) {
        return build()
                .call(service.submitGroupScreening(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedComplexScreening> approveGroupScreening(@NonNull String groupId) {
        return build()
                .call(service.approveGroupScreening(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedComplexScreening> updateStatus(@NonNull String groupId) {
        return build()
                .call(service.updateStatus(groupId))
                .map(parser::parse);
    }

    @Override
    public Observable<GroupedComplexScreeningResponse> createNewLoanCycle(@NonNull String groupId, @NonNull String loanAmount) {
        final JsonObject request = new JsonObject();
        request.addProperty("group", groupId);
        request.addProperty("total_amount", Double.parseDouble(loanAmount));

        return build()
                .call(service.createNewLoan(request))
                .map(this::parseResponse);
    }

    @Override
    public GroupedComplexScreeningResponse parseResponse(@NonNull JsonObject object) throws Exception {
        final GroupedComplexScreeningResponse response = new GroupedComplexScreeningResponse();
        response.complexScreeningResponseList = new ArrayList<>();
        response.groupedComplexScreening = parser.parse(object);

        final JsonArray complexScreeningArray = object.get("screenings").getAsJsonArray();
        if (complexScreeningArray.size() != 0) {
            for (int i = 0; i < complexScreeningArray.size(); i++) {
                response.complexScreeningResponseList.add(complexScreeningRemote.parseResponse(complexScreeningArray.get(i).getAsJsonObject()));
            }
        }

        return response;
    }
}

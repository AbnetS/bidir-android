package com.gebeya.mobile.bidir.data.groupedcomplexloan.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemote;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComlexLoanResponse;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GroupedComplexLoanRemote extends BaseRemoteSource<GroupedComplexLoanService> implements GroupedComplexLoanRemoteSource{

    @Inject GroupedComplexLoanParser parser;
    @Inject ComplexLoanApplicationRemote loanApplicationRemote;

    @Inject
    public GroupedComplexLoanRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.GROUPS, GroupedComplexLoanService.class);
    }

    @Override
    public Observable<GroupedComlexLoanResponse> download(@NonNull String groupId) {
        return build()
                .call(service.get(groupId))
                .map(this::parseResponse);
    }

    @Override
    public Observable<List<GroupedComplexLoan>> getAll() {
        return build()
                .call(service.getAll())
                .map(response -> {
                    final List<GroupedComplexLoan> groupedLoans = new ArrayList<>();
                    final JsonArray array = response.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        GroupedComplexLoan loan = parser.parse(array.get(i).getAsJsonObject());
                        if (loan != null)
                            groupedLoans.add(loan);
                    }
                    return groupedLoans;
                });
    }

    @Override
    public Observable<GroupedComplexLoan> submitGroupLoan(@NonNull String groupId) {
        return build()
                .call(service.submitGroupLoan(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedComplexLoan> approveGroupLoan(@NonNull String groupId) {
        return build()
                .call(service.approveGroupLoan(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedComplexLoan> updateStatus(@NonNull String groupId) {
        return build().call(service.updateStatus(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedComplexLoan> create(@NonNull String groupId) {
        return build().call(service.create(groupId)).map(response -> parser.parse(response));
    }

    @Override
    public GroupedComlexLoanResponse parseResponse(@NonNull JsonObject object) throws Exception{
        final GroupedComlexLoanResponse response = new GroupedComlexLoanResponse();
        response.complexLoanApplicationResponses = new ArrayList<>();
        response.groupedComplexLoan = parser.parse(object);

        final JsonArray complexLoanArray = object.get("loans").getAsJsonArray();
        if (complexLoanArray.size() != 0) {
            for (int i = 0; i < complexLoanArray.size(); i++) {
                response.complexLoanApplicationResponses.add(loanApplicationRemote.parseResponse(complexLoanArray.get(i).getAsJsonObject()));
            }
        }

        return response;
    }
}

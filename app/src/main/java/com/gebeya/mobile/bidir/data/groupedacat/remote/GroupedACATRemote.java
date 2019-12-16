package com.gebeya.mobile.bidir.data.groupedacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemote;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GroupedACATRemote extends BaseRemoteSource<GroupedACATService> implements
        GroupedACATRemoteSource {

    @Inject GroupedACATParser parser;
    @Inject ACATApplicationRemote acatRemote;

    @Inject
    public GroupedACATRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.GROUPS, GroupedACATService.class);
    }

    @Override
    public Observable<List<GroupedACAT>> getAll() {
        return build()
                .call(service.getAll())
                .map(response -> {
                    final List<GroupedACAT> acats = new ArrayList<>();
                    final JsonArray array = response.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        GroupedACAT acat = parser.parse(array.get(i).getAsJsonObject());
                        if (acat != null)
                            acats.add(acat);
                    }
                    return acats;
                });
    }

    @Override
    public Observable<GroupedACAT> download(@NonNull String groupId) {
        return build()
                .call(service.get(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedACAT> submitGroupACAT(@NonNull String groupId) {
        return build()
                .call(service.submitGroupACAT(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedACAT> approveGroupACAT(@NonNull String groupId) {
        return build()
                .call(service.approveGroupACAT(groupId))
                .map(response -> parser.parse(response));
    }

    @Override
    public Observable<GroupedACAT> updateStatus(@NonNull String groupId) {
        return build()
                .call(service.updateStatus(groupId))
                .map(parser::parse);
    }

    @Override
    public Observable<GroupedACAT> create(@NonNull String groupId) {
        final JsonObject request = new JsonObject();
        request.addProperty("group", groupId);
        return build().call(service.create(request)).map(response -> parser.parse(response));
    }

    @Override
    public Observable<ACATApplicationResponse> initializeMemberACAT(@NonNull String groupId, @NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs) {
        final JsonObject request = new JsonObject();
        request.addProperty("client", client._id);
        request.addProperty("loan_product", loanProduct._id);
        request.add("crop_acats", toJson(cropACATs));

        return build()
                .call(service.initialize(groupId, request))
                .map(object -> acatRemote.parse(object));
    }

    private JsonArray toJson(@NonNull List<String> items) {
        final JsonArray array = new JsonArray();
        for(int i = 0; i < items.size(); i++) {
            array.add(items.get(i));
        }

        return array;
    }
}

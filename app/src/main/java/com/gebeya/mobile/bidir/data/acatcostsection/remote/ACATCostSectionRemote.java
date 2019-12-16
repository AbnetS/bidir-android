package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionRequest;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ACATCostSectionRemote extends BaseRemoteSource<ACATCostSectionService> implements ACATCostSectionRemoteSource {
    @Inject BaseACATCostSectionResponseParser acatCostSectionResponseParser;


    @Inject
    public ACATCostSectionRemote(){
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.ACAT, ACATCostSectionService.class);
    }

    public Observable<ACATCostSectionResponse> updateACATCostSection
            (@NonNull String acatCostSectionId, @NonNull ACATCostSectionRequest request){
        return build()
                .call(service.updateSection(acatCostSectionId, createCostSectionRequest(request)))
                .map(updated -> {
                    ACATCostSectionResponse response = acatCostSectionResponseParser.parseSingleSection(updated, request.section.parentSectionID);
                    return response;
                });
    }

    private JsonObject createCostSectionRequest(@NonNull ACATCostSectionRequest request) {
        final JsonObject object = new JsonObject();

        ACATCostSection section = request.section;
        CashFlow estimatedCashFlow = request.estimatedCashFlow;
        CashFlow actualCashFlow = request.actualCashFlow;

        object.addProperty("is_client_acat", true);
        object.addProperty("client_acat", request.acatApplicationId);

        if (request.section.title.compareTo("Seed")==0) {
            object.addProperty("variety", request.section.variety);
            JsonArray sources = new JsonArray();
            for (String source: request.section.seedSources){
                sources.add(source);
            }

            object.add("seed_source",sources);
        }

        if (request.estimatedCashFlow != null){
            object.addProperty("estimated_sub_total", request.section.estimatedSubTotal);
            JsonObject estimatedCashFlowObj = createCashFlow(request.estimatedCashFlow);
            object.add("estimated_cash_flow", estimatedCashFlowObj);
        }

        if (request.actualCashFlow != null){
            object.addProperty("achieved_sub_total", request.section.actualSubTotal);
            JsonObject achievedCashFlowObj = createCashFlow(request.actualCashFlow);
            object.add("achieved_cash_flow", achievedCashFlowObj);
        }



        return object;
    }

    private JsonObject createCashFlow(@NonNull CashFlow cashFlow){
        final JsonObject object =  new JsonObject();

        object.addProperty("jan", cashFlow.january);
        object.addProperty("feb", cashFlow.february);
        object.addProperty("mar", cashFlow.march);
        object.addProperty("apr", cashFlow.april);
        object.addProperty("may", cashFlow.may);
        object.addProperty("june", cashFlow.june);
        object.addProperty("july", cashFlow.july);
        object.addProperty("aug", cashFlow.august);
        object.addProperty("sep", cashFlow.september);
        object.addProperty("oct", cashFlow.october);
        object.addProperty("nov", cashFlow.november);
        object.addProperty("dec", cashFlow.december);

        return object;
    }

}

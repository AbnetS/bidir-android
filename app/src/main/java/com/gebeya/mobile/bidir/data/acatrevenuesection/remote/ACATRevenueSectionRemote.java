package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 8/29/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATRevenueSectionRemote extends BaseRemoteSource<ACATRevenueSectionService> implements ACATRevenueSectionRemoteSource {

    @Inject ACATRevenueSectionParser parser;

    public ACATRevenueSectionRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.ACAT, ACATRevenueSectionService.class);
    }

    @Override
    public Observable<ACATRevenueSection> upload(@NonNull ACATRevenueSection section, @NonNull String clientACATId, @NonNull String ClientId, @NonNull String parentSectionId) {
        final JsonObject root = new JsonObject();

        root.addProperty("is_client_acat", true);
        root.addProperty("client_acat", clientACATId);
        if (section.title.equalsIgnoreCase("Revenue")){
            root.addProperty("estimated_probable_revenue", section.estimatedProbRevenue);
            root.addProperty("estimated_max_revenue", section.estimatedMaxRevenue);
            root.addProperty("estimated_min_revenue", section.estimatedMinRevenue);
        }
        root.addProperty("estimated_sub_total", section.estimatedSubTotal);
        root.addProperty("achieved_sub_total", section.actualSubTotal);

        // TODO: Add estimated cash flow object
        // TODO: Add actual cash flow object

        return build()
                .call(service.update(section._id, root))
                .map(object -> parser.parse(object, parentSectionId));
    }


    @Override
    public Observable<ACATRevenueSection> updateACATRevenueSection(@NonNull String acatRevenueSectionId,
                                                          @NonNull ACATRevenueSectionRequest request) {
        return build()
                .call(service.update(acatRevenueSectionId, createRevenueSectionRequest(request)))
                .map(object -> parser.parse(object, request.section.parentSectionID));
    }

    private JsonObject createRevenueSectionRequest(@NonNull ACATRevenueSectionRequest request){
        final JsonObject object = new JsonObject();

        ACATRevenueSection section = request.section;
        CashFlow estimatedCashFlow = request.estimatedCashFlow;
        CashFlow actualCashFlow = request.actualCashFlow;

        object.addProperty("is_client_acat", true);
        object.addProperty("client_acat", request.acatApplicationId);

//        if (estimatedCashFlow != null) {
            if (section.title.equalsIgnoreCase("Revenue")) {
                object.addProperty("estimated_probable_revenue", section.estimatedProbRevenue);
                object.addProperty("estimated_max_revenue", section.estimatedMaxRevenue);
                object.addProperty("estimated_min_revenue", section.estimatedMinRevenue);
                object.addProperty("estimated_sub_total", section.estimatedSubTotal);
            } else {
                object.addProperty("estimated_sub_total", section.estimatedSubTotal);
            }

            if (estimatedCashFlow != null) {
                JsonObject estimatedCashFlowObj = createCashFlow(request.estimatedCashFlow);

                object.add("estimated_cash_flow", estimatedCashFlowObj);
            }

//        }
//        if (actualCashFlow != null){
            if (section.title.equalsIgnoreCase("Revenue")) {
                object.addProperty("achieved_revenue", section.actualRevenue);
                object.addProperty("actual_sub_total", section.actualSubTotal);
            } else {
                object.addProperty("achieved_sub_total", section.actualSubTotal);
            }
            if (actualCashFlow != null) {
                JsonObject actualCashFlowObj = createCashFlow(request.actualCashFlow);
                object.add("achieved_cash_flow", actualCashFlowObj);
            }

//        }

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

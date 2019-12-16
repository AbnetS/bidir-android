package com.gebeya.mobile.bidir.data.acatitem.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemHelper;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemRequest;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by abuti on 6/5/2018.
 */

public class ACATItemRemote extends BaseRemoteSource<ACATItemService> implements ACATItemRemoteSource{
    @Inject BaseACATItemResponseParser acatItemResponseParser;

    @Inject
    public ACATItemRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.ACAT, ACATItemService.class);
    }

    @Override
    public Observable<ACATItemResponse> updateACATItem
            (@NonNull String acatItemId, @NonNull ACATItemRequest request){
        return build()
                .call(service.updateCostListItem(acatItemId, createACATItemRequest(request)))
                .map(updated -> {
                    ACATItemResponse response = acatItemResponseParser.parse(updated,request.acatItem.sectionId,
                            request.acatItem.costListId, request.acatItem.groupedListId, ACATItemHelper.COST_CATEGORY);
                    return response;
                });

    }

    @Override
    public Observable<ACATItemResponse> updateRevenueItem(@NonNull String acatItemId, @NonNull ACATItemRequest request) {
        return build()
                .call(service.updateCostListItem(acatItemId, createACATItemRequest(request)))
                .map(updated -> {
                    ACATItemResponse response = acatItemResponseParser.parse(updated,request.acatItem.sectionId,
                            request.acatItem.costListId, request.acatItem.groupedListId, ACATItemHelper.REVENUE_CATEGORY);
                    return response;
                });
    }

    @Override
    public Observable<ACATItemResponse> registerACATItem (@NonNull ACATItemRequest request){
        return build()
                .call(service.addCostListItem(createACATItemRequestForRegister(request)))
                .map(added -> {
                    ACATItemResponse response = acatItemResponseParser.parse(added,request.acatItem.sectionId,
                            request.acatItem.costListId, request.acatItem.groupedListId, ACATItemHelper.COST_CATEGORY);
                    return response;
                });

    }

    private JsonObject createACATItemRequest(@NonNull ACATItemRequest request){
        final JsonObject object = new JsonObject();

        ACATItem acatItem = request.acatItem;
        ACATItemValue estimatedACATItemValue = request.estimatedAcatItemValue;
        ACATItemValue actualACATItemValue = request.actualAcatItemValue;
        CashFlow estimatedCashFlow = request.estimatedCashFlow;
        CashFlow actualCashFlow = request.actualCashFlow;

        object.addProperty("is_client_acat", true);
        object.addProperty("client_acat", request.acatApplicationId);
        object.addProperty("item", acatItem.item);
        object.addProperty("unit", acatItem.unit);
        object.addProperty("remark", acatItem.remark);

        if (request.estimatedAcatItemValue != null)
            object.add("estimated", createEstimatedValues(request));

        if (request.actualAcatItemValue != null)
            object.add("achieved", createActualValues(request));

        return object;

    }

    private JsonObject createACATItemRequestForRegister(@NonNull ACATItemRequest request){
        final JsonObject object = new JsonObject();

        ACATItem acatItem = request.acatItem;
        ACATItemValue estimatedACATItemValue = request.estimatedAcatItemValue;
        ACATItemValue actualACATItemValue = request.actualAcatItemValue;
        CashFlow estimatedCashFlow = request.estimatedCashFlow;
        CashFlow actualCashFlow = request.actualCashFlow;

        if (acatItem.groupedListId == null){
            object.addProperty("type", "linear");
            String costListId = acatItem.costListId.replaceAll("^\"|\"$", "");
            object.addProperty("parent_cost_list", costListId);
        }
        else
        {
            object.addProperty("parent_grouped_list", acatItem.groupedListId);
        }
        object.addProperty("item", acatItem.item);
        object.addProperty("unit", acatItem.unit);

        return object;

    }


//    @Override
//    public Observable<ACATItemResponse> updateEstimatedValueOfACATItem(@NonNull ACATItemDto acatItemDto,
//                                                                       @NonNull String acatItemId) {
//        final JsonObject request = new JsonObject();
//
//        if (acatItemDto.item != null) request.addProperty("item", acatItemDto.item);
//        if (acatItemDto.unit != null) request.addProperty("unit", acatItemDto.unit);
//        if (acatItemDto.remark != null) request.addProperty("remark", acatItemDto.remark);
//
//        request.add("estimated", createEstimatedValues(acatItemDto));
//
//        return build()
//                .call(service.updateCostListItem(acatItemId, request))
//                .map(updated -> {
//                    ACATItemResponse pageResponse = acatItemResponseParser.parse(updated,acatItemDto.sectionId, acatItemDto.costListId,
//                            acatItemDto.groupListId, "COST");
//                    return pageResponse;
//                });
//        //return Completable.complete();
//    }

//    @Override
//    public Observable<ACATItemResponse> updateActualValueOfACATItem(@NonNull ACATItemDto acatItemDto,
//                                                                       @NonNull String acatItemId) {
//        final JsonObject request = new JsonObject();
//
//        if (acatItemDto.item != null) request.addProperty("item", acatItemDto.item);
//        if (acatItemDto.unit != null) request.addProperty("unit", acatItemDto.unit);
//        if (acatItemDto.remark != null) request.addProperty("remark", acatItemDto.remark);
//
//        request.add("achieved", createActualValues(acatItemDto));
//
//        return build()
//                .call(service.updateCostListItem(acatItemId, request))
//                .map(updated -> {
//                    ACATItemResponse pageResponse = acatItemResponseParser.parse(updated,acatItemDto.sectionId, acatItemDto.costListId,
//                            acatItemDto.groupListId, "COST");
//                    return pageResponse;
//                });
//        //return Completable.complete();
//    }



    private JsonObject createEstimatedValues(@NonNull ACATItemRequest acatItemRequest) {
        final JsonObject object = new JsonObject();


        object.addProperty("value",acatItemRequest.estimatedAcatItemValue.value);
        object.addProperty("unit_price", acatItemRequest.estimatedAcatItemValue.unitPrice);
        object.addProperty("total_price", acatItemRequest.estimatedAcatItemValue.totalPrice);

        JsonObject estimatedCashFlow = createCashFlow(acatItemRequest.estimatedCashFlow);
        object.add("cash_flow",estimatedCashFlow);

        return object;
    }

    private JsonObject createActualValues(@NonNull ACATItemRequest acatItemRequest) {
        final JsonObject object = new JsonObject();

        object.addProperty("value", acatItemRequest.actualAcatItemValue.value);
        object.addProperty("unit_price", acatItemRequest.actualAcatItemValue.unitPrice);
        object.addProperty("total_price", acatItemRequest.actualAcatItemValue.totalPrice);

        JsonObject actualCashFlow = createCashFlow(acatItemRequest.actualCashFlow);
        object.add("cash_flow",actualCashFlow);

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

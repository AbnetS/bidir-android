package com.gebeya.mobile.bidir.data.acatapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.remote.CashFlowParser;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATResponseParser;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.data.pagination.remote.PageParser;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
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
 * Created by abuti on 5/17/2018.
 */

public class ACATApplicationRemote extends BaseRemoteSource<ACATApplicationService>  implements ACATApplicationRemoteSource {

    @Inject
    ACATApplicationParser acatApplicationParser;

    @Inject
    CropACATResponseParser cropACATResponseParser;

    @Inject
    CashFlowParser cashFlowParser;

    @Inject
    PageParser pageParser;

    @Inject
    public ACATApplicationRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.ACAT_PROCESSOR, ACATApplicationService.class);
    }

    @Override
    public Observable<List<ACATApplicationResponse>> getAll() {
        return build()
                .call(service.getAll())
                .map(object -> {
                    final List<ACATApplicationResponse> responses = new ArrayList<>();
                    final JsonArray docs = object.getAsJsonArray("docs");
                    final int size = docs.size();

                    for (int i = 0; i < size; i++) {
                        JsonObject acatApplicationObject = docs.get(i).getAsJsonObject();
                        ACATApplicationResponse response = parse(acatApplicationObject);//, acatApplicationID);
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

        return pageParser.parse(object, Service.ACAT_PROCESSOR);
    }

    @Override
    public PaginatedACATApplicationResponse getAllPaginated(int currentPage) throws Exception {
        build();

        final Response<JsonObject> apiResponse = service.getAllByPage(currentPage).execute();
        final JsonObject object = apiResponse.body();

        final PaginatedACATApplicationResponse paginatedResponse = new PaginatedACATApplicationResponse();
        final List<ACATApplicationResponse> responses = new ArrayList<>();
        final JsonArray docs = object.getAsJsonArray("docs");
        final int size = docs.size();

        for (int i = 0; i < size; i++) {
            JsonObject acatApplicationObject = docs.get(i).getAsJsonObject();
            ACATApplicationResponse response = parse(acatApplicationObject);
            responses.add(response);
        }

        paginatedResponse.applicationResponses = responses;
        paginatedResponse.pageResponse = pageParser.parse(object, Service.ACAT_PROCESSOR);

        return paginatedResponse;
    }

    @Override
    public Observable<ACATApplicationResponse> fetchACATApplication(@NonNull String clientId) {
        return build()
                .call(service.getOne(clientId))
                .map(this::parse);
    }

    @Override
    public Observable<ACATApplicationResponse> getACAT(@NonNull String clientACATId) {
        return build()
                .call(service.getOne(clientACATId))
                .map(this::parse);
    }

    @Override
    public ACATApplicationResponse parseACATApplication(@NonNull JsonObject object, @NonNull String acatApplicationID) throws Exception {
        try {
            final ACATApplicationResponse response = new ACATApplicationResponse();

            response.acatApplication = acatApplicationParser.parse(object);

            final JsonArray ACATsObject = object.getAsJsonArray("docs");
            final int size = ACATsObject.size();

            for (int i = 0; i < size; i++) {
                JsonObject cropACATObject = ACATsObject.get(i).getAsJsonObject();
                response.cropACATs.add(cropACATResponseParser.parse(cropACATObject,acatApplicationID));
            }

            return response;
        } catch (Exception e) {
            throw new Exception("Error parsing ACAT Application: " + e);
        }
    }

    @Override
    public ACATApplicationResponse parse(@NonNull JsonObject object) throws Exception {
        try {
            final ACATApplicationResponse response = new ACATApplicationResponse();
            List <CropACATResponse> cropACATS = new ArrayList<>();

            //JsonArray acatApplicationArray = object.getAsJsonArray("docs");
            //final int size = acatApplicationArray.size();

            //for (int i = 0; i < size; i++) {
                //JsonObject acatApplicationObject = acatApplicationArray.getByType(i).getAsJsonObject();
            JsonObject acatApplicationObject = object;
                String acatApplicationID = acatApplicationObject.get("_id").getAsString();
                response.acatApplication = acatApplicationParser.parse(acatApplicationObject);

                final JsonObject estimated = acatApplicationObject.get("estimated").getAsJsonObject();
                final JsonObject estNetCashFlowObj = estimated.get("net_cash_flow").getAsJsonObject();
                response.estimatedNetCashFlow = cashFlowParser.parse(estNetCashFlowObj,
                        acatApplicationID, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                        CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE); //Found out only Net Cashflow is applicable for ACAT Application
            //TODO: Check this fact with Clients.

                final JsonObject actual = acatApplicationObject.get("achieved").getAsJsonObject();
                final JsonObject actNetCashFlowObj = actual.get("net_cash_flow").getAsJsonObject();
                response.actualNetCashFlow = cashFlowParser.parse(actNetCashFlowObj,acatApplicationID,
                    CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE);


                JsonArray cropACATArray = acatApplicationObject.get("ACATs").getAsJsonArray();
                if(cropACATArray.size() > 0) {
                    for (int j = 0; j < cropACATArray.size(); j++) {
                        JsonObject cropACATObject = cropACATArray.get(j).getAsJsonObject();
                        cropACATS.add(cropACATResponseParser.parse(cropACATObject, acatApplicationID));
                    }
                }

            response.cropACATs = cropACATS;
                    //}

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Application: " + e.getMessage());
        }
    }


    @Override
    public Observable<ACATApplicationResponse> initializeACAT(@NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs) {
        final JsonObject request = new JsonObject();
        request.addProperty("client", client._id);
        request.addProperty("loan_product", loanProduct._id);
        request.add("crop_acats", toJson(cropACATs));

        return build()
                .call(service.initializeClientACAT(request))
                .map(object -> parse(object));
//        return build()
//                .call(service.getOneByClient(client._id))
//                .map(this::parse);
    }

    @Override
    public Observable<ACATApplicationResponse> update(@NonNull String acatApplicationId, @NonNull ACATApplicationRequest request) {

        return build()
                .call(service.update(acatApplicationId, createAcatApplicationRequest(request)))
                .map(this::parse);
    }

    @Override
    public Observable<ACATApplicationResponse> updateApiStatus(@NonNull ACATApplication acatApplication, @NonNull String status) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", status);
        return build()
                .call(service.update(acatApplication._id, request))
                .map(this::parse);
    }

    private JsonObject createAcatApplicationRequest (@NonNull ACATApplicationRequest request){
        final JsonObject object = new JsonObject();

        final ACATApplication acatApplication = request.acatApplication;

        object.addProperty("status", acatApplicationParser.getApiStatus(request.acatApplication.status));

        object.add("estimated", createEstimatedValues(request));
        object.add("achieved", createActualValues(request));

        return object;
    }

    private JsonObject createEstimatedValues(@NonNull ACATApplicationRequest request) {
        final JsonObject object = new JsonObject();

        object.addProperty("total_cost", request.acatApplication.estimatedTotalCost);
        object.addProperty("total_revenue", request.acatApplication.estimatedTotalRevenue);
        object.addProperty("net_income", request.acatApplication.estimatedNetIncome);

        if (request.estimatedCashFlow != null) {
            JsonObject estimatedCashFlow = createCashFlow(request.estimatedCashFlow);
            object.add("net_cash_flow", estimatedCashFlow);
        }

        return object;
    }

    private JsonObject createActualValues(@NonNull ACATApplicationRequest request) {
        final JsonObject object = new JsonObject();

        object.addProperty("total_cost", request.acatApplication.actualTotalCost);
        object.addProperty("total_revenue", request.acatApplication.actualTotalRevenue);
        object.addProperty("net_income", request.acatApplication.actualNetIncome);

        if (request.actualCashFlow != null) {
            JsonObject actualCashFlow = createCashFlow(request.actualCashFlow);
            object.add("net_cash_flow", actualCashFlow);
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

    private JsonArray toJson(@NonNull List<String> items) {
        final JsonArray array = new JsonArray();
        for(int i = 0; i < items.size(); i++) {
            array.add(items.get(i));
        }

        return array;
    }
}

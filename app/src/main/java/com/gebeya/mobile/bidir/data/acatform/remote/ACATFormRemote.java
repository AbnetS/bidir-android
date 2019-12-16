package com.gebeya.mobile.bidir.data.acatform.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.BaseACATRevenueSectionResponseParser;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.remote.CashFlowParser;
import com.gebeya.mobile.bidir.data.crop.Crop;
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
 * Implementation for the {@link ACATFormRemoteSource} interface
 */

public class ACATFormRemote extends BaseRemoteSource<ACATFormService> implements ACATFormRemoteSource {

    // TODO: SAM - Provide helper method to parseACATApplication all the

    @Inject
    ACATFormParser acatFormParser;

    @Inject
    PageParser pageParser;

    @Inject
    CashFlowParser cashFlowParser;

    @Inject
    ACATCostSectionResponseParser acatCostSectionResponseParser;

    @Inject
    BaseACATRevenueSectionResponseParser acatRevenueSectionResponseParser;

    final List<String> crops = new ArrayList<>();

    @Inject
    public ACATFormRemote() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        setParams(Service.ACAT_FORM, ACATFormService.class);
    }

    @Override
    public Observable<List<ACATFormResponse>> getAll() {
        return build()
                .call(service.getAll())
                .map(object -> {
                   final List<ACATFormResponse> responses = new ArrayList<>();
                   final JsonArray docs = object.getAsJsonArray("docs");
                   final int size = docs.size();
                   for (int i = 0; i < size; i++) {
                       JsonObject acatFormObject = docs.get(i).getAsJsonObject();
                       ACATFormResponse response = parse(acatFormObject);
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

        return pageParser.parse(object, Service.ACAT_FORM);
    }

    @Override
    public PaginatedACATFormResponse getAllPaginated(int currentPage) throws Exception {
        build();
        final Response<JsonObject> apiResponse = service.getAllByPage(currentPage).execute();
        final JsonObject object = apiResponse.body();

        final PaginatedACATFormResponse paginatedResponse = new PaginatedACATFormResponse();

        final List<ACATFormResponse> responses = new ArrayList<>();
        final JsonArray docs = object.getAsJsonArray("docs");
        final PageResponse pageResponse = pageParser.parse(object, Service.ACAT_FORM);
        final int size = docs.size();
        for (int i = 0; i < size; i++) {
            JsonObject acatFormObject = docs.get(i).getAsJsonObject();
            ACATFormResponse response = parse(acatFormObject);
            responses.add(response);
        }
        paginatedResponse.responses = responses;
        paginatedResponse.pageResponse = pageResponse;

        return paginatedResponse;
    }

    @Override
    public ACATFormResponse parse(@NonNull JsonObject object) throws Exception {
        final ACATFormResponse response = new ACATFormResponse();


        //final ACATForm acatForm = parseForm(object); //I have moved the method to a standalone class
        final ACATForm acatForm = acatFormParser.parse(object);

        response.acatForm = acatForm;

        final Crop crop = parseCrop(object.get("crop").getAsJsonObject());       // TODO: Figure out why this is not an object
        crop.acatId = acatForm._id;
        acatForm.cropName = crop.name;
//        pageResponse.acatForm.cropNames = crops;

        final JsonArray formSections = object.get("sections").getAsJsonArray();
        final JsonObject costSection = formSections.get(0).getAsJsonObject(); //Get the first section as the cost section
        final JsonObject revenueSection = formSections.get(1).getAsJsonObject(); //Get the first section as the cost section

        response.costSection = acatCostSectionResponseParser.parse (costSection);
//        pageResponse.revenueSection = acatRevenueSectionResponseParser.parse(revenueSection);

        final JsonObject estimated = object.get("estimated").getAsJsonObject();
        response.estimatedNetCashFlow =  cashFlowParser.parse (estimated.get("net_cash_flow").getAsJsonObject(),
                object.get("_id").getAsString(), "ESTIMATED", "FORM_CASH_FLOW");

        final JsonObject actual = object.get("achieved").getAsJsonObject();
        response.actualNetCashFlow =  cashFlowParser.parse (actual.get("net_cash_flow").getAsJsonObject(),
                object.get("_id").getAsString(), "ACTUAL", "FORM_CASH_FLOW");

        response.crop = crop;
        return response;
    }

    @Override
    public ACATForm parseForm(@NonNull JsonObject object) throws Exception {
        try {
            final ACATForm acatForm = new ACATForm();

            acatForm._id = object.get("_id").getAsString();
            acatForm.type = object.get("type").getAsString();
            acatForm.createdBy = object.get("created_by").getAsString();
            acatForm.title = object.get("title").getAsString();
            acatForm.layout = object.get("layout").getAsString();

            return acatForm;
        } catch (Exception e) {
            throw new Exception("Error parsing ACAT Form: " + e);
        }
    }

    @Override
    public Crop parseCrop(@NonNull JsonObject object) throws Exception {

        try {
            final Crop crop = new Crop();

            crop._id = object.get("_id").getAsString();
            crop.category = object.get("category").getAsString();
            crop.name = object.get("name").getAsString();

            return crop;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Crop: " + e);
        }
    }


}

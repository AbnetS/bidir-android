package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.support.annotation.Nullable;


import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationSync;

import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionRequest;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionRemoteSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemRequest;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemRemoteSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.ACATRevenueSectionRemoteSource;
import com.gebeya.mobile.bidir.data.acatsection.local.ACATSectionLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.BaseSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.client.ClientSyncState;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;

import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATRequest;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATRemoteSource;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocationResponse;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.data.marketdetails.MarketDetailsResponse;
import com.gebeya.mobile.bidir.data.marketdetails.local.MarketDetailsLocalSource;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.YieldConsumptionRemoteSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.reactivex.Observable;

/**
 * Created by Dawit on 9/29/2018.
 */

public class ACATSyncService extends BaseSyncService {

    @Inject BoxStore store;

    @Inject ACATApplicationSyncLocalSource acatSyncLocal;
    @Inject ACATApplicationRemoteSource acatSyncRemote;

    @Inject ACATApplicationLocalSource acatApplicationLocalSource;

    @Inject CashFlowLocalSource cashFlowLocalSource;

    @Inject ACATSyncState state;

    @Inject ACATCostSectionRemoteSource acatCostSectionRemoteSource;
    @Inject ACATCostSectionLocalSource acatCostSectionLocalSource;

    @Inject ACATRevenueSectionLocalSource acatRevenueSectionLocalSource;
    @Inject ACATRevenueSectionRemoteSource acatRevenueSectionRemoteSource;

    @Inject ACATItemLocalSource acatItemLocalSource;
    @Inject ACATItemRemoteSource acatItemRemoteSource;

    @Inject CropACATLocalSource cropACATLocalSource;
    @Inject CropACATRemoteSource cropACATRemoteSource;

    @Inject ACATItemValueLocalSource acatItemValueLocalSource;

    @Inject GPSLocationLocalSource gpsLocationLocalSource;

    @Inject YieldConsumptionLocalSource yieldConsumptionLocalSource;
    @Inject YieldConsumptionRemoteSource yieldConsumptionRemoteSource;

    @Inject MarketDetailsLocalSource marketDetailsLocalSource;

    private ACATApplication acatApplication;

    public ACATSyncService() {
        super(ACATSyncService.class.getSimpleName());

        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        d("ACAT Service Started");
        if (state.busy()) {
            d("-> Service is already running");
            return;
        }

        d("Getting a list of modified ACAT applications to upload");
        final List<ACATApplicationSync> modifiedACATApplications = acatSyncLocal.getAllModifiedACATApplication();
        final int modifiedLength = modifiedACATApplications.size();
        if (modifiedLength == 0) {
            d("-> No modified ACAT Applications to upload found");
        } else {
            d("-> Found %d modified ACAT Applications to upload", modifiedLength, modifiedLength == 1 ? "" : "s");
            d("Uploading data...");
            state.setBusy();
            for (int i = 0; i < modifiedLength; i++) {
                final ACATApplicationSync acatApplicationSync = modifiedACATApplications.get(i);
                d("-> Uploading acatApplicationSync: " + acatApplicationSync.acatAppId);

                // Uploading ACAT Application
                ACATApplicationRequest request = new ACATApplicationRequest();

                acatApplicationLocalSource.get(acatApplicationSync.acatAppId)
                        .subscribe(acatApplicationData -> {
                            request.acatApplication = acatApplicationData.get();
                        });

                cashFlowLocalSource.get(acatApplicationSync.acatAppId, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE)
                        .subscribe(cashFlows ->
                                request.estimatedCashFlow = cashFlows.get(0));
                cashFlowLocalSource.get(acatApplicationSync.acatAppId, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE)
                        .subscribe(cashFlows ->
                                request.actualCashFlow = cashFlows.get(0));

                acatSyncRemote
                        .update(acatApplicationSync.acatAppId, request)
                        .subscribe(uploaded -> {
                            d("-> ACAT Application %s uploaded", acatApplicationSync.acatAppId);
                            acatApplication = uploaded.acatApplication;
                            uploaded.acatApplication.uploaded = true;
                            uploaded.acatApplication.modified = false;
                            uploaded.acatApplication.updatedAt = new DateTime();
                            acatApplicationLocalSource.put(uploaded.acatApplication);
                            // TODO: 9/29/2018 Save ACAT Application Locally
                            acatApplicationSync.uploaded = true;
                            acatApplicationSync.modified = false;
                            acatSyncLocal.put(acatApplicationSync);

                        }, throwable -> {
                            e("-> Uploading acatApplicationSync %s failed:", acatApplicationSync.acatAppId);
                            acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                            throwable.printStackTrace();
                        });

                // Uploading Crop ACAT Application
                Observable.fromIterable(acatApplicationSync.cropACATIds)
                        .subscribe(s -> {
                            CropACATRequest cropACATRequest = new CropACATRequest();

                            cropACATLocalSource.get(s)
                                    .subscribe(cropACATData ->
                                            cropACATRequest.cropACAT = cropACATData.get());

                            cashFlowLocalSource.get(cropACATRequest.cropACAT._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE)
                                    .subscribe(cashFlows ->
                                            cropACATRequest.estimatedCashFlow = cashFlows.get(0));
                            cashFlowLocalSource.get(cropACATRequest.cropACAT._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE)
                                    .subscribe(cashFlows ->
                                            cropACATRequest.actualCashFlow = cashFlows.get(0));

                            cropACATRequest.gpsLocationResponse = new GPSLocationResponse();
                            cropACATRequest.gpsLocationResponse.gpsLocations = new ArrayList<>();
                            gpsLocationLocalSource.getAllCropACATGPSLocations(cropACATRequest.cropACAT._id,
                                    cropACATRequest.cropACAT.acatApplicationID)
                                    .subscribe(gpsLocations -> {
                                        cropACATRequest.gpsLocationResponse.gpsLocations = gpsLocations;
                                    });

                            cropACATRemoteSource.updateCropACATInstance(cropACATRequest.cropACAT._id, acatApplicationSync.acatAppId, cropACATRequest)
                                    .subscribe(acatItemResponse -> {
                                        d("-> Crop ACAT %s uploaded", cropACATRequest.cropACAT._id);
                                        acatApplicationSync.uploaded = true;
                                        acatApplicationSync.modified = false;
                                        acatSyncLocal.put(acatApplicationSync);
                                    }, throwable -> {
                                        e("-> Uploading Crop ACAT %s failed:", cropACATRequest.cropACAT._id);
                                        acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                                        throwable.printStackTrace();
                                    });
                        });

                // Uploading All ACAT Items
                Observable.fromIterable(acatApplicationSync.acatItemIds)
                        .subscribe(s -> {
                            ACATItemRequest acatItemRequest = new ACATItemRequest();

                            acatItemLocalSource.get(s)
                                    .subscribe(acatItemData -> {
                                        e(s);
                                        if (!acatItemData.empty())
                                            acatItemRequest.acatItem = acatItemData.get();
                                        acatApplicationSync.uploaded = true;
                                        acatApplicationSync.modified = false;
                                    });

                            acatItemRequest.acatApplicationId = acatApplicationSync.acatAppId;

                            cashFlowLocalSource.get(acatItemRequest.acatItem._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.ITEM_CASH_FLOW)
                                    .subscribe(cashFlows ->
                                            acatItemRequest.estimatedCashFlow = cashFlows.get(0));
                            cashFlowLocalSource.get(acatItemRequest.acatItem._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.ITEM_CASH_FLOW)
                                    .subscribe(cashFlows ->
                                            acatItemRequest.actualCashFlow = cashFlows.get(0));

                            acatItemValueLocalSource.getByType(acatItemRequest.acatItem._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE)
                                    .subscribe(acatItemValueData -> acatItemRequest.estimatedAcatItemValue = acatItemValueData.get());
                            acatItemValueLocalSource.getByType(acatItemRequest.acatItem._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE)
                                    .subscribe(acatItemValueData -> acatItemRequest.actualAcatItemValue = acatItemValueData.get());

                            //Check if it is a newly added item
                            if (acatItemRequest.acatItem.pendingCreation){
                                acatItemRemoteSource.registerACATItem(acatItemRequest)
                                        .subscribe(acatItemResponse -> {
                                            d("-> ACAT Item %s uploaded", acatItemRequest.acatItem.item);
                                            //TODO: Save the added item locally.
                                            acatItemLocalSource.updateACATItemId(acatItemRequest.acatItem, acatItemResponse.acatItem._id);
                                            acatItemValueLocalSource.updateACATItemRefId(acatItemRequest.estimatedAcatItemValue, acatItemResponse.acatItem._id);
                                            cashFlowLocalSource.updateACATItemRefId(acatItemRequest.estimatedCashFlow,  acatItemResponse.acatItem._id);
                                            acatApplicationSync.acatItemIds.remove(acatItemRequest.acatItem._id);
                                            acatApplicationSync.acatItemIds.add(acatItemResponse.acatItem._id);
                                            acatSyncLocal.put(acatApplicationSync);
                                            acatItemRequest.acatItem._id = acatItemResponse.acatItem._id;

                                            acatItemRequest.estimatedAcatItemValue.acatItemId = acatItemResponse.acatItem._id;
                                            acatItemRequest.estimatedCashFlow.referenceId = acatItemResponse.acatItem._id;
                                            acatApplicationSync.uploaded = true;
                                            acatApplicationSync.modified = false;
                                            acatApplication.pendingCreation = false;
                                            acatApplicationLocalSource.put(acatApplication);
                                        }, throwable -> {
                                            e("-> Uploading acatItem %s failed:", acatItemRequest.acatItem.item);
                                            acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                                            throwable.printStackTrace();
                                        });
                            }
                            //else {

                                acatItemRemoteSource.updateACATItem(acatItemRequest.acatItem._id, acatItemRequest)
                                        .subscribe(acatItemResponse -> {
                                            d("-> ACAT Item %s uploaded", acatItemRequest.acatItem.item);
                                            acatApplicationSync.uploaded = true;
                                            acatApplicationSync.modified = false;
                                            acatSyncLocal.put(acatApplicationSync);
                                        }, throwable -> {
                                            e("-> Uploading acatItem %s failed:", acatItemRequest.acatItem.item);
                                            acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                                            throwable.printStackTrace();
                                        });
                            //}
                        }, throwable -> {
                            acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                            throwable.printStackTrace();
                        });

                // Upload All ACAT Cost Sections
                Observable.fromIterable(acatApplicationSync.costSectionIds)
                        .subscribe(s -> {
                            ACATCostSectionRequest acatCostSectionRequest = new ACATCostSectionRequest();

                            acatCostSectionLocalSource.get(s)
                                    .subscribe(acatCostSectionData -> acatCostSectionRequest.section = acatCostSectionData.get());

                            acatCostSectionRequest.acatApplicationId = acatApplicationSync.acatAppId;

                            cashFlowLocalSource.get(acatCostSectionRequest.section._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                                    .subscribe(cashFlows ->
                                            acatCostSectionRequest.estimatedCashFlow = cashFlows.get(0));

                            cashFlowLocalSource.get(acatCostSectionRequest.section._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                                    .subscribe(cashFlows ->
                                            acatCostSectionRequest.actualCashFlow = cashFlows.get(0));

                            acatCostSectionRemoteSource.updateACATCostSection(acatCostSectionRequest.section._id, acatCostSectionRequest)
                                    .subscribe(acatItemResponse -> {
                                        d("-> ACAT Section %s uploaded", acatCostSectionRequest.section._id);
                                        acatApplicationSync.uploaded = true;
                                        acatApplicationSync.modified = false;
                                        acatSyncLocal.put(acatApplicationSync);
                                    }, throwable -> {
                                        e("-> Uploading ACAT Section %s failed:", acatCostSectionRequest.section._id);
                                        acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                                        throwable.printStackTrace();
                                    });
                        });

                //Upload All ACAT Revenue Sections
                Observable.fromIterable(acatApplicationSync.revenueSectionIds)
                        .subscribe(s -> {
                            ACATRevenueSectionRequest acatRevenueSectionRequest = new ACATRevenueSectionRequest();

                            acatRevenueSectionLocalSource.get(s)
                                    .subscribe(acatRevenueSectionData ->
                                            acatRevenueSectionRequest.section = acatRevenueSectionData.get());

                            acatRevenueSectionRequest.acatApplicationId = acatApplicationSync.acatAppId;

                            cashFlowLocalSource.get(acatRevenueSectionRequest.section._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                                    .subscribe(cashFlows ->
                                            acatRevenueSectionRequest.estimatedCashFlow = cashFlows.get(0));

                            cashFlowLocalSource.get(acatRevenueSectionRequest.section._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                                    .subscribe(cashFlows ->
                                            acatRevenueSectionRequest.actualCashFlow = cashFlows.get(0));

                            acatRevenueSectionRemoteSource.updateACATRevenueSection(acatRevenueSectionRequest.section._id, acatRevenueSectionRequest)
                                    .subscribe(acatRevenueSection -> {
                                        d("-> ACAT Revenue Section %s uploaded", acatRevenueSectionRequest.section._id);
                                        acatApplicationSync.uploaded = true;
                                        acatApplicationSync.modified = false;
                                        acatSyncLocal.put(acatApplicationSync);
                                    }, throwable -> {
                                        e("-> Uploading ACAT Revenue Section %s failed:", acatRevenueSectionRequest.section._id);
                                        acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                                        throwable.printStackTrace();
                                    });
                        });

                //Upload All Yield Consumptions
                Observable.fromIterable(acatApplicationSync.yieldConsumptionIds)
                        .subscribe(s -> {
                            YieldConsumptionRequest yieldConsumptionRequest = new YieldConsumptionRequest();

                            yieldConsumptionLocalSource.get(s)
                                    .subscribe(yieldConsumptionData -> {
                                        yieldConsumptionRequest.yieldConsumption = yieldConsumptionData.get();
                                    });

                            marketDetailsLocalSource.getByYieldConsumption(yieldConsumptionRequest.yieldConsumption._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE)
                                    .subscribe(marketDetails -> {
                                        MarketDetailsResponse  response = new MarketDetailsResponse();
                                        response.marketDetails = marketDetails;
                                        yieldConsumptionRequest.estimatedMarketDetails = response;
                                    });

                            marketDetailsLocalSource.getByYieldConsumption(yieldConsumptionRequest.yieldConsumption._id, CashFlowHelper.ACTUAL_CASH_FLOW_TYPE)
                                    .subscribe(marketDetails ->  {
                                        MarketDetailsResponse response = new MarketDetailsResponse();
                                        response.marketDetails = marketDetails;
                                        yieldConsumptionRequest.actualMarketDetails = response;
                                    });

                            yieldConsumptionRemoteSource.updateYieldConsumption(yieldConsumptionRequest.yieldConsumption._id, yieldConsumptionRequest)
                                    .subscribe(yieldConsumption -> {
                                        d("-> Yield Consumption %s uploaded", yieldConsumptionRequest.yieldConsumption._id);
                                        acatApplicationSync.uploaded = true;
                                        acatApplicationSync.modified = false;
                                        acatSyncLocal.put(acatApplicationSync);
                                    }, throwable -> {
                                        e("-> Uploading Yield Consumption %s failed:", yieldConsumptionRequest.yieldConsumption._id);
                                        acatSyncLocal.markForUpload(acatApplicationSync.acatAppId);
                                        throwable.printStackTrace();

                                    });
                        });


            }
        }


        state.setIdle();
    }
}

package com.gebeya.mobile.bidir.data.acatapplication.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationRequest;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationSync;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemote;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionResponse;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.costlist.CostListResponse;
import com.gebeya.mobile.bidir.data.costlist.local.CostListLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATParser;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedListResponse;
import com.gebeya.mobile.bidir.data.groupedlist.local.GroupedListLocalSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.data.marketdetails.MarketDetailsResponse;
import com.gebeya.mobile.bidir.data.marketdetails.local.MarketDetailsLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/18/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATApplicationRepository implements ACATApplicationRepositorySource {

    @Inject ACATApplicationRemote remote;
    @Inject ACATApplicationLocalSource localACATApplication;
    @Inject CropACATLocalSource localCropACAT;
    @Inject CashFlowLocalSource localCashFlow;
    @Inject ACATCostSectionLocalSource localCostSection;
    @Inject ACATRevenueSectionLocalSource localRevenue;
    @Inject YieldConsumptionLocalSource localYieldCons;
    @Inject CostListLocalSource localCostList;
    @Inject MarketDetailsLocalSource localMarketDetails;
    @Inject ACATItemLocalSource localACATItem;
    @Inject ACATItemValueLocalSource localACATItemValue;
    @Inject GroupedListLocalSource localGroupedList;
    @Inject GPSLocationLocalSource localGPSLocation;
    @Inject ACATApplicationSyncLocalSource localSync;

    public ACATApplicationRepository() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable saveACATRevenueComponentsLocally(@NonNull ACATApplicationResponse response) {
        for (CropACATResponse cropACATResponse : response.cropACATs) {
            final ACATRevenueSectionResponse revenueSectionResponse = cropACATResponse.revenueSection;

            localRevenue.putAll(revenueSectionResponse.sections);
            localCashFlow.putAll(revenueSectionResponse.estimatedCashFlows);
            localCashFlow.putAll(revenueSectionResponse.actualCashFlows);

            for (ACATItemResponse acatItemResponse : revenueSectionResponse.yields) {
                localACATItem.put(acatItemResponse.acatItem);
                localCashFlow.put(acatItemResponse.estimatedCashFlow);
                localCashFlow.put(acatItemResponse.actualCashFlow);
                localACATItemValue.put(acatItemResponse.estimatedAcatItemValue);
                localACATItemValue.put(acatItemResponse.actualAcatItemValue);
            }

            if (revenueSectionResponse.yieldConsumptions != null) {
                for (YieldConsumptionResponse yieldResponse : revenueSectionResponse.yieldConsumptions) {

                    localYieldCons.put(yieldResponse.yieldConsumption);

                    final MarketDetailsResponse estimatedMarketDetailsResponse = yieldResponse.estimatedMarketDetails;
                    // TODO: 6/7/2018 Check this later
                    if (estimatedMarketDetailsResponse.marketDetails != null) localMarketDetails.putAll(estimatedMarketDetailsResponse.marketDetails);
                    final MarketDetailsResponse actualMarketDetailsResponse = yieldResponse.actualMarketDetails;
                    if (actualMarketDetailsResponse.marketDetails != null) localMarketDetails.putAll(actualMarketDetailsResponse.marketDetails);
                }
            }

        }

        return Completable.complete();
    }

    @Override
    public Completable saveACATCostComponentsLocally(@NonNull ACATApplicationResponse response) {
        localACATApplication.put(response.acatApplication);

        localCashFlow.put(response.estimatedNetCashFlow);
        localCashFlow.put(response.actualNetCashFlow);

        for (CropACATResponse cropACATResponse : response.cropACATs) {
            final CropACAT cropACAT = cropACATResponse.cropACAT;
            localCropACAT.put(cropACAT);

            localCashFlow.put(cropACATResponse.estimatedNetCashFlow);
            localCashFlow.put(cropACATResponse.actualNetCashFlow);

//            for (GPSLocation gpsLocation: cropACATResponse.gpsLocation.gpsLocations){
//                localGPSLocation.put(gpsLocation);
//            }
            localGPSLocation.putAll(cropACATResponse.gpsLocation.gpsLocations,cropACAT._id, cropACAT.acatApplicationID);

            final ACATCostSectionResponse costSectionResponse = cropACATResponse.costSection;
            for (ACATCostSection section : costSectionResponse.sections){
                localCostSection.put(section);
            }

            for (CashFlow cashFlow : costSectionResponse.estimatedCashFlows){
                localCashFlow.put(cashFlow);
            }
            for (CashFlow cashFlow : costSectionResponse.actualCashFlows){
                localCashFlow.put(cashFlow);
            }

            for (CostListResponse costListResponse : costSectionResponse.costLists){
                localCostList.put(costListResponse.costList);
                if (costListResponse.linear != null){
                    for (ACATItemResponse acatItemResponse : costListResponse.linear){
                        localACATItem.put(acatItemResponse.acatItem);
                        localACATItemValue.put(acatItemResponse.estimatedAcatItemValue);
                        localACATItemValue.put(acatItemResponse.actualAcatItemValue);
                        localCashFlow.put(acatItemResponse.estimatedCashFlow);
                        localCashFlow.put(acatItemResponse.actualCashFlow);
                    }
                }

                if (costListResponse.grouped != null) {
                    for (GroupedListResponse groupedListResponse : costListResponse.grouped) {
                        if (groupedListResponse.groupedList != null) {
                            localGroupedList.put(groupedListResponse.groupedList);
                            for (ACATItemResponse acatItemResponse : groupedListResponse.items) {
                                localACATItem.put(acatItemResponse.acatItem);
                                localACATItemValue.put(acatItemResponse.estimatedAcatItemValue);
                                localACATItemValue.put(acatItemResponse.actualAcatItemValue);
                                localCashFlow.put(acatItemResponse.estimatedCashFlow);
                                localCashFlow.put(acatItemResponse.actualCashFlow);
                            }
                        }


                    }
                }
            }

        }
        return Completable.complete();
    }

    @Override
    public Completable saveACATApplicationSync(@NonNull ACATApplicationResponse response) {
        final ACATApplicationSync sync = new ACATApplicationSync();
        final List<String> cropACATIds = new ArrayList<>();
        final List<String> costSectionIds = new ArrayList<>();
        final List<String> revenueSectionIds = new ArrayList<>();
        final List<String> acatItemsIds = new ArrayList<>();
        final List<String> yieldConsumptionIds = new ArrayList<>();

        sync.acatAppId = response.acatApplication._id;
        sync.clientId = response.acatApplication.clientID;

        for (CropACATResponse cropACATResponse : response.cropACATs) {
            final CropACAT cropACAT = cropACATResponse.cropACAT;
            cropACATIds.add(cropACAT._id);

            final ACATCostSectionResponse costSectionResponse = cropACATResponse.costSection;
            for (ACATCostSection section : costSectionResponse.sections) {
                costSectionIds.add(section._id);
            }

            final ACATRevenueSectionResponse revenueSectionResponse = cropACATResponse.revenueSection;
            for (ACATRevenueSection revenueSection : revenueSectionResponse.sections) {
                revenueSectionIds.add(revenueSection._id);
            }

            for (CostListResponse costListResponse : costSectionResponse.costLists) {
                if (costListResponse.linear != null) {
                    for (ACATItemResponse acatItemResponse : costListResponse.linear) {
                        acatItemsIds.add(acatItemResponse.acatItem._id);
                    }
                }

                if (costListResponse.grouped != null) {
                    for (GroupedListResponse groupedListResponse : costListResponse.grouped) {
                        if (groupedListResponse.groupedList != null) {
                            for (ACATItemResponse acatItemResponse : groupedListResponse.items) {
                                acatItemsIds.add(acatItemResponse.acatItem._id);
                            }
                        }

                    }
                }

            }

            for (ACATItemResponse acatItemResponse : revenueSectionResponse.yields) {
                acatItemsIds.add(acatItemResponse.acatItem._id);
            }

            for (YieldConsumptionResponse yieldConsumption : revenueSectionResponse.yieldConsumptions) {
                yieldConsumptionIds.add(yieldConsumption.yieldConsumption._id);
            }

        }

        sync.cropACATIds = new ArrayList<>();
        sync.cropACATIds.clear();
        sync.cropACATIds.addAll(cropACATIds);

        sync.costSectionIds = new ArrayList<>();
        sync.costSectionIds.clear();
        sync.costSectionIds.addAll(costSectionIds);

        sync.revenueSectionIds = new ArrayList<>();
        sync.revenueSectionIds.clear();
        sync.revenueSectionIds.addAll(revenueSectionIds);

        sync.acatItemIds = new ArrayList<>();
        sync.acatItemIds.clear();
        sync.acatItemIds.addAll(acatItemsIds);

        sync.yieldConsumptionIds = new ArrayList<>();
        sync.yieldConsumptionIds.clear();
        sync.yieldConsumptionIds.addAll(yieldConsumptionIds);

        localSync.put(sync);

        return Completable.complete();
    }
    @Override
    public Completable initializeClientACAT(@NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs) {
        return remote.initializeACAT(client, loanProduct, cropACATs)
                .flatMapCompletable(response -> {
                    saveACATCostComponentsLocally(response);
                    saveACATRevenueComponentsLocally(response);
                    saveACATApplicationSync(response);
                    return Completable.complete();
                });
    }

    @Override
    public Completable updateACATApplication(@NonNull String acatApplciationId, @NonNull ACATApplicationRequest request) {
        return remote.update(acatApplciationId, request)
                .flatMapCompletable(data -> {
                    saveACATCostComponentsLocally(data);
                    saveACATRevenueComponentsLocally(data);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<ACATApplication> changeClientACATStatus(@NonNull ACATApplication acatApplication, @NonNull String status) {
        return remote.updateApiStatus(acatApplication, status)
                .flatMap(data -> {
                    saveACATCostComponentsLocally(data);
                    saveACATRevenueComponentsLocally(data);
                    return Observable.just(data.acatApplication);
                });
    }

    @Override
    public Observable<Boolean> submitACATApplication(@NonNull ACATApplication acatApplication) {
        return remote.updateApiStatus(acatApplication, ACATApplicationParser.STATUS_API_SUBMITTED)
                .flatMap(data -> {
                    saveACATCostComponentsLocally(data);
                    saveACATRevenueComponentsLocally(data);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> approveACATApplication(@NonNull ACATApplication acatApplication) {
        return remote.updateApiStatus(acatApplication, ACATApplicationParser.STATUS_API_AUTHORIZED)
                .flatMap(data -> {
                    saveACATCostComponentsLocally(data);
                    saveACATRevenueComponentsLocally(data);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> declineACATApplication(@NonNull ACATApplication acatApplication, @NonNull String remark) {
        return remote.updateApiStatus(acatApplication, ACATApplicationParser.STATUS_API_DECLINED_FOR_REVIEW)
                .flatMap(data -> {
                    saveACATCostComponentsLocally(data);
                    saveACATRevenueComponentsLocally(data);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<ACATApplication> fetchByACATId(@NonNull String clientACATId) {
        return localACATApplication.get(clientACATId)
                .flatMap(data ->
                    data.empty() ? fetchForceByACATId(clientACATId) : Observable.just(data.get())
                );
    }

    @Override
    public Observable<ACATApplication> fetchForceByACATId(@NonNull String clientACATId) {
        return remote.getACAT(clientACATId)
                .flatMap(response -> {
                    final List<CropACAT> cropACATS = new ArrayList<>();
                    for (CropACATResponse r : response.cropACATs) {
                        localCropACAT.put(r.cropACAT);
                    }
                    return localACATApplication.put(response.acatApplication);
                });
    }

    @Override
    public Observable<List<ACATApplication>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
                    final List<ACATApplication> applications = new ArrayList<>();
                    for (ACATApplicationResponse response : responses) {
                        saveACATCostComponentsLocally(response);
                        saveACATRevenueComponentsLocally(response);
                        saveACATApplicationSync(response);

                        applications.add(response.acatApplication);
                    }
                    return Observable.just(applications);
                });
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Observable<List<ACATApplication>> getAll() {
        return null;
    }

    @Override
    public Observable<List<ACATApplication>> fetchAll() {
        return null;
    }

    //@Override
//    public Observable<List<ACATApplication>> fetchForceAll() {
//        return remote.getAllByIds()
//                .flatMap(responses -> {
//                    final List<ACATApplication> applications = new ArrayList<>();
//                    for (ACATApplicationResponse pageResponse : responses) {
//                        final ACATApplication application = pageResponse.acatApplication;
//                        applications.add(application);
//                        localACATApplication.put(application);
//                        localCashFlow.put(pageResponse.estimatedNetCashFlow);
//                        localCashFlow.put(pageResponse.actualNetCashFlow);
//                        for (CropACATResponse cropACATResponse : pageResponse.cropACATs) {
//                            localCashFlow.put(cropACATResponse.estimatedNetCashFlow);
//                            localCashFlow.put(cropACATResponse.actualNetCashFlow);
//                            localCropACAT.put(cropACATResponse.cropACAT);
//                            final ACATCostSectionResponse acatCostSectionResponse = cropACATResponse.costSection;
//                            localCashFlow.putAll(acatCostSectionResponse.estimatedCashFlows);
//                            localCashFlow.putAll(acatCostSectionResponse.actualCashFlows);
//                            localCostSection.putAll(acatCostSectionResponse.sections);
//                            final ACATRevenueSectionResponse acatRevenueSectionResponse = cropACATResponse.revenueSection;
//                            // TODO: 6/6/2018 Parse GPS here.
//                            localRevenue.putAll(acatRevenueSectionResponse.sections);
//                            localCashFlow.putAll(acatRevenueSectionResponse.estimatedCashFlows);
//                            localCashFlow.putAll(acatRevenueSectionResponse.actualCashFlows);
//
//                            for (ACATItemResponse acatItemResponse : acatRevenueSectionResponse.yields) {
//                                localACATItem.put(acatItemResponse.acatItem);
//                                localCashFlow.put(acatItemResponse.estimatedCashFlow);
//                                localCashFlow.put(acatItemResponse.actualCashFlow);
//                                localACATItemValue.put(acatItemResponse.estimatedAcatItemValue);
//                                localACATItemValue.put(acatItemResponse.actualAcatItemValue);
//                            }
//
//                            for (YieldConsumptionResponse yieldResponse : acatRevenueSectionResponse.yieldConsumptions) {
//                                // TODO: 6/6/2018 save ParentSectionId;
//                                localYieldCons.put(yieldResponse.yieldConsumption);
//
//                                final MarketDetailsResponse estimatedMarketDetailsResponse = yieldResponse.estimatedMarketDetails;
//                                localMarketDetails.putAll(estimatedMarketDetailsResponse.marketDetails);
//                                final MarketDetailsResponse actualMarketDetailsResponse = yieldResponse.actualMarketDetails;
//                                localMarketDetails.putAll(actualMarketDetailsResponse.marketDetails);
//                            }
//                            for (CostListResponse cropListResponse : acatCostSectionResponse.costLists) {
//                                localCostList.put(cropListResponse.costList);
//
//                                for (ACATItemResponse acatItemResponse : cropListResponse.linear) {
//                                    localACATItem.put(acatItemResponse.acatItem);
//                                    localCashFlow.put(acatItemResponse.estimatedCashFlow);
//                                    localCashFlow.put(acatItemResponse.actualCashFlow);
//                                    localACATItemValue.put(acatItemResponse.estimatedAcatItemValue);
//                                    localACATItemValue.put(acatItemResponse.actualAcatItemValue);
//                                }
//                                for (GroupedListResponse groupedListResponse : cropListResponse.grouped) {
//                                    localGroupedList.put(groupedListResponse.groupedList);
//
//                                    for (ACATItemResponse acatItemResponse : groupedListResponse.items) {
//                                        localACATItem.put(acatItemResponse.acatItem);
//                                        localCashFlow.put(acatItemResponse.estimatedCashFlow);
//                                        localCashFlow.put(acatItemResponse.actualCashFlow);
//                                        localACATItemValue.put(acatItemResponse.estimatedAcatItemValue);
//                                        localACATItemValue.put(acatItemResponse.actualAcatItemValue);
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                    return Observable.just(applications);
//                });
//    }

    @Override
    public Observable<Data<ACATApplication>> get(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<Data<ACATApplication>> get(int position) {
        return null;
    }

    @Override
    public Observable<Data<ACATApplication>> first() {
        return null;
    }

    @Override
    public Observable<ACATApplication> fetch(@NonNull String id) {
        return localACATApplication.getACATByClient(id)
                .flatMap(acatApplicationData ->
                    acatApplicationData.empty() ? fetchForce(id) : Observable.just(acatApplicationData.get())
                );
    }

    @Override
    public Observable<ACATApplication> fetchForce(@NonNull String clientId) {
       return remote.fetchACATApplication(clientId)
               .flatMap(response -> {
                   for (CropACATResponse cropACATResponse : response.cropACATs) {
                       localCropACAT.put(cropACATResponse.cropACAT);
                   }
                   return localACATApplication.put(response.acatApplication);
               });
    }
}

package com.gebeya.mobile.bidir.ui.summary_yield;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class SummaryYieldPresenter implements SummaryYieldContract.Presenter {

    private SummaryYieldContract.View view;

    private final String clientId;
    private final String cropACATId;
    private final SummaryMenuItem.Label label;

    private CropACAT cropACAT;
    private ACATRevenueSection revenueSection;
    private ACATItem estimatedACATItem;
    private ACATItem actualACATItem;
    private ACATItemValue estimatedAcatItemValue;
    private ACATItemValue actualAcatItemValue;
    @Nullable private YieldConsumption yieldConsumption;

    private final List<ACATRevenueSection> yieldSections;

    @Inject CropACATLocalSource cropACATLocal;
    @Inject ACATRevenueSectionLocalSource revenueSectionLocal;
    @Inject YieldConsumptionLocalSource yieldConsumptionLocal;
    @Inject ACATItemLocalSource acatItemlocal;
    @Inject ACATItemValueLocalSource acatItemValueLocal;

    @Inject SchedulersProvider schedulers;
    public SummaryYieldPresenter(@NonNull SummaryMenuItem.Label label, @NonNull String clientId, @NonNull String cropACATId) {
        this.clientId = clientId;
        this.cropACATId = cropACATId;
        this.label = label;

        yieldSections = new ArrayList<>();

        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }
    @SuppressLint("CheckResult")
    @Override
    public void start() {
        cropACATLocal.get(cropACATId)
                .flatMap(data -> {
                    cropACAT = data.get();
                    return revenueSectionLocal.get(cropACAT.revenueSectionId);
                })
                .flatMap(data -> {
                    revenueSection = data.get();
                    return revenueSectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    yieldSections.clear();
                    yieldSections.addAll(list);

                    if (yieldSections.isEmpty()) return;

                    loadData(getYieldPosition(label));
                    view.setTitle(label);
                }, Throwable::printStackTrace);
    }

    private void loadData(int yieldPosition) {
        final ACATRevenueSection section = yieldSections.get(yieldPosition);

        if (yieldPosition == 1 || yieldPosition == 2) {
            view.toggleOwnCons(false);
            view.toggleSeedRes(false);
            view.toggleForMarket(false);
        } else {
            view.toggleOwnCons(true);
            view.toggleSeedRes(true);
            view.toggleForMarket(true);
            loadYieldConsumption(section.yieldConsumptionID, true);
        }
        loadYield(section.yieldID, true);



    }

    @SuppressLint("CheckResult")
    private void loadYieldConsumption(String id, boolean estimated) {
        yieldConsumptionLocal.get(id == null ? "-" : id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (view == null) return;

                    if (data.empty()) {
                        yieldConsumption = null;
                    } else {
                        yieldConsumption = data.get();

                        view.setOwnCons(yieldConsumption.estimatedOwnCons, yieldConsumption.actualOwnCons);
                        view.setSeedRes(yieldConsumption.estimatedSeedReserve, yieldConsumption.actualSeedReserve);
                        view.setForMarket(yieldConsumption.estimatedForMarket, yieldConsumption.actualForMarket);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void loadYield(String yieldID, boolean estimated) {
        acatItemlocal.get(yieldID)
                .flatMap(data -> {
                    estimatedACATItem = data.get();
                    return acatItemValueLocal.getByType(estimatedACATItem._id, ACATItemValueHelper.ESTIMATED_VALUE_TYPE);
                })
                .flatMap(data -> {
                    estimatedAcatItemValue = data.get();
                    return acatItemValueLocal.getByType(estimatedACATItem._id, ACATItemValueHelper.ACTUAL_VALUE_TYPE);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (view == null) return;
                    actualAcatItemValue = data.get();

                    view.setUnit(estimatedACATItem.unit); // TODO: the same for both estimated and actual unit.
                    view.setUnitPrice(estimatedAcatItemValue.unitPrice, actualAcatItemValue.unitPrice);
                    view.setTotalPrice(estimatedAcatItemValue.totalPrice, actualAcatItemValue.totalPrice);
                });
    }

    @Override
    public void attachView(SummaryYieldContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public SummaryYieldContract.View getView() {
        return view;
    }

    private int getYieldPosition(@NonNull SummaryMenuItem.Label label) {
        switch (label) {
            case PROBABLE_YIELD:
                return 0;
            case MAXIMUM_YIELD:
                return 1;
            case MINIMUM_YIELD:
                return 2;
        }
        return 3;
    }

    @Override
    public void onMonitorButtonClicked() {
        view.openYieldFragment(clientId, cropACATId);
    }
}

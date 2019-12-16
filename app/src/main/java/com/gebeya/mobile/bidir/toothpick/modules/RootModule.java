package com.gebeya.mobile.bidir.toothpick.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatapplication.remote.BaseACATApplicationParser;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionParser;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.BaseACATCostSectionParser;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.BaseACATCostSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormParser;
import com.gebeya.mobile.bidir.data.acatform.remote.BaseACATFormParser;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemParser;
import com.gebeya.mobile.bidir.data.acatitem.remote.BaseACATItemParser;
import com.gebeya.mobile.bidir.data.acatitemvalue.remote.ACATItemValueParser;
import com.gebeya.mobile.bidir.data.acatitemvalue.remote.BaseACATItemValueParser;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.ACATRevenueSectionParser;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.BaseACATRevenueSectionParser;
import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.base.remote.QuestionAnswerHelper;
import com.gebeya.mobile.bidir.data.base.remote.backend.BaseConnectionProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.BaseEndpointProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.ConnectionProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.EndpointProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACADataDownloadState;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATSyncState;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.client.ClientSyncState;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.loanapplication.ComplexLoanApplicationSyncState;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening.ComplexScreeningSyncState;
import com.gebeya.mobile.bidir.data.cashflow.remote.BaseCashFlowParser;
import com.gebeya.mobile.bidir.data.cashflow.remote.CashFlowParser;
import com.gebeya.mobile.bidir.data.client.remote.BaseClientParser;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.BaseComplexLoanApplicationParser;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationParser;
import com.gebeya.mobile.bidir.data.complexquestion.remote.BaseComplexQuestionParser;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.complexscreening.remote.BaseComplexScreeningParser;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningParser;
import com.gebeya.mobile.bidir.data.costlist.remote.BaseCostListParser;
import com.gebeya.mobile.bidir.data.costlist.remote.CostListParser;
import com.gebeya.mobile.bidir.data.cropacat.remote.BaseCropACATParser;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATParser;
import com.gebeya.mobile.bidir.data.form.remote.BaseComplexFormParser;
import com.gebeya.mobile.bidir.data.form.remote.ComplexFormParser;
import com.gebeya.mobile.bidir.data.gpslocation.remote.BaseGPSLocationResponseParser;
import com.gebeya.mobile.bidir.data.gpslocation.remote.GPSLocationResponseParser;
import com.gebeya.mobile.bidir.data.groupedlist.remote.BaseGroupedListParser;
import com.gebeya.mobile.bidir.data.groupedlist.remote.GroupedListParser;
import com.gebeya.mobile.bidir.data.loanapplication.BaseLoanApplicationStatusHelper;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplicationStatusHelper;
import com.gebeya.mobile.bidir.data.loanproduct.remote.BaseLoanProductParser;
import com.gebeya.mobile.bidir.data.loanproduct.remote.LoanProductParser;
import com.gebeya.mobile.bidir.data.loanproductitem.remote.BaseLoanProductItemParser;
import com.gebeya.mobile.bidir.data.loanproductitem.remote.LoanProductItemParser;
import com.gebeya.mobile.bidir.data.marketdetails.remote.BaseMarketDetailsParser;
import com.gebeya.mobile.bidir.data.marketdetails.remote.MarketDetailsParser;
import com.gebeya.mobile.bidir.data.pagination.local.BasePageManipulator;
import com.gebeya.mobile.bidir.data.pagination.local.PageManipulator;
import com.gebeya.mobile.bidir.data.pagination.remote.BasePageParser;
import com.gebeya.mobile.bidir.data.pagination.remote.PageParser;
import com.gebeya.mobile.bidir.data.permission.BasePermissionHelper;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.prerequisite.remote.BasePrerequisiteParser;
import com.gebeya.mobile.bidir.data.prerequisite.remote.PrerequisiteParser;
import com.gebeya.mobile.bidir.data.screening.BaseScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.screening.ScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.section.remote.BaseSectionParser;
import com.gebeya.mobile.bidir.data.section.remote.SectionParser;
import com.gebeya.mobile.bidir.data.task.BaseTaskHelper;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.BaseYieldConsumptionParser;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.YieldConsumptionParser;
import com.gebeya.mobile.bidir.impl.rx.BaseSchedulersProvider;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.error.BaseErrorHandler;
import com.gebeya.mobile.bidir.impl.util.error.ErrorHandler;
import com.gebeya.mobile.bidir.impl.util.location.android.BaseDefaultGpsManager;
import com.gebeya.mobile.bidir.impl.util.location.android.DefaultGpsManager;
import com.gebeya.mobile.bidir.impl.util.location.play_services.BasePlayServicesApi;
import com.gebeya.mobile.bidir.impl.util.location.play_services.BasePlayServicesGpsManager;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesApi;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesGpsManager;
import com.gebeya.mobile.bidir.impl.util.location.preference.AppPreference;
import com.gebeya.mobile.bidir.impl.util.location.preference.PreferenceHelper;
import com.gebeya.mobile.bidir.impl.util.network.BaseNetworkWatcher;
import com.gebeya.mobile.bidir.impl.util.network.NetworkWatcher;
import com.google.android.gms.common.GoogleApiAvailability;

import io.objectbox.BoxStore;
import toothpick.config.Module;

/**
 * Module class for the {@link android.app.Application}, which provides bindings for common
 * dependencies that are needed throughout the entire application lifecycle.
 */
public class RootModule extends Module {

    public RootModule(@NonNull Context context, @NonNull BoxStore store) {
        // Initialize ObjectBox and its BoxStore as a singleton
        bind(BoxStore.class).toInstance(store);
        bind(PlayServicesApi.class).toInstance(new BasePlayServicesApi(context, GoogleApiAvailability.getInstance()));
        bind(DefaultGpsManager.class).toInstance(new BaseDefaultGpsManager(context));
        bind(PlayServicesGpsManager.class).toInstance(new BasePlayServicesGpsManager(context));

        // Initialize the NetworkWatcher's implementation as a singleton
        bind(NetworkWatcher.class).toInstance(new BaseNetworkWatcher(context));
        bind(ErrorHandler.class).toInstance(new BaseErrorHandler());
        bind(EndpointProvider.class).toInstance(new BaseEndpointProvider());
        bind(ConnectionProvider.class).toInstance(new BaseConnectionProvider());
        bind(SchedulersProvider.class).toInstance(new BaseSchedulersProvider());

        // Initialize the helpers
        bind(PermissionHelper.class).toInstance(new BasePermissionHelper());
        bind(TaskHelper.class).toInstance(new BaseTaskHelper());
        bind(ScreeningStatusHelper.class).toInstance(new BaseScreeningStatusHelper());
        bind(LoanApplicationStatusHelper.class).toInstance(new BaseLoanApplicationStatusHelper());
        bind(QAHelper.class).toInstance(new QuestionAnswerHelper());
        bind(ClientParser.class).toInstance(new BaseClientParser());

        /// Initialize the parsers
        bind(PrerequisiteParser.class).toInstance(new BasePrerequisiteParser());
        bind(ComplexScreeningParser.class).toInstance(new BaseComplexScreeningParser());
        bind(SectionParser.class).toInstance(new BaseSectionParser());
        bind(ComplexQuestionParser.class).toInstance(new BaseComplexQuestionParser());
        bind(ComplexLoanApplicationParser.class).toInstance(new BaseComplexLoanApplicationParser());
        bind(LoanProductParser.class).toInstance(new BaseLoanProductParser());
        bind(LoanProductItemParser.class).toInstance(new BaseLoanProductItemParser());
        bind(ComplexFormParser.class).toInstance(new BaseComplexFormParser());

        // ACAT Parsers
        bind(CashFlowParser.class).toInstance(new BaseCashFlowParser());
        bind(ACATCostSectionParser.class).toInstance(new BaseACATCostSectionParser());
        bind(CostListParser.class).toInstance(new BaseCostListParser());
        bind(ACATItemParser.class).toInstance(new BaseACATItemParser());
        bind(ACATItemValueParser.class).toInstance(new BaseACATItemValueParser());
        bind(ACATFormParser.class).toInstance(new BaseACATFormParser());
        bind(GroupedListParser.class).toInstance(new BaseGroupedListParser());
        bind(GPSLocationResponseParser.class).toInstance(new BaseGPSLocationResponseParser());

        bind(MarketDetailsParser.class).toInstance(new BaseMarketDetailsParser());
        bind(CropACATParser.class).toInstance(new BaseCropACATParser());
        bind(ACATApplicationParser.class).toInstance(new BaseACATApplicationParser());
        bind(ACATCostSectionResponseParser.class).toInstance(new BaseACATCostSectionResponseParser());

        bind(ACATRevenueSectionParser.class).toInstance(new BaseACATRevenueSectionParser());
        bind(YieldConsumptionParser.class).toInstance(new BaseYieldConsumptionParser());

        // Sync service states
        bind(ClientSyncState.class).toInstance(new ClientSyncState());
        bind(ACATSyncState.class).toInstance(new ACATSyncState());
        bind(ComplexScreeningSyncState.class).toInstance(new ComplexScreeningSyncState());
        bind(ComplexLoanApplicationSyncState.class).toInstance(new ComplexLoanApplicationSyncState());
        bind(ACADataDownloadState.class).toInstance(new ACADataDownloadState());

        // Shared Preference
        bind(PreferenceHelper.class).toInstance(new AppPreference(context));

        // Page manipulators
        bind(PageManipulator.class).toInstance(new BasePageManipulator());
        bind(PageParser.class).toInstance(new BasePageParser());
    }
}
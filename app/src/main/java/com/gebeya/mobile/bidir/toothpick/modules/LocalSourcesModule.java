package com.gebeya.mobile.bidir.toothpick.modules;

import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocal;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocal;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocal;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatform.local.ACATFormFormLocal;
import com.gebeya.mobile.bidir.data.acatform.local.ACATFormLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocal;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.remote.BaseACATItemResponseParser;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocal;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocal;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.BaseACATRevenueSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatsection.local.ACATSectionLocal;
import com.gebeya.mobile.bidir.data.acatsection.local.ACATSectionLocalSource;
import com.gebeya.mobile.bidir.data.answer.local.AnswerLocal;
import com.gebeya.mobile.bidir.data.answer.local.AnswerLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocal;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocal;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationLocal;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocal;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocal;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.costlist.local.CostListLocal;
import com.gebeya.mobile.bidir.data.costlist.local.CostListLocalSource;
import com.gebeya.mobile.bidir.data.costlist.remote.BaseCostListResponseParser;
import com.gebeya.mobile.bidir.data.crop.local.CropLocal;
import com.gebeya.mobile.bidir.data.crop.local.CropLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocal;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.BaseCropACATResponseParser;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATResponseParser;
import com.gebeya.mobile.bidir.data.form.local.ComplexFormLocal;
import com.gebeya.mobile.bidir.data.form.local.ComplexFormLocalSource;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocal;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.data.groupedacat.local.GroupedACATLocal;
import com.gebeya.mobile.bidir.data.groupedacat.local.GroupedACATLocalSource;
import com.gebeya.mobile.bidir.data.groupedacat.remote.BaseGroupedACATParser;
import com.gebeya.mobile.bidir.data.groupedacat.remote.GroupedACATParser;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.local.GroupComplexLoanLocal;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.local.GroupedComplexLoanLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.BaseGroupComplexLoanParser;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanParser;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.local.GroupedComplexScreeningLocal;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.local.GroupedComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.BaseGroupedComplexScreeningParser;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningParser;
import com.gebeya.mobile.bidir.data.groupedlist.local.GroupedListLocal;
import com.gebeya.mobile.bidir.data.groupedlist.local.GroupedListLocalSource;
import com.gebeya.mobile.bidir.data.groupedlist.remote.BaseGroupedListResponseParser;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocal;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.groups.remote.BaseGroupParser;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.remote.BaseLoanProposalParser;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalParser;
import com.gebeya.mobile.bidir.data.loanapplication.local.LoanApplicationLocal;
import com.gebeya.mobile.bidir.data.loanapplication.local.LoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.loanproduct.local.LoanProductLocal;
import com.gebeya.mobile.bidir.data.loanproduct.local.LoanProductLocalSource;
import com.gebeya.mobile.bidir.data.loanproductitem.local.LoanProductItemLocal;
import com.gebeya.mobile.bidir.data.loanproductitem.local.LoanProductItemLocalSource;
import com.gebeya.mobile.bidir.data.marketdetails.local.MarketDetailsLocal;
import com.gebeya.mobile.bidir.data.marketdetails.local.MarketDetailsLocalSource;
import com.gebeya.mobile.bidir.data.marketdetails.remote.BaseMarketDetailsResponseParser;
import com.gebeya.mobile.bidir.data.pagination.local.PageLocal;
import com.gebeya.mobile.bidir.data.pagination.local.PageLocalSource;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocal;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocal;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.screening.local.ScreeningLocal;
import com.gebeya.mobile.bidir.data.screening.local.ScreeningLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocal;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.data.task.local.TaskLocal;
import com.gebeya.mobile.bidir.data.task.local.TaskLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocal;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocal;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.BaseYieldConsumptionResponseParser;

import toothpick.config.Module;

/**
 * Module for representing local data sources, such as the
 * {@link UserLocalSource} one, all as singletons.
 */
public class LocalSourcesModule extends Module {

    public LocalSourcesModule() {
        bind(UserLocalSource.class).toInstance(new UserLocal());
        bind(TaskLocalSource.class).toInstance(new TaskLocal());
        bind(ScreeningLocalSource.class).toInstance(new ScreeningLocal());
        bind(LoanApplicationLocalSource.class).toInstance(new LoanApplicationLocal());

        bind(ComplexQuestionLocalSource.class).toInstance(new ComplexQuestionLocal());
        bind(ComplexScreeningLocalSource.class).toInstance(new ComplexScreeningLocal());
        bind(ComplexLoanApplicationLocalSource.class).toInstance(new ComplexLoanApplicationLocal());
        bind(ClientLocalSource.class).toInstance(new ClientLocal());
        bind(PermissionLocalSource.class).toInstance(new PermissionLocal());
        bind(AnswerLocalSource.class).toInstance(new AnswerLocal());
        bind(SectionLocalSource.class).toInstance(new SectionLocal());
        bind(PrerequisiteLocalSource.class).toInstance(new PrerequisiteLocal());
        bind(ComplexFormLocalSource.class).toInstance(new ComplexFormLocal());
        bind(PageLocalSource.class).toInstance(new PageLocal());

        // ACAT resources
        bind(ACATFormLocalSource.class).toInstance(new ACATFormFormLocal());
        bind(CropLocalSource.class).toInstance(new CropLocal());
        bind(LoanProductLocalSource.class).toInstance(new LoanProductLocal());
        bind(LoanProductItemLocalSource.class).toInstance(new LoanProductItemLocal());
        bind(ACATCostSectionLocalSource.class).toInstance(new ACATCostSectionLocal());
        bind(ACATItemLocalSource.class).toInstance(new ACATItemLocal());
        bind(ACATItemValueLocalSource.class).toInstance(new ACATItemValueLocal());
        bind(ACATSectionLocalSource.class).toInstance(new ACATSectionLocal());
        bind(CashFlowLocalSource.class).toInstance(new CashFlowLocal());
        bind(CostListLocalSource.class).toInstance(new CostListLocal());
        bind(GroupedListLocalSource.class).toInstance(new GroupedListLocal());

        bind(BaseCostListResponseParser.class).toInstance(new BaseCostListResponseParser());

        bind(BaseGroupedListResponseParser.class).toInstance(new BaseGroupedListResponseParser());
        bind(BaseACATItemResponseParser.class).toInstance(new BaseACATItemResponseParser());

        bind(CropACATLocalSource.class).toInstance(new CropACATLocal());
        bind(MarketDetailsLocalSource.class).toInstance(new MarketDetailsLocal());
        bind(YieldConsumptionLocalSource.class).toInstance(new YieldConsumptionLocal());
        bind(ACATApplicationLocalSource.class).toInstance(new ACATApplicationLocal());
        bind(ACATRevenueSectionLocalSource.class).toInstance(new ACATRevenueSectionLocal());
        bind(BaseYieldConsumptionResponseParser.class).toInstance(new BaseYieldConsumptionResponseParser());
        bind(BaseACATRevenueSectionResponseParser.class).toInstance(new BaseACATRevenueSectionResponseParser());
        bind(CropACATResponseParser.class).toInstance(new BaseCropACATResponseParser());
        bind(BaseMarketDetailsResponseParser.class).toInstance(new BaseMarketDetailsResponseParser());
        bind(GPSLocationLocalSource.class).toInstance(new GPSLocationLocal());

        bind(LoanProposalLocalSource.class).toInstance(new LoanProposalLocal());
        bind(LoanProposalParser.class).toInstance(new BaseLoanProposalParser());
        bind(ACATApplicationSyncLocalSource.class).toInstance(new ACATApplicationSyncLocal());
        bind(GroupLocalSource.class).toInstance(new GroupLocal());

        bind(GroupParser.class).toInstance(new BaseGroupParser());
        bind(GroupedComplexScreeningLocalSource.class).toInstance(new GroupedComplexScreeningLocal());
        bind(GroupedComplexScreeningParser.class).toInstance(new BaseGroupedComplexScreeningParser());

        bind(GroupedComplexLoanLocalSource.class).toInstance(new GroupComplexLoanLocal());
        bind(GroupedComplexLoanParser.class).toInstance(new BaseGroupComplexLoanParser());

        bind(GroupedACATLocalSource.class).toInstance(new GroupedACATLocal());
        bind(GroupedACATParser.class).toInstance(new BaseGroupedACATParser());
    }
}
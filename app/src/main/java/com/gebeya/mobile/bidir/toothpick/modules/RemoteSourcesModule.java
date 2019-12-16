package com.gebeya.mobile.bidir.toothpick.modules;

import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemote;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepository;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionRemote;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionRemoteSource;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.ACATCostSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatcostsection.remote.BaseACATCostSectionResponseParser;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormRemote;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormRemoteSource;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemRemote;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemRemoteSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.ACATRevenueSectionRemote;
import com.gebeya.mobile.bidir.data.acatrevenuesection.remote.ACATRevenueSectionRemoteSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientRemote;
import com.gebeya.mobile.bidir.data.client.remote.ClientRemoteSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemote;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningRemote;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATRemote;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATRemoteSource;
import com.gebeya.mobile.bidir.data.form.remote.ComplexFormRemote;
import com.gebeya.mobile.bidir.data.form.remote.ComplexFormRemoteSource;
import com.gebeya.mobile.bidir.data.groupedacat.remote.GroupedACATRemote;
import com.gebeya.mobile.bidir.data.groupedacat.remote.GroupedACATRemoteSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanRemote;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanRemoteSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningRemote;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.groups.remote.GroupRemote;
import com.gebeya.mobile.bidir.data.groups.remote.GroupRemoteSource;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalRemote;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalRemoteSource;
import com.gebeya.mobile.bidir.data.loanapplication.remote.LoanApplicationRemote;
import com.gebeya.mobile.bidir.data.loanapplication.remote.LoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.loanproduct.remote.LoanProductRemote;
import com.gebeya.mobile.bidir.data.loanproduct.remote.LoanProductRemoteSource;
import com.gebeya.mobile.bidir.data.screening.remote.ScreeningRemote;
import com.gebeya.mobile.bidir.data.screening.remote.ScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.task.remote.TaskRemote;
import com.gebeya.mobile.bidir.data.task.remote.TaskRemoteSource;
import com.gebeya.mobile.bidir.data.user.remote.UserRemote;
import com.gebeya.mobile.bidir.data.user.remote.UserRemoteSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.YieldConsumptionRemote;
import com.gebeya.mobile.bidir.data.yieldconsumption.remote.YieldConsumptionRemoteSource;

import toothpick.config.Module;

/**
 * Module for representing remote sources of data, such as the
 * {@link UserRemoteSource} one, as singletons each.
 */
public class RemoteSourcesModule extends Module {
    public RemoteSourcesModule() {
        bind(UserRemoteSource.class).toInstance(new UserRemote());
        bind(TaskRemoteSource.class).toInstance(new TaskRemote());
        bind(ScreeningRemoteSource.class).toInstance(new ScreeningRemote());
        bind(LoanApplicationRemoteSource.class).toInstance(new LoanApplicationRemote());
        bind(ClientRemoteSource.class).toInstance(new ClientRemote());
        bind(ComplexScreeningRemoteSource.class).toInstance(new ComplexScreeningRemote());
        bind(ComplexLoanApplicationRemote.class).toInstance(new ComplexLoanApplicationRemote());
        bind(LoanProductRemoteSource.class).toInstance(new LoanProductRemote());
        bind(ACATCostSectionResponseParser.class).toInstance(new BaseACATCostSectionResponseParser());
        bind(ACATApplicationRemoteSource.class).toInstance(new ACATApplicationRemote());
        bind(ACATApplicationRemote.class).toInstance(new ACATApplicationRemote());
        bind(ACATApplicationRepository.class).toInstance(new ACATApplicationRepository());
        bind(CropACATRemoteSource.class).toInstance(new CropACATRemote());
        bind(ComplexFormRemoteSource.class).toInstance(new ComplexFormRemote());
        bind(ACATFormRemoteSource.class).toInstance(new ACATFormRemote());

        bind(ACATItemRemoteSource.class).toInstance (new ACATItemRemote());
        bind(ACATCostSectionRemoteSource.class).toInstance (new ACATCostSectionRemote());

        bind(LoanProposalRemoteSource.class).toInstance(new LoanProposalRemote());

        bind(ACATRevenueSectionRemoteSource.class).toInstance(new ACATRevenueSectionRemote());
        bind(YieldConsumptionRemoteSource.class).toInstance(new YieldConsumptionRemote());
        bind(GroupRemoteSource.class).toInstance(new GroupRemote());
        bind(GroupedComplexScreeningRemoteSource.class).toInstance(new GroupedComplexScreeningRemote());
        bind(GroupedComplexLoanRemoteSource.class).toInstance(new GroupedComplexLoanRemote());
        bind(GroupedACATRemoteSource.class).toInstance(new GroupedACATRemote());
    }
}
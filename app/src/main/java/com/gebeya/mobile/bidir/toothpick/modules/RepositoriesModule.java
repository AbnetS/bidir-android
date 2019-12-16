package com.gebeya.mobile.bidir.toothpick.modules;

import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemote;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepository;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.acatform.ACATFormRepo;
import com.gebeya.mobile.bidir.data.acatform.ACATFormRepoSource;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormRemote;
import com.gebeya.mobile.bidir.data.acatitem.repo.ACATItemRepository;
import com.gebeya.mobile.bidir.data.acatitem.repo.ACATItemRepositorySource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.repo.ACATRevenueRepo;
import com.gebeya.mobile.bidir.data.acatrevenuesection.repo.ACATRevenueRepoSource;
import com.gebeya.mobile.bidir.data.client.ClientRepo;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepository;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepository;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepo;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepoSource;
import com.gebeya.mobile.bidir.data.form.repo.ComplexFormRepository;
import com.gebeya.mobile.bidir.data.form.repo.ComplexFormRepositorySource;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepo;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.repo.GroupedComplexLoanRepo;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.repo.GroupedComplexLoanRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepo;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepoSource;
import com.gebeya.mobile.bidir.data.groups.GroupRepo;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepo;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplicationRepo;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplicationRepoSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProductRepo;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProductRepoSource;
import com.gebeya.mobile.bidir.data.screening.ScreeningRepo;
import com.gebeya.mobile.bidir.data.screening.ScreeningRepoSource;
import com.gebeya.mobile.bidir.data.task.TaskRepo;
import com.gebeya.mobile.bidir.data.task.TaskRepoSource;
import com.gebeya.mobile.bidir.data.user.UserRepo;
import com.gebeya.mobile.bidir.data.user.UserRepoSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.repo.YieldConsumptionRepo;
import com.gebeya.mobile.bidir.data.yieldconsumption.repo.YieldConsumptionRepoSource;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.UpdaterUtility;

import toothpick.config.Module;

/**
 * Module for repositories, a step higher than the data sources.
 */
public class RepositoriesModule extends Module {
    public RepositoriesModule() {
        bind(UserRepoSource.class).toInstance(new UserRepo());
        bind(TaskRepoSource.class).toInstance(new TaskRepo());
        bind(ScreeningRepoSource.class).toInstance(new ScreeningRepo());
        bind(LoanApplicationRepoSource.class).toInstance(new LoanApplicationRepo());
        bind(ClientRepoSource.class).toInstance(new ClientRepo());
        bind(ComplexScreeningRepositorySource.class).toInstance(new ComplexScreeningRepository());
        bind(ComplexLoanApplicationRepositorySource.class).toInstance(new ComplexLoanApplicationRepository());
        bind(LoanProductRepoSource.class).toInstance(new LoanProductRepo());
        bind(ACATFormRepoSource.class).toInstance(new ACATFormRepo());
        bind(ACATFormRemote.class).toInstance(new ACATFormRemote());
        bind(ACATApplicationRepositorySource.class).toInstance(new ACATApplicationRepository());
        bind(CropACATRepoSource.class).toInstance(new CropACATRepo());
        bind(ComplexFormRepositorySource.class).toInstance(new ComplexFormRepository());
        bind(ACATItemRepositorySource.class).toInstance(new ACATItemRepository());
        bind(ACATApplicationRemoteSource.class).toInstance(new ACATApplicationRemote());

        bind(UpdaterUtility.class).toInstance(new UpdaterUtility());
        bind(LoanProposalRepoSource.class).toInstance(new LoanProposalRepo());

        bind(ACATRevenueRepoSource.class).toInstance(new ACATRevenueRepo());
        bind(YieldConsumptionRepoSource.class).toInstance(new YieldConsumptionRepo());
        bind(GroupRepoSource.class).toInstance(new GroupRepo());
        bind(GroupedComplexScreeningRepoSource.class).toInstance(new GroupedComplexScreeningRepo());
        bind(GroupedComplexLoanRepoSource.class).toInstance(new GroupedComplexLoanRepo());
        bind(GroupedACATRepoSource.class).toInstance(new GroupedACATRepo());
    }
}
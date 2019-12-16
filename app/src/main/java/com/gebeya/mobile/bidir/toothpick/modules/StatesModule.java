package com.gebeya.mobile.bidir.toothpick.modules;

import com.gebeya.mobile.bidir.ui.form.loanapplication.LoanApplicationLoadState;
import com.gebeya.mobile.bidir.ui.form.loanapplication.LoanApplicationUpdateState;
import com.gebeya.mobile.bidir.ui.form.questions.controller.BaseQuestionController;
import com.gebeya.mobile.bidir.ui.form.questions.controller.QuestionController;
import com.gebeya.mobile.bidir.ui.form.screening.ScreeningLoadState;
import com.gebeya.mobile.bidir.ui.form.screening.ScreeningUpdateState;
import com.gebeya.mobile.bidir.ui.home.main.clientlists.ClientListState;
import com.gebeya.mobile.bidir.ui.home.main.clients.ClientState;
import com.gebeya.mobile.bidir.ui.home.main.groupedacat.GroupedACATLoadState;
import com.gebeya.mobile.bidir.ui.home.main.groupedacat.GroupedACATUpdateState;
import com.gebeya.mobile.bidir.ui.home.main.groupedclient.GroupedClientLoadState;
import com.gebeya.mobile.bidir.ui.home.main.groupedclient.GroupedClientUpdateState;
import com.gebeya.mobile.bidir.ui.home.main.groupedloans.GroupedLoansLoadState;
import com.gebeya.mobile.bidir.ui.home.main.groupedloans.GroupedLoansUpdateState;
import com.gebeya.mobile.bidir.ui.home.main.groupedscreenings.GroupedScreeningsLoadState;
import com.gebeya.mobile.bidir.ui.home.main.groupedscreenings.GroupedScreeningsUpdateState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatapplication.ACATPreliminaryInfoLoadState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatapplication.ACATPreliminaryInfoUpdateState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationLoadState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationUpdateState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemLoadState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation.ACATItemCostEstimationLoadState;
import com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal.ACATLoanProposalLoadState;
import com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat.PreliminaryInformationState;
import com.gebeya.mobile.bidir.ui.register.RegisterPicturesData;

import toothpick.config.Module;

/**
 * Module for states.
 */
public class StatesModule extends Module {
    public StatesModule() {
        bind(ScreeningLoadState.class).toInstance(new ScreeningLoadState());
        bind(ScreeningUpdateState.class).toInstance(new ScreeningUpdateState());
        bind(LoanApplicationLoadState.class).toInstance(new LoanApplicationLoadState());
        bind(LoanApplicationUpdateState.class).toInstance(new LoanApplicationUpdateState());
        bind(QuestionController.class).toInstance(new BaseQuestionController());
        bind(RegisterPicturesData.class).toInstance(new RegisterPicturesData());
        bind(PreliminaryInformationState.class).toInstance(new PreliminaryInformationState());
        bind(ACATPreliminaryInfoLoadState.class).toInstance(new ACATPreliminaryInfoLoadState());
        bind(ACATPreliminaryInfoUpdateState.class).toInstance(new ACATPreliminaryInfoUpdateState());
        bind(ACATCostEstimationLoadState.class).toInstance(new ACATCostEstimationLoadState());
        bind(ACATCostEstimationUpdateState.class).toInstance(new ACATCostEstimationUpdateState());
        bind(ACATItemCostEstimationLoadState.class).toInstance(new ACATItemCostEstimationLoadState());
        bind(ACATLoanProposalLoadState.class).toInstance(new ACATLoanProposalLoadState());
        bind(ACATCropItemLoadState.class).toInstance(new ACATCropItemLoadState());
        bind(ClientState.class).toInstance(new ClientState());
        bind(GroupedClientLoadState.class).toInstance(new GroupedClientLoadState());
        bind(GroupedClientUpdateState.class).toInstance(new GroupedClientUpdateState());
        bind(ClientListState.class).toInstance(new ClientListState());
        bind(GroupedScreeningsUpdateState.class).toInstance(new GroupedScreeningsUpdateState());
        bind(GroupedScreeningsLoadState.class).toInstance(new GroupedScreeningsLoadState());
        bind(GroupedLoansLoadState.class).toInstance(new GroupedLoansLoadState());
        bind(GroupedLoansUpdateState.class).toInstance(new GroupedLoansUpdateState());
        bind(GroupedACATUpdateState.class).toInstance(new GroupedACATUpdateState());
        bind(GroupedACATLoadState.class).toInstance(new GroupedACATLoadState());
    }
}
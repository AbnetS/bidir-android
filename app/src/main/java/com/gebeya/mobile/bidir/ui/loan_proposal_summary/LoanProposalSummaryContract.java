package com.gebeya.mobile.bidir.ui.loan_proposal_summary;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;

/**
 * Created by Samuel K. on 8/25/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface LoanProposalSummaryContract {

    interface View extends BaseView<Presenter> {
        void setTotalRevenue(double value);
        void setTotalCost(double value);
        void setNetIncome(double value);
        void setLoanRequested(double value);
        void setLoanProposed(double value);
        void setLoanApproved(double value);
        void setTotalDeductible(double value);
        void setTotalLoanCost(double value);
        void setCashAtHand(double value);
        void setRepayable(double value);
    }

    interface Presenter extends BasePresenter<View> {

    }
}

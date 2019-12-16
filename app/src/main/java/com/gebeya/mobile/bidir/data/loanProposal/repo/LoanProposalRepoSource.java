package com.gebeya.mobile.bidir.data.loanProposal.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 8/17/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface LoanProposalRepoSource extends
        ReadableSource<LoanProposal>,
        FetchOne<LoanProposal>,
        FetchMany<LoanProposal>,
        ReadSize{

    Completable createLoanProposal(@NonNull ACATApplication acatApplication, @NonNull LoanProduct loanProduct);

    Observable<LoanProposal> fetchByClient(@NonNull String clientId);

    Observable<LoanProposal> fetchForceByClient(@NonNull String clientId);

    Completable saveLoanProposal(@NonNull LoanProposal loanProposal, @NonNull CashFlow cashFlow, @NonNull ACATApplication acatApplication);

    Observable<LoanProposal> updateLoanProposal(@NonNull LoanProposal loanProposal);

    Observable<Boolean> changeApiStatus(@NonNull LoanProposal loanProposal, @NonNull String status);

    Observable<Boolean> submitLoanProposal(@NonNull LoanProposal loanProposal);

    Observable<Boolean> approveLoanProposal(@NonNull LoanProposal loanProposal);

    Observable<Boolean> declineForReview(@NonNull LoanProposal loanProposal);
}

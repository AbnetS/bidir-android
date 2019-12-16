package com.gebeya.mobile.bidir.data.loanProposal.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 8/17/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProposalRemote extends BaseRemoteSource<LoanProposalService> implements LoanProposalRemoteSource {

    @Inject LoanProposalParser loanProposalParser;

    public LoanProposalRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.LOAN_PROPOSAL, LoanProposalService.class);
    }

    @Override
    public Observable<List<LoanProposal>> getAll() {
        return build()
                .call(service.getAll())
                .map(object -> {
                    final List<LoanProposal> loanProposals = new ArrayList<>();
                    final JsonArray array = object.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        JsonObject loanProposalObject = array.get(i).getAsJsonObject();
                        loanProposals.add(loanProposalParser.parse(loanProposalObject));
                    }

                    return loanProposals;
                });
    }

    @Override
    public Observable<LoanProposal> getOneByClient(@NonNull String clientId) {
        return build()
                .call(service.getOneByClient(clientId))
                .map(object -> loanProposalParser.parse(object));
    }

    @Override
    public Observable<LoanProposal> get(@NonNull String loanProposalId) {
        return build()
                .call(service.get(loanProposalId))
                .map(object -> loanProposalParser.parse(object));
    }

    @Override
    public Observable<LoanProposal> create(@NonNull ACATApplication acatApplication, @NonNull LoanProduct loanProduct) {
        final JsonObject request = new JsonObject();
        request.addProperty("client", acatApplication.clientID);
        request.addProperty("client_acat", acatApplication._id);
        request.add("loan_detail", createLoanDetail(
                loanProduct.maxLoanAmount
        ));

        return build()
                .call(service.create(request))
                .map(object -> loanProposalParser.parse(object));
    }

    @Override
    public Observable<LoanProposal> saveLoanProposal(@NonNull LoanProposal loanProposal, @NonNull CashFlow cashFlow, @NonNull ACATApplication acatApplication) {
        final JsonObject request = new JsonObject();
        request.addProperty("loan_proposed", loanProposal.loanProposed);
        request.addProperty("loan_requested", loanProposal.loanRequested);
        request.add("loan_detail", updateLoanDetail(
                loanProposal.totalCostOfLoan,
                loanProposal.totalDeductible
        ));
//        request.add("net_cash_flow", createNetCashFlow(cashFlow));
        request.addProperty("repayable", loanProposal.repayable);
        request.addProperty("total_revenue", acatApplication.estimatedTotalRevenue);
        request.addProperty("total_cost", acatApplication.estimatedTotalCost);
        request.addProperty("status", LoanProposalParser.STATUS_API_IN_PROGRESS);

        return build()
                .call(service.update(loanProposal._id, request))
                .map(object -> loanProposalParser.parse(object));
    }

    @Override
    public Observable<LoanProposal> updateLoanProposal(@NonNull LoanProposal loanProposal) {
        final JsonObject request = new JsonObject();
        request.addProperty("loan_approved", loanProposal.loanApproved);
        request.addProperty("status", LoanProposalParser.STATUS_API_AUTHORIZED);
        return build()
                .call(service.update(loanProposal._id, request))
                .map(object -> loanProposalParser.parse(object));
    }

    @Override
    public Observable<LoanProposal> updateApiStatus(@NonNull LoanProposal loanProposal, @NonNull String status) {
        final JsonObject request = new JsonObject();
        request.addProperty("status", status);

        return build()
                .call(service.update(loanProposal._id, request))
                .map(object -> loanProposalParser.parse(object));
    }

    private JsonObject createLoanDetail(double maxAmount) {
        final JsonObject object = new JsonObject();

        object.addProperty("max_amount", maxAmount);

        return object;
    }

    private JsonObject updateLoanDetail(double totalCostOfLoan, double totalDeductible) {
        final JsonObject object = new JsonObject();

        object.addProperty("total_cost_of_loan", totalCostOfLoan);
        object.addProperty("total_deductible", totalDeductible);

        return object;
    }
    private JsonObject createNetCashFlow(@NonNull CashFlow cashFlow) {
        final JsonObject object = new JsonObject();

        object.addProperty("jan", cashFlow.january);
        object.addProperty("feb", cashFlow.february);
        object.addProperty("mar", cashFlow.march);
        object.addProperty("apr", cashFlow.april);
        object.addProperty("may", cashFlow.may);
        object.addProperty("june", cashFlow.june);
        object.addProperty("july", cashFlow.july);
        object.addProperty("aug", cashFlow.august);
        object.addProperty("sep", cashFlow.september);
        object.addProperty("oct", cashFlow.october);
        object.addProperty("nov", cashFlow.november);
        object.addProperty("dec", cashFlow.december);

        return object;
    }
}

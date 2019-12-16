package com.gebeya.mobile.bidir.data.loanProposal.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalParser;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalRemoteSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 8/17/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProposalRepo implements LoanProposalRepoSource {

    @Inject LoanProposalRemoteSource remote;
    @Inject LoanProposalLocalSource local;

    public LoanProposalRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable saveLoanProposal(@NonNull LoanProposal loanProposal, @NonNull CashFlow cashFlow, @NonNull ACATApplication acatApplication) {
        return remote.saveLoanProposal(loanProposal, cashFlow, acatApplication)
                .flatMapCompletable(data -> {
                    local.put(data);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<LoanProposal> updateLoanProposal(@NonNull LoanProposal loanProposal) {
        return remote.updateLoanProposal(loanProposal)
                .flatMap(data -> local.put(data));
    }

    @Override
    public Observable<Boolean> changeApiStatus(@NonNull LoanProposal loanProposal, @NonNull String status) {
        return remote.updateApiStatus(loanProposal, status)
                .flatMap(data -> {
                    local.put(data);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> submitLoanProposal(@NonNull LoanProposal loanProposal) {
        return remote.updateApiStatus(loanProposal, LoanProposalParser.STATUS_API_SUBMITTED)
                .flatMap(data -> {
                    local.put(data);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> approveLoanProposal(@NonNull LoanProposal loanProposal) {
        return remote.updateApiStatus(loanProposal, LoanProposalParser.STATUS_API_AUTHORIZED)
                .flatMap(data -> {
                    local.put(data);
                    return Observable.just(true);
                });
    }

    @Override
    public Observable<Boolean> declineForReview(@NonNull LoanProposal loanProposal) {
        return remote.updateApiStatus(loanProposal, LoanProposalParser.STATUS_API_DECLINED_FOR_REVIEW)
                .flatMap(data -> {
                    local.put(data);
                    return Observable.just(true);
                });
    }


    @Override
    public Observable<List<LoanProposal>> getAll() {
        return local.getAll();
    }

    @Override
    public Observable<List<LoanProposal>> fetchAll() {
        return local.getAll()
                .flatMap(loanProposals ->
                    loanProposals.isEmpty() ? fetchForceAll() : Observable.just(loanProposals)
                );
    }

    @Override
    public Observable<List<LoanProposal>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
                    final List<LoanProposal> loanProposals = new ArrayList<>();
                    loanProposals.addAll(responses);
                    local.putAll(loanProposals);
                    return Observable.just(loanProposals);
                });
    }

    @Override
    public Observable<Data<LoanProposal>> get(@NonNull String id) {
        return local.get(id);
    }

    @Override
    public Observable<Data<LoanProposal>> get(int position) {
        return local.get(position);
    }

    @Override
    public Observable<Data<LoanProposal>> first() {
        return local.first();
    }

    @Override
    public Observable<LoanProposal> fetch(@NonNull String id) {
        return local.get(id)
                .flatMap(data -> data.empty() ? fetchForce(id) : Observable.just(data.get()));
    }

    @Override
    public Observable<LoanProposal> fetchForce(@NonNull String id) {
        return remote.get(id)
                .flatMap(response -> local.put(response));
    }

    @Override
    public Observable<LoanProposal> fetchByClient(@NonNull String clientId) {
        return local.getProposalByClient(clientId)
                .flatMap(data -> data.empty() ? fetchForceByClient(clientId) : Observable.just(data.get()));
    }

    @Override
    public Observable<LoanProposal> fetchForceByClient(@NonNull String clientId) {
        return remote.getOneByClient(clientId)
                .flatMap(response -> local.put(response));
    }

    @Override
    public Completable createLoanProposal(@NonNull ACATApplication acatApplication, @NonNull LoanProduct loanProduct) {
        return remote.create(acatApplication, loanProduct)
                .flatMapCompletable(response -> {
                    local.put(response);
                    return Completable.complete();
                });
    }

    @Override
    public int size() {
        return local.size();
    }
}

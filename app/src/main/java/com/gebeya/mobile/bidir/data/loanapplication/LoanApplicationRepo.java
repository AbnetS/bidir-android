package com.gebeya.mobile.bidir.data.loanapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.answer.local.AnswerLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanapplication.local.LoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.loanapplication.remote.LoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.loanapplication.remote.LoanApplicationResponse;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link LoanApplicationRepoSource} contract.
 */
public class LoanApplicationRepo implements LoanApplicationRepoSource {

    @Inject LoanApplicationLocalSource local;
    @Inject LoanApplicationRemoteSource remote;

    @Inject AnswerLocalSource answerLocal;

    public LoanApplicationRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable pushQuestions(@NonNull String loanApplicationId) {
        final LoanApplicationResponse response = new LoanApplicationResponse();
        return local.get(loanApplicationId)
                .flatMap(data -> {
                    response.application = data.get();
                    return answerLocal.getAll(QAHelper.REF_TYPE_LOAN_APPLICATION, loanApplicationId);
                })
                .flatMap(answers -> {
                    response.answers = answers;
                    return remote.update(response);
                })
                .flatMapCompletable(application -> {
                    local.put(application);
                    return Completable.complete();
                });
    }

    @Override
    public Completable pushStatus(@NonNull LoanApplication application,
                                  @NonNull String status,
                                  @Nullable String remark) {
        return remote.updateApiStatus(application, status, remark)
                .flatMapCompletable(apiApplication -> {
                    local.put(apiApplication);
                    return Completable.complete();
                });
    }

    @Override
    public Completable createLoanApplication(@NonNull Client client) {
        return remote.create(client)
                .flatMapCompletable(response -> {
                    local.put(response.application);
                    answerLocal.putAll(response.answers, QAHelper.REF_TYPE_LOAN_APPLICATION, response.application._id);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<Data<LoanApplication>> first() {
        return local.first();
    }

    @Override
    public Observable<Data<LoanApplication>> get(@NonNull String id) {
        return local.get(id);
    }

    @Override
    public Observable<Data<LoanApplication>> get(int position) {
        return local.get(position);
    }


    @Override
    public Observable<List<LoanApplication>> getAll() {
        return local.getAll();
    }

    @Override
    public Observable<List<LoanApplication>> fetchAll() {
        return local.getAll()
                .flatMap(applications ->
                        applications.isEmpty() ? fetchForceAll() : Observable.just(applications)
                );
    }

    @Override
    public Observable<List<LoanApplication>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
                    final List<LoanApplication> applications = new ArrayList<>();
                    for (LoanApplicationResponse response : responses) {
                        final LoanApplication application = response.application;
                        applications.add(application);
                        answerLocal.putAll(response.answers, QAHelper.REF_TYPE_LOAN_APPLICATION, application._id);
                    }
                    local.putAll(applications);
                    return Observable.just(applications);
                });
    }

    @Override
    public Observable<LoanApplication> fetch(@NonNull String id) {
        return local.get(id)
                .flatMap(data ->
                        data.empty() ? fetchForce(id) : Observable.just(data.get())
                );
    }

    @Override
    public Observable<LoanApplication> fetchForce(@NonNull String id) {
        return remote.downloadApplication(id)
                .flatMap(response -> {
                    answerLocal.putAll(response.answers, QAHelper.REF_TYPE_LOAN_APPLICATION, response.application._id);
                    return local.put(response.application);
                });
    }

    @Override
    public int size() {
        return local.size();
    }
}
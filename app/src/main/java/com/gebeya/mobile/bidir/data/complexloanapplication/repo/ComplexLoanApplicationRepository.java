package com.gebeya.mobile.bidir.data.complexloanapplication.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationRequest;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationParser;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemote;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Implementation for the {@link ComplexLoanApplicationRepositorySource} interface.
 */
public class ComplexLoanApplicationRepository implements ComplexLoanApplicationRepositorySource {

    @Inject ComplexLoanApplicationRemote remote;
    @Inject ComplexLoanApplicationLocalSource local;

    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject SectionLocalSource sectionLocal;

    @Inject ComplexLoanApplicationParser parser;


    public ComplexLoanApplicationRepository() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable createLoanApplication(@NonNull Client client) {
        return remote.create(client)
                .flatMapCompletable(response -> {
                    final ComplexLoanApplication application = response.application;
                    local.put(application);
                    for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                        complexQuestionLocal.put(questionResponse.question);
                        prerequisiteLocal.putAll(questionResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);
                    return Completable.complete();
                });
    }

    @Override
    public Completable saveApplication(@NonNull String applicationId) {
        return uploadApplication(applicationId, ComplexLoanApplicationParser.STATUS_API_IN_PROGRESS, null);
    }

    @Override
    public Completable submitApplication(@NonNull String applicationId) {
        return uploadApplication(applicationId, ComplexLoanApplicationParser.STATUS_API_SUBMITTED, null);
    }

    @Override
    public Completable acceptApplication(@NonNull String applicationId) {
        return uploadApplication(applicationId, ComplexLoanApplicationParser.STATUS_API_ACCEPTED, null);
    }

    @Override
    public Completable declineApplication(@NonNull String applicationId, boolean isFinal, @Nullable String remark) {
        final String status = isFinal ? ComplexLoanApplicationParser.STATUS_API_DECLINED_FINAL : ComplexLoanApplicationParser.STATUS_API_DECLINED_UNDER_REVIEW;
        return uploadApplication(applicationId, status, remark);
    }

    private Completable uploadApplication(@NonNull String applicationId, @NonNull String apiStatus, @Nullable String remark) {
        final ComplexLoanApplicationRequest request = new ComplexLoanApplicationRequest();
        request.status = apiStatus;

        return local.get(applicationId)
                .flatMap(data -> {
                    final ComplexLoanApplication application = data.get();
                    application.status = parser.getLocalStatus(apiStatus);
                    application.remark = remark == null ? "" : remark;
                    local.markForUpload(application);

                    return complexQuestionLocal.getRequests(applicationId, Constants.REF_TYPE_LOAN_APPLICATION);
                })
                .flatMap(requests -> sectionLocal.getRequests(applicationId, Constants.REF_TYPE_LOAN_APPLICATION, requests))
                .flatMap(sectionRequests -> {
                    request.sectionRequests = sectionRequests;
                    return complexQuestionLocal.getRequests(applicationId, Constants.REF_TYPE_LOAN_APPLICATION);
                })
                .flatMap(list -> {
                    request.requests = list;
                    return remote.uploadApplication(applicationId, request, remark);
                })
                .flatMapCompletable(response -> {
                    final ComplexLoanApplication application = response.application;
                    application.uploaded = true;
                    application.modified = false;
                    application.updatedAt = new DateTime();
                    local.put(application);
                    for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                        complexQuestionLocal.put(questionResponse.question);
                        prerequisiteLocal.putAll(questionResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<List<ComplexLoanApplication>> fetchAll() {
        return local.getAll()
                .flatMap(
                        applications -> applications.isEmpty() ? fetchForceAll() : Observable.just(applications)
                );
    }

    @Override
    public Observable<List<ComplexLoanApplication>> fetchForceAll() {
        return remote.downloadAll()
                .flatMap(responses -> {
                    final List<ComplexLoanApplication> applications = new ArrayList<>();
                    for (ComplexLoanApplicationResponse response : responses) {
                        final ComplexLoanApplication application = response.application;
                        applications.add(application);
                        local.put(application);
                        for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                            complexQuestionLocal.put(questionResponse.question);
                            prerequisiteLocal.putAll(questionResponse.prerequisites);
                        }
                        sectionLocal.putAll(response.sections);
                    }
                    local.putAll(applications);
                    return Observable.just(applications);
                });
    }

    @Override
    public Observable<ComplexLoanApplication> fetch(@NonNull String id) {
        return local.get(id)
                .flatMap(
                        data -> data.empty() ? fetchForce(id) : Observable.just(data.get())
                );
    }

    @Override
    public Observable<ComplexLoanApplication> fetchForce(@NonNull String id) {
        return remote.download(id)
                .flatMap(response -> {
                    final ComplexLoanApplication application = response.application;

                    local.put(application);
                    for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                        complexQuestionLocal.put(questionResponse.question);
                        prerequisiteLocal.putAll(questionResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);

                    return Observable.just(application);
                });
    }
}

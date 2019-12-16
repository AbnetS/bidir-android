package com.gebeya.mobile.bidir.data.base.remote.backend.sync.loanapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.backend.sync.BaseSyncService;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationRequest;
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

import java.util.List;

import javax.inject.Inject;

public class ComplexLoanApplicationSyncService extends BaseSyncService {

    @Inject ComplexLoanApplicationLocalSource loanApplicationLocal;
    @Inject ComplexLoanApplicationRemote loanApplicationRemote;
    @Inject PrerequisiteLocalSource prerequisiteLocal;

    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject SectionLocalSource sectionLocal;

    @Inject ComplexLoanApplicationSyncState state;

    @Inject ComplexLoanApplicationParser parser;

    public ComplexLoanApplicationSyncService() {
        super(ComplexLoanApplicationSyncService.class.getSimpleName());
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        d("Service Started");
        if (state.busy()) {
            d("-> Service is already running");
            return;
        }

        d("Getting a list of applications to upload");
        final List<ComplexLoanApplication> applications = loanApplicationLocal.getAllModifiedNonUploaded();
        final int length = applications.size();
        if (length == 0) {
            d("-> No applications to upload found");
            return;
        }

        d("-> Found %d applications%s to upload", length, length == 1 ? "" : "s");
        d("Uploading data...");
        state.setBusy();
        for (int i = 0; i < length; i++) {
            final ComplexLoanApplication complexApplication = applications.get(i);
            final String applicationId = complexApplication._id;

            d("-> Uploading application: " + applicationId);
            final ComplexLoanApplicationRequest request = new ComplexLoanApplicationRequest();
            request.status = parser.getApiStatus(complexApplication.status);

            complexQuestionLocal.getRequests(applicationId, Constants.REF_TYPE_LOAN_APPLICATION)
                    .flatMap(list -> {
                        request.requests = list;
                        return sectionLocal.getRequests(applicationId, Constants.REF_TYPE_LOAN_APPLICATION, list);
                    })
                    .flatMap(list -> {
                        request.sectionRequests = list;
                        return loanApplicationRemote.uploadApplication(applicationId, request, complexApplication.remark);
                    })
                    .subscribe(response -> {
                        final ComplexLoanApplication application = response.application;
                        application.uploaded = true;
                        application.modified = false;
                        application.updatedAt = new DateTime();
                        loanApplicationLocal.put(application);
                        for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                            complexQuestionLocal.put(questionResponse.question);
                            prerequisiteLocal.putAll(questionResponse.prerequisites);
                        }
                        sectionLocal.putAll(response.sections);
                    });
        }
        state.setIdle();
    }
}
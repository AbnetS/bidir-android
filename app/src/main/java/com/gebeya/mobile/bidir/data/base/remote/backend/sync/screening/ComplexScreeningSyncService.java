package com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.backend.sync.BaseSyncService;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningRequest;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningParser;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class ComplexScreeningSyncService extends BaseSyncService {

    @Inject BoxStore store;
    private final Box<User> userBox;

    @Inject ComplexScreeningLocalSource screeningLocal;
    @Inject ComplexScreeningRemoteSource screeningRemote;
    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject ComplexScreeningRepositorySource screeningRepo;
    @Inject GroupLocalSource groupLocal;

    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject SectionLocalSource sectionLocal;

    @Inject ComplexScreeningParser parser;

    @Inject ComplexScreeningSyncState state;

    public ComplexScreeningSyncService() {
        super(ComplexScreeningSyncService.class.getSimpleName());

        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
        userBox = store.boxFor(User.class);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        d("Service Started");
        if (state.busy()) {
            d("-> Service is already running");
            return;
        }

        final List<User> users = userBox.getAll();
        if (users.isEmpty()) {
            e("-> Cannot upload data: User is currently logged out");
            return;
        }

        d("Getting a list of modified screenings to upload");
        final List<ComplexScreening> screenings = screeningLocal.getAllModifiedNonUploaded();
        final int modifiedLength = screenings.size();
        if (modifiedLength == 0) {
            d("-> No modified screenings to upload found");
        } else {
            d("-> Found %d modified screening%s to upload", modifiedLength, modifiedLength == 1 ? "" : "s");
            d("Uploading data...");
            state.setBusy();
            for (int i = 0; i < modifiedLength; i++) {
                final ComplexScreening complexScreening = screenings.get(i);
                final String screeningId = complexScreening._id;

                d("-> Uploading screening: " + screeningId);
                final ComplexScreeningRequest request = new ComplexScreeningRequest();
                request.status = parser.getApiStatus(complexScreening.status);

                complexQuestionLocal.getRequests(complexScreening._id, Constants.REF_TYPE_SCREENING)
                        .flatMap(list -> {
                            request.requests = list;
                            return sectionLocal.getRequests(screeningId, Constants.REF_TYPE_SCREENING, list);
                        })
                        .flatMap(list -> {
                            request.sectionRequests = list;
                            return screeningRemote.uploadScreening(screeningId, request, complexScreening.remark);
                        })
                        .subscribe(response -> {
                            d("-> Screening %s uploaded", screeningId);
                            final ComplexScreening screening = response.screening;
                            screening.uploaded = true;
                            screening.modified = false;
                            screening.updatedAt = new DateTime();
                            screeningLocal.put(screening);
                            for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                                complexQuestionLocal.put(questionResponse.question);
                                prerequisiteLocal.putAll(questionResponse.prerequisites);
                            }
                            sectionLocal.putAll(response.sections);
                        }, throwable -> {
                            e("-> Uploading screening failed: " + throwable.getMessage());
                            throwable.printStackTrace();
                        });
            }
        }

        // TODO: Upload any new screenings created offline

        state.setBusy();
        d("Downloading all the screenings...");
        screeningRepo.fetchForceAll()   // TODO: Optimize this to getByType only screenings that are needed
                .map(complexScreenings -> complexScreenings.size())
                .subscribe(length -> d("-> Downloaded %d screening%s", length, length == 1 ? "" : "s"),
                        throwable -> {
                            e("-> Downloading screenings failed:");
                            throwable.printStackTrace();
                        });

        groupLocal.getAllScreenings();

        state.setIdle();
    }
}

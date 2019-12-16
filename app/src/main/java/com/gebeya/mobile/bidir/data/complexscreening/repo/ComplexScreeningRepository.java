package com.gebeya.mobile.bidir.data.complexscreening.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningRequest;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningResponse;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningParser;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.screening.Screening;
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
 * Concrete implementation for the {@link ComplexScreeningRepositorySource} interface.
 */
public class ComplexScreeningRepository implements ComplexScreeningRepositorySource {

    @Inject ComplexScreeningLocalSource local;
    @Inject ComplexScreeningRemoteSource remote;

    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject SectionLocalSource sectionLocal;

    @Inject ComplexScreeningParser parser;

    public ComplexScreeningRepository() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable saveScreening(@NonNull String screeningId) {
        return uploadScreening(screeningId, ComplexScreeningParser.STATUS_API_IN_PROGRESS, null);
    }

    @Override
    public Completable submitScreening(@NonNull String screeningId) {
        return uploadScreening(screeningId, ComplexScreeningParser.STATUS_API_SUBMITTED, null);
    }

    @Override
    public Completable approveScreening(@NonNull String screeningId) {
        return uploadScreening(screeningId, ComplexScreeningParser.STATUS_API_APPROVED, null);
    }

    @Override
    public Completable declineScreening(@NonNull String screeningId, boolean isFinal, @Nullable String remark) {
        final String status = isFinal ? ComplexScreeningParser.STATUS_API_DECLINED_FINAL : ComplexScreeningParser.STATUS_API_DECLINED_UNDER_REVIEW;
        return uploadScreening(screeningId, status, remark);
    }

    @Override
    public Observable<ComplexScreeningResponse> createScreening(@NonNull String clientId, boolean isGrouped) {
        return remote.create(clientId, isGrouped)
                .flatMap(response -> {
                    final ComplexScreening screening = response.screening;
                    local.put(screening);

                    for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                        complexQuestionLocal.put(questionResponse.question);
                        prerequisiteLocal.putAll(questionResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);
                    return Observable.just(response);
                });
    }

    private Completable uploadScreening(@NonNull String screeningId,
                                        @NonNull String apiStatus,
                                        @Nullable String remark) {
        final ComplexScreeningRequest request = new ComplexScreeningRequest();
        request.status = apiStatus;

        return local.get(screeningId)
                .flatMap(data -> {
                    final ComplexScreening screening = data.get();
                    screening.status = parser.getLocalStatus(apiStatus);
                    screening.remark = remark == null ? "" : remark;
                    local.markForUpload(screening);

                    return complexQuestionLocal.getRequests(screeningId, Constants.REF_TYPE_SCREENING);
                })
                .flatMap(list -> {
                    request.requests = list;
                    return sectionLocal.getRequests(screeningId, Constants.REF_TYPE_SCREENING, list);
                })
                .flatMap(list -> {
                    request.sectionRequests = list;
                    return remote.uploadScreening(screeningId, request, remark);
                })
                .flatMapCompletable(response -> {
                    final ComplexScreening screening = response.screening;
                    screening.uploaded = true;
                    screening.modified = false;
                    screening.updatedAt = new DateTime();
                    local.put(screening);
                    for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                        complexQuestionLocal.put(questionResponse.question);
                        prerequisiteLocal.putAll(questionResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<List<ComplexScreening>> fetchAll() {
        return local.getAll()
                .flatMap(screenings ->
                        screenings.isEmpty() ? fetchForceAll() : Observable.just(screenings)
                );
    }

    @Override
    public Observable<List<ComplexScreening>> fetchForceAll() {
        return remote.downloadAll()
                .flatMap(responses -> {
                    final List<ComplexScreening> screenings = new ArrayList<>();
                    for (ComplexScreeningResponse response : responses) {
                        final ComplexScreening screening = response.screening;
                        local.put(screening);
                        for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                            complexQuestionLocal.put(questionResponse.question);
                            prerequisiteLocal.putAll(questionResponse.prerequisites);
                        }
                        sectionLocal.putAll(response.sections);
                        screenings.add(screening);
                    }

                    return Observable.just(screenings);
                });
    }

    @Override
    public Observable<ComplexScreening> fetch(@NonNull String id) {
        return local.get(id)
                .flatMap(data ->
                        data.empty() ? fetchForce(id) : Observable.just(data.get())
                );
    }

    @Override
    public Observable<ComplexScreening> fetchForce(@NonNull String id) {
        return remote.download(id)
                .flatMap(response -> {
                    final ComplexScreening screening = response.screening;
                    local.put(screening);
                    for (ComplexQuestionResponse screeningResponse : response.questionResponses) {
                        complexQuestionLocal.put(screeningResponse.question);
                        prerequisiteLocal.putAll(screeningResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);
                    return Observable.just(screening);
                });
    }
}

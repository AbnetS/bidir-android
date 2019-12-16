package com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningResponse;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocal;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreeningResponse;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.local.GroupedComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class GroupedComplexScreeningRepo implements GroupedComplexScreeningRepoSource{

    @Inject ComplexScreeningLocal complexScreeningLocal;

    @Inject GroupedComplexScreeningLocalSource local;
    @Inject GroupedComplexScreeningRemoteSource remote;

    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject SectionLocalSource sectionLocal;
    @Inject ComplexQuestionLocalSource complexQuestionLocal;

    public GroupedComplexScreeningRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<GroupedComplexScreening> getById(@NonNull String groupId) {
        return remote.download(groupId)
                .flatMap(response -> {
                    updateGroupComplexScreeningId(response);
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexScreening> submitGroup(@NonNull String groupId) {
        return remote.submitGroupScreening(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexScreening> approveGroup(@NonNull String groupId) {
        return remote.approveGroupScreening(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexScreening> updateStatus(@NonNull String groupId) {
        return remote.updateStatus(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexScreening> createNewLoanCycle(@NonNull String groupId, @NonNull String loanAmount) {
        return remote.createNewLoanCycle(groupId, loanAmount)
                .flatMap(response ->{
                    final GroupedComplexScreening groupedComplexScreening = response.groupedComplexScreening;

                    for (ComplexScreeningResponse screeningResponse : response.complexScreeningResponseList) {
                        for (ComplexQuestionResponse questionResponse : screeningResponse.questionResponses) {
                            complexQuestionLocal.put(questionResponse.question);
                            prerequisiteLocal.putAll(questionResponse.prerequisites);
                        }
                        sectionLocal.putAll(screeningResponse.sections);
                    }

                    return local.put(groupedComplexScreening);
                });
    }

    @Override
    public int size() {
        return local.size();
    }

    @Override
    public Observable<List<GroupedComplexScreening>> fetchAll() {
        return local.getAll()
                .flatMap(screenings ->
                        screenings.isEmpty() ? fetchForceAll() : Observable.just(screenings));
    }

    @Override
    public Observable<List<GroupedComplexScreening>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
                    updateGroupComplexScreeningIds(responses);
                    local.putAll(responses);
                    return Observable.just(responses);
                });
    }

    @Override
    public Observable<GroupedComplexScreening> fetch(@NonNull String id) {
        return local.getByGroupId(id).flatMap(data -> {
            if (data.empty()) {
                return fetchForce(id);
            } else {
                return Observable.just(data.get());
            }
        });
    }

    @Override
    public Observable<GroupedComplexScreening> fetchForce(@NonNull String id) {
        return getById(id);
    }

    private void updateGroupComplexScreeningIds(List<GroupedComplexScreening> groupedScreenings) {
        final int length = groupedScreenings.size();
        for (int i = 0; i < length; i++) {
            final GroupedComplexScreening groupedScreening = groupedScreenings.get(i);
            final List<ComplexScreening> complexScreenings = groupedScreening.complexScreenings;
            complexScreeningLocal.updateWithLocalIds(complexScreenings);
            updateComplexScreeningGroupTarget(complexScreenings, groupedScreening);
        }
    }

    private void updateGroupComplexScreeningId(GroupedComplexScreening groupedComplexScreening) {
        final List<ComplexScreening> complexScreenings = groupedComplexScreening.complexScreenings;
        complexScreeningLocal.updateWithLocalIds(complexScreenings);
        updateComplexScreeningGroupTarget(complexScreenings, groupedComplexScreening);
    }

    private void updateComplexScreeningGroupTarget(List<ComplexScreening> complexScreenings, GroupedComplexScreening screening) {
        for (ComplexScreening complexScreening: complexScreenings) {
            complexScreening.groupedComplexScreening.setTarget(screening);
        }
    }


}

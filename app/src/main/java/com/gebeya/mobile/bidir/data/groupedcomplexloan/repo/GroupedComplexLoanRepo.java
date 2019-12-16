package com.gebeya.mobile.bidir.data.groupedcomplexloan.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationLocal;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.local.GroupedComplexLoanLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanRemoteSource;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class GroupedComplexLoanRepo implements GroupedComplexLoanRepoSource {

    @Inject ComplexLoanApplicationLocal complexLoanApplicationLocal;
    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject SectionLocalSource sectionLocal;
    @Inject GroupedComplexLoanLocalSource local;
    @Inject GroupedComplexLoanRemoteSource remote;


    public GroupedComplexLoanRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<GroupedComplexLoan> getById(@NonNull String groupId) {
        return remote.download(groupId)
                .flatMap(response -> {
//                    updateGroupComplexLoanId(response);
                    final GroupedComplexLoan groupedLoan = response.groupedComplexLoan;

                    for (ComplexLoanApplicationResponse loanResponse : response.complexLoanApplicationResponses) {
                        for (ComplexQuestionResponse questionResponse : loanResponse.questionResponses) {
                            complexQuestionLocal.put(questionResponse.question);
                            prerequisiteLocal.putAll(questionResponse.prerequisites);
                        }
                        sectionLocal.putAll(loanResponse.sections);
                    }

                    return local.put(groupedLoan);
                });
    }

    @Override
    public Observable<GroupedComplexLoan> submitGroup(@NonNull String groupId) {
        return remote.submitGroupLoan(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexLoan> approveGroup(@NonNull String groupId) {
        return remote.approveGroupLoan(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexLoan> updateStatus(@NonNull String groupId) {
        return remote.updateStatus(groupId)
                .flatMap(response -> {
                    local.put(response);
                    return Observable.just(response);
                });
    }

    @Override
    public Observable<GroupedComplexLoan> create(@NonNull String groupId) {
        return remote.create(groupId)
                .flatMap(response -> local.put(response));
    }

    @Override
    public int size() {
        return local.size();
    }

    @Override
    public Observable<List<GroupedComplexLoan>> fetchAll() {
        return local.getAll()
                .flatMap(loans ->
                        loans.isEmpty() ? fetchForceAll() : Observable.just(loans));
    }

    @Override
    public Observable<List<GroupedComplexLoan>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
//                    updateGroupComplexLoanIds(responses);
                    local.putAll(responses);
                    return Observable.just(responses);
                });
    }

    @Override
    public Observable<GroupedComplexLoan> fetch(@NonNull String id) {
        return local.getByGroupId(id).flatMap(data -> {
            if (data.empty()) {
                return fetchForce(id);
            } else {
                return Observable.just(data.get());
            }
        });
    }

    @Override
    public Observable<GroupedComplexLoan> fetchForce(@NonNull String id) {
        return getById(id);
    }

    private void updateGroupComplexLoanIds(List<GroupedComplexLoan> groupedLoans) {
        final int length = groupedLoans.size();
        for (int i = 0; i < length; i++) {
            final GroupedComplexLoan groupedLoan = groupedLoans.get(i);
            final List<ComplexLoanApplication> complexLoans = groupedLoan.complexLoans;
            complexLoanApplicationLocal.updateWithLocalIds(complexLoans);
            updateComplexLoanGroupTarget(complexLoans, groupedLoan);
        }
    }

    private void updateGroupComplexLoanId(GroupedComplexLoan groupedComplexLoan) {
        final List<ComplexLoanApplication> complexLoanApplications = groupedComplexLoan.complexLoans;
        complexLoanApplicationLocal.updateWithLocalIds(complexLoanApplications);
        updateComplexLoanGroupTarget(complexLoanApplications, groupedComplexLoan);
    }

    private void updateComplexLoanGroupTarget(List<ComplexLoanApplication> complexLoanApplications, GroupedComplexLoan loan) {
        for (ComplexLoanApplication complexLoanApplication: complexLoanApplications) {
            complexLoanApplication.groupedComplexLoan.setTarget(loan);
        }
    }
}

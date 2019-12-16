package com.gebeya.mobile.bidir.data.form.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.gebeya.mobile.bidir.data.form.ComplexFormResponse;
import com.gebeya.mobile.bidir.data.form.local.ComplexFormLocalSource;
import com.gebeya.mobile.bidir.data.form.remote.ComplexFormRemoteSource;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Repository implementation for the {@link ComplexFormRepositorySource} contract.
 */
public class ComplexFormRepository implements ComplexFormRepositorySource {

    @Inject ComplexFormLocalSource local;
    @Inject ComplexFormRemoteSource remote;

    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject SectionLocalSource sectionLocal;

    public ComplexFormRepository() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Observable<List<ComplexForm>> fetchAll() {
        return local.getAll()
                .flatMap(forms ->
                        forms.isEmpty() ? fetchForceAll() : Observable.just(forms)
                );
    }

    @Override
    public Observable<List<ComplexForm>> fetchForceAll() {
        return remote.downloadAll()
                .flatMap(responses -> {
                    final List<ComplexForm> forms = new ArrayList<>();
                    for (ComplexFormResponse response : responses) {
                        final ComplexForm form = response.form;
                        local.put(form);
                        for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                            complexQuestionLocal.put(questionResponse.question);
                            prerequisiteLocal.putAll(questionResponse.prerequisites);
                        }
                        sectionLocal.putAll(response.sections);
                        forms.add(form);
                    }

                    return Observable.just(forms);
                });
    }

    @Override
    public Observable<ComplexForm> fetch(@NonNull String id) {
        return local.get(id)
                .flatMap(data ->
                        data.empty() ? fetchForce(id) : Observable.just(data.get())
                );
    }

    @Override
    public Observable<ComplexForm> fetchForce(@NonNull String id) {
        return remote.download(id)
                .flatMap(response -> {
                    final ComplexForm form = response.form;
                    local.put(form);
                    for (ComplexQuestionResponse questionResponse : response.questionResponses) {
                        complexQuestionLocal.put(questionResponse.question);
                        prerequisiteLocal.putAll(questionResponse.prerequisites);
                    }
                    sectionLocal.putAll(response.sections);
                    return Observable.just(form);
                });
    }
}
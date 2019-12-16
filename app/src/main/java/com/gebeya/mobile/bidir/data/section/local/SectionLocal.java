package com.gebeya.mobile.bidir.data.section.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.section.Section;
import com.gebeya.mobile.bidir.data.section.SectionRequest;
import com.gebeya.mobile.bidir.data.section.Section_;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link SectionLocalSource} interface.
 */
public class SectionLocal extends BaseLocalSource implements SectionLocalSource {

    private final Box<Section> box;
    private final Box<ComplexQuestion> questionLocalBox;

    public SectionLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Section.class);
        questionLocalBox = store.boxFor(ComplexQuestion.class);
    }

    @Override
    public Observable<List<Section>> putAll(@NonNull List<Section> sections) {
        final int length = sections.size();
        for (int i = 0; i < length; i++) {
            final Section section = sections.get(i);
            box.query().equal(Section_._id, section._id).build().remove();
        }

        box.put(sections);
        return Observable.just(sections);
    }

    @Override
    public Observable<List<Section>> getAll(@NonNull String referenceId,
                                            @NonNull String referenceType) {
        final List<Section> sections = box.query().equal(Section_.referenceId, referenceId)
                .and()
                .equal(Section_.referenceType, referenceType)
                .build()
                .find();
        return Observable.just(sections);
    }

    @Override
    public Observable<List<SectionRequest>> getRequests(@NonNull String referenceId,
                                                        @NonNull String referenceType,
                                                        @NonNull List<ComplexQuestionRequest> questionRequests) {

        final List<SectionRequest> requests = new ArrayList<>();
        final List<Section> sections = box.query().equal(Section_.referenceId, referenceId)
                .and()
                .equal(Section_.referenceType, referenceType)
                .build()
                .find();

        final int length = sections.size();
        for (int i = 0; i < length; i++) {
            final SectionRequest request = new SectionRequest();

            final Section section = sections.get(i);
            request.section = section;
            request.requests = getSectionComplexQuestions(section, questionRequests);
            requests.add(request);
        }

        return Observable.just(requests);
    }

    private List<ComplexQuestionRequest> getSectionComplexQuestions(@NonNull Section section, @NonNull List<ComplexQuestionRequest> requests) {
        final List<ComplexQuestionRequest> found = new ArrayList<>();
        final int length = section.questionIds.size();
        for (int i = 0; i < length; i++) {
            final String id = section.questionIds.get(i);
            final ComplexQuestionRequest request = getComplexQuestionRequest(id, requests);
            if (request != null) {
                found.add(request);
            }
        }

        return found;
    }

    private ComplexQuestionRequest getComplexQuestionRequest(@NonNull String id, @NonNull List<ComplexQuestionRequest> requests) {
        final int length = requests.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestionRequest request = requests.get(i);
            if (request.question._id.equals(id)) {
                return request;
            }
        }
        return null;
    }
}

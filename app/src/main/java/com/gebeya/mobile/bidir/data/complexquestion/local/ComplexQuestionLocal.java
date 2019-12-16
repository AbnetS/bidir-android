package com.gebeya.mobile.bidir.data.complexquestion.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion_;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite_;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link ComplexQuestionLocalSource} source.
 */
public class ComplexQuestionLocal extends BaseLocalSource implements ComplexQuestionLocalSource {

    private final Box<ComplexQuestion> box;
    private final Box<Prerequisite> prerequisiteBox;

    public ComplexQuestionLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ComplexQuestion.class);
        prerequisiteBox = store.boxFor(Prerequisite.class);
    }

    @Override
    public Observable<List<ComplexQuestion>> getAll(@NonNull String referenceId, @NonNull String referenceType) {
        final List<ComplexQuestion> questions = box.query()
                .equal(ComplexQuestion_.referenceId, referenceId)
                .and()
                .equal(ComplexQuestion_.referenceType, referenceType)
                .build()
                .find();

        return Observable.just(questions);
    }

    @Override
    public Observable<ComplexQuestion> put(@NonNull ComplexQuestion item) {
        box.query().equal(ComplexQuestion_._id, item._id).build().remove();
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Observable<List<ComplexQuestion>> putAll(@NonNull List<ComplexQuestion> items) {
        box.put(items);
        return Observable.just(items);
    }

    @Override
    public Observable<List<ComplexQuestionRequest>> getRequests(@NonNull String referenceId, @NonNull String referenceType) {
        final List<ComplexQuestionRequest> requests = new ArrayList<>();
        final List<ComplexQuestion> questions = box.query()
                .equal(ComplexQuestion_.referenceId, referenceId)
                .and()
                .equal(ComplexQuestion_.referenceType, referenceType)
                .build()
                .find();

        final List<ComplexQuestion> parentQuestions = getGroupedQuestions(questions);
        final int length = parentQuestions.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestionRequest request = new ComplexQuestionRequest();

            final ComplexQuestion parentQuestion = parentQuestions.get(i);
            final List<ComplexQuestion> childQuestions = getChildQuestions(parentQuestion, questions);

            request.question = parentQuestion;
            request.prerequisites = prerequisiteBox.query()
                    .equal(Prerequisite_.parentQuestionId, parentQuestion._id)
                    .build().find();
            request.childQuestions = childQuestions;

            requests.add(request);
        }

        if (!questions.isEmpty()) {
            final int size = questions.size();
            for (int i = 0; i < size; i++) {
                final ComplexQuestionRequest request = new ComplexQuestionRequest();

                final ComplexQuestion question = questions.get(i);

                request.question = question;
                request.prerequisites = prerequisiteBox.query()
                        .equal(Prerequisite_.parentQuestionId, question._id)
                        .build().find();
                request.childQuestions = new ArrayList<>();
                requests.add(request);
            }
        }

        return Observable.just(requests);
    }

    private List<ComplexQuestion> getChildQuestions(@NonNull ComplexQuestion parentQuestion, @NonNull List<ComplexQuestion> questions) {
        final List<ComplexQuestion> found = new ArrayList<>();
        final Iterator<ComplexQuestion> iterator = questions.iterator();
        while (iterator.hasNext()) {
            final ComplexQuestion childQuestion = iterator.next();
            if (parentQuestion.subQuestions.contains(childQuestion._id)) {
                found.add(childQuestion);
                iterator.remove();
            }
        }

        return found;
    }

    private List<ComplexQuestion> getGroupedQuestions(final List<ComplexQuestion> questions) {
        final List<ComplexQuestion> groups = new ArrayList<>();
        final Iterator<ComplexQuestion> iterator = questions.iterator();
        while (iterator.hasNext()) {
            final ComplexQuestion question = iterator.next();
            if (question.type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
                groups.add(question);
                iterator.remove();
            }
        }

        return groups;
    }
}
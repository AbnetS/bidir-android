package com.gebeya.mobile.bidir.data.answer.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.answer.Answer_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link AnswerLocalSource} contract
 */
public class AnswerLocal extends BaseLocalSource implements AnswerLocalSource {

    private final Box<Answer> box;

    public AnswerLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Answer.class);
    }

    @Override
    public Observable<List<Answer>> getAll(@NonNull String referenceType, @NonNull String referenceId) {
        final List<Answer> answers = box.query()
                .equal(Answer_.referenceType, referenceType)
                .and()
                .equal(Answer_.referenceId, referenceId)
                .build()
                .find();

        return Observable.just(answers);
    }

    @Override
    public Observable<List<Answer>> putAll(@NonNull List<Answer> answers,
                                           @NonNull String referenceType,
                                           @NonNull String referenceId) {
        box.query()
                .equal(Answer_.referenceType, referenceType)
                .and()
                .equal(Answer_.referenceId, referenceId)
                .build().remove();
        box.put(answers);
        return Observable.just(answers);
    }

    @Override
    public Observable<Answer> put(@NonNull Answer item) {
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Completable removeAll(@NonNull String referenceType, @NonNull String referenceId) {
        box.query()
                .equal(Answer_.referenceType, referenceType)
                .and()
                .equal(Answer_.referenceId, referenceId)
                .build()
                .remove();

        return Completable.complete();
    }

    @Override
    public int size(@NonNull String referenceType, @NonNull String referenceId) {
        return (int) box.query().equal(Answer_.referenceType, referenceType)
                .and()
                .equal(Answer_.referenceId, referenceId).build().count();
    }
}
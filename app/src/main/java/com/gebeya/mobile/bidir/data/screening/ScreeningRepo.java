package com.gebeya.mobile.bidir.data.screening;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.answer.local.AnswerLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.screening.local.ScreeningLocalSource;
import com.gebeya.mobile.bidir.data.screening.remote.ScreeningRemoteSource;
import com.gebeya.mobile.bidir.data.screening.remote.ScreeningResponse;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Screening repository contract implementation
 */
public class ScreeningRepo implements ScreeningRepoSource {

    @Inject ScreeningLocalSource local;
    @Inject ScreeningRemoteSource remote;

    @Inject AnswerLocalSource answerLocal;

    public ScreeningRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable pushQuestions(@NonNull String screeningId) {
        final ScreeningResponse response = new ScreeningResponse();

        return local.get(screeningId)
                .flatMap(data -> {
                    response.screening = data.get();
                    return answerLocal.getAll(QAHelper.REF_TYPE_SCREENING, screeningId);
                })
                .flatMap(answers -> {
                    response.answers = answers;
                    return remote.update(response);
                })
                .flatMapCompletable(screening -> {
                    local.put(screening);
                    return Completable.complete();
                });
    }

    /*        return Observable         // TODO: I will fix you
                .zip(userLocal.first(), local.getByType(screeningId), (userData, screeningData) -> {
                    return remote.submitCropACAT(userData.getByType(), screeningData.getByType(), status, remark);
                }).flatMapCompletable(screeningObservable -> local.put(screeningObservable.));
    */

    @Override
    public Completable pushStatus(@NonNull Screening screening, @NonNull String status, @Nullable String remark) {
        return remote.updateApiStatus(screening, status, remark)
                .flatMapCompletable(apiScreening -> {
                    local.put(apiScreening);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<Data<Screening>> first() {
        return local.first();
    }

    @Override
    public Observable<Data<Screening>> get(@NonNull String id) {
        return local.get(id);
    }

    @Override
    public Observable<Data<Screening>> get(int position) {
        return local.get(position);
    }

    @Override
    public Observable<List<Screening>> getAll() {
        return local.getAll();
    }

    @Override
    public Observable<List<Screening>> fetchAll() {
        return local.getAll()
                .flatMap(screenings ->
                        screenings.isEmpty() ? fetchForceAll() : Observable.just(screenings)
                );
    }

    @Override
    public Observable<List<Screening>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
                    final List<Screening> screenings = new ArrayList<>();
                    for (ScreeningResponse response : responses) {
                        final Screening screening = response.screening;
                        screenings.add(screening);
                        answerLocal.putAll(response.answers, QAHelper.REF_TYPE_SCREENING, screening._id);
                    }
                    local.putAll(screenings);
                    return Observable.just(screenings);
                });
    }

    @Override
    public Observable<Screening> fetch(@NonNull String id) {
        return local.get(id)
                .flatMap(data ->
                        data.empty() ? fetchForce(id) : Observable.just(data.get())
                );
    }

    @Override
    public Observable<Screening> fetchForce(@NonNull String id) {
        return remote.getOne(id)
                .flatMap(response -> {
                    answerLocal.putAll(response.answers, QAHelper.REF_TYPE_SCREENING, response.screening._id);
                    return local.put(response.screening);
                });
    }

    @Override
    public Completable remove(int position) {
        return local.remove(position);
    }

    @Override
    public Completable remove(@NonNull String id) {
        return local.remove(id);
    }

    @Override
    public int size() {
        return local.size();
    }
}
package com.gebeya.mobile.bidir.data.acatform.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.gebeya.mobile.bidir.data.acatform.ACATForm_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/5/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATFormFormLocal extends BaseLocalSource implements ACATFormLocalSource {

    private final Box<ACATForm> box;

    public ACATFormFormLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ACATForm.class);
    }

    @Override
    public Observable<Data<ACATForm>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ACATForm>> get(int position) {
        final List<ACATForm> acatForms = box.getAll();
        return Observable.just(new Data<>(acatForms.isEmpty() ? null : acatForms.get(position)));
    }

    @Override
    public Observable<Data<ACATForm>> get(@NonNull String id) {
        final List<ACATForm> acatForms = box.find(ACATForm_._id, id);
        return Observable.just(new Data<>(acatForms.isEmpty() ? null : acatForms.get(0)));
    }

    @Override
    public Observable<List<ACATForm>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<ACATForm> put(@NonNull ACATForm acatForm) {
        box.query()
                .equal(ACATForm_._id, acatForm._id)
                .build()
                .remove();
        box.put(acatForm);
        return Observable.just(acatForm);
    }

    @Override
    public Observable<List<ACATForm>> putAll(@NonNull List<ACATForm> acatForms) {
        box.put(acatForms);
        return Observable.just(acatForms);
    }
}

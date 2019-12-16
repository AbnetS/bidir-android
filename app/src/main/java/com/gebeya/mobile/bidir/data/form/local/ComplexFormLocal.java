package com.gebeya.mobile.bidir.data.form.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.gebeya.mobile.bidir.data.form.ComplexForm_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Implementation for the {@link ComplexFormLocalSource} source.
 */
public class ComplexFormLocal extends BaseLocalSource implements ComplexFormLocalSource {

    private final Box<ComplexForm> box;

    public ComplexFormLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ComplexForm.class);
    }

    @Override
    public Observable<Data<ComplexForm>> getByType(@NonNull String type) {
        final List<ComplexForm> forms = box.find(ComplexForm_.type, type);
        return Observable.just(new Data<>(forms.isEmpty() ? null : forms.get(0)));
    }

    @Override
    public Observable<List<ComplexForm>> getAll() {
        final List<ComplexForm> forms = box.getAll();
        return Observable.just(forms);
    }

    @Override
    public Observable<Data<ComplexForm>> get(@NonNull String id) {
        final List<ComplexForm> forms = box.find(ComplexForm_._id, id);
        return Observable.just(new Data<>(forms.isEmpty() ? null : forms.get(0)));
    }

    @Override
    public Observable<ComplexForm> put(@NonNull ComplexForm form) {
        box.query().equal(ComplexForm_.type, form.type).build().remove();
        box.put(form);
        return Observable.just(form);
    }

    @Override
    public Observable<Data<ComplexForm>> get(int position) {
        throw new RuntimeException("Use either getByType(String id) or getByType(String type)");
    }

    @Override
    public Observable<Data<ComplexForm>> first() {
        throw new RuntimeException("Use either getByType(String id) or getByType(String type)");
    }
}
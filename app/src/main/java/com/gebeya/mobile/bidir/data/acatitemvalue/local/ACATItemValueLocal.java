package com.gebeya.mobile.bidir.data.acatitemvalue.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link ACATItemValueLocalSource} interface.
 */
public class ACATItemValueLocal extends BaseLocalSource implements ACATItemValueLocalSource {

    private final Box<ACATItemValue> box;

    public ACATItemValueLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ACATItemValue.class);
    }

    @Override
    public Observable<Data<ACATItemValue>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ACATItemValue>> get(int position) {
        final List<ACATItemValue> acatItemValues = box.getAll();
        return Observable.just(new Data<>(acatItemValues.isEmpty() ? null : acatItemValues.get(position)));
    }

    @Override
    public Observable<Data<ACATItemValue>> get(@NonNull String id) {
        final List<ACATItemValue> acatItemValues = box.find(ACATItemValue_.acatItemId, id);
        return Observable.just(new Data<>(acatItemValues.isEmpty() ? null : acatItemValues.get(0)));
    }

    @Override
    public Observable<Data<ACATItemValue>> getByType(@NonNull String acatItemId, @NonNull String type) {
        final List<ACATItemValue> acatItemValues = box.query()
                .equal(ACATItemValue_.acatItemId, acatItemId)
                .and()
                .equal(ACATItemValue_.type, type)
                .build().find();
        return Observable.just(new Data<>(acatItemValues.isEmpty() ? null : acatItemValues.get(0)));
    }

    @Override
    public Observable<List<ACATItemValue>> getAllByACATItemId(@NonNull String acatItemId) {
        final List<ACATItemValue> acatItemValues = box.getAll();
        return Observable.just(acatItemValues);
    }

    @Override
    public Observable<List<ACATItemValue>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<ACATItemValue> put(@NonNull ACATItemValue acatItemValue) {
        box.query().equal(ACATItemValue_.acatItemId, acatItemValue.acatItemId)
                .and()
                .equal(ACATItemValue_.type, acatItemValue.type)
                .build()
                .remove();
        acatItemValue.id = 0;
        box.put(acatItemValue);
        return Observable.just(acatItemValue);
    }

    @Override
    public Observable<List<ACATItemValue>> putAll(@NonNull List<ACATItemValue> acatItemValues) {
        box.removeAll();
        box.put(acatItemValues);
        return Observable.just(acatItemValues);
    }

    @Override
    public Observable<ACATItemValue> putNewACATItemValue(@NonNull ACATItemValue acatItemValue) {
        box.put(acatItemValue);
        return Observable.just(acatItemValue);
    }

    @Override
    public Observable<ACATItemValue> updateACATItemRefId(@NonNull ACATItemValue acatItemValue, String acatItemId) {
        ACATItemValue fetched = box.get(acatItemValue.id);
        box.remove(fetched.id); //remove the item
        fetched.acatItemId = acatItemId; //Update the ACAT Item Id with the API Id

        box.put(fetched);
        return Observable.just(fetched);
    }
}

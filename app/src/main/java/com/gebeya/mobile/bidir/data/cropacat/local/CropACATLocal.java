package com.gebeya.mobile.bidir.data.cropacat.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.crop.Crop;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATComparator;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT_;
import com.gebeya.mobile.bidir.data.cropacat.remote.CropACATParser;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */
public class CropACATLocal extends BaseLocalSource implements CropACATLocalSource {
    private final Box<CropACAT> box;

    public CropACATLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(CropACAT.class);
    }

    @Override
    public Observable<Data<CropACAT>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<CropACAT>> get(int position) {
        final List<CropACAT> cropACATs = box.getAll();
        return Observable.just(new Data<>(cropACATs.isEmpty() ? null : cropACATs.get(position)));
    }

    @Override
    public Observable<Data<CropACAT>> get(@NonNull String id) {
        final List<CropACAT> cropACATs = box.find(CropACAT_._id, id);
        return Observable.just(new Data<>(cropACATs.isEmpty() ? null : cropACATs.get(0)));
    }

    @Override
    public Observable<List<CropACAT>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<CropACAT> markForUpload(@NonNull CropACAT cropACAT) {
        cropACAT.modified = true;
        cropACAT.uploaded = false;
        cropACAT.updatedAt = new DateTime();

        put(cropACAT);

        return Observable.just(cropACAT);
    }

    @Override
    public Observable<CropACAT> put(@NonNull CropACAT cropACAT) {
        box.query().equal(CropACAT_._id, cropACAT._id)
                .build()
                .remove();

        cropACAT.id = 0;
        box.put(cropACAT);
        return Observable.just(cropACAT);
    }

    @Override
    public Observable<CropACAT> putNew(@NonNull CropACAT cropACAT) {
        box.put(cropACAT);
        return Observable.just(cropACAT);
    }

    @Override
    public Observable<List<CropACAT>> putAll(@NonNull List<CropACAT> cropACATs) {
        box.removeAll();
        box.put(cropACATs);
        return Observable.just(cropACATs);
    }

    @Override
    public Observable<List<CropACAT>> getClientACATCrop(@NonNull String clientId) {
        final List<CropACAT> cropACATs = box.query()
                .equal(CropACAT_.clientID, clientId)
                .and()
                .notEqual(CropACAT_.status, CropACATParser.STATUS_LOCAL_LOAN_PAID)
                .sort(new CropACATComparator())
                .build()
                .find();

        return Observable.just(cropACATs);
    }

    @Override
    public Observable<Data<CropACAT>> getClientCropItem(@NonNull String clientId, @NonNull String cropId) {
        final List<CropACAT> cropACATS = box.query()
                .equal(CropACAT_.cropID, cropId)
                .and()
                .equal(CropACAT_.clientID, clientId)
                .sort(new CropACATComparator())
                .build()
                .find();
        return Observable.just(new Data<>(cropACATS.isEmpty() ? null : cropACATS.get(0)));
    }

    @Override
    public Observable<Data<CropACAT>> getACATCrop(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        final List<CropACAT> cropACATS = box.query()
                .equal(CropACAT_._id, cropACATId)
                .and()
                .equal(CropACAT_.acatApplicationID, acatApplicationId)
                .sort(new CropACATComparator())
                .build()
                .find();

        return Observable.just(new Data<>(cropACATS.isEmpty() ? null : cropACATS.get(0)));
    }

    @Override
    public Observable<List<CropACAT>> getCropACATByApp(@NonNull String acatApplicationId){
        final List<CropACAT> cropACATs = box.query()
                .equal(CropACAT_.acatApplicationID, acatApplicationId)
                .sort(new CropACATComparator())
                .build()
                .find();

        return Observable.just(cropACATs);
    }

    @Override
    public Observable<Data<CropACAT>> getActiveClientCropACAT(@NonNull String clientId, @NonNull String acatApplicationId) {
        final List<CropACAT> activeCropACATs = box.query()
                .equal(CropACAT_.clientID, clientId)
                .and()
                .equal(CropACAT_.acatApplicationID, acatApplicationId)
                .and()
                .equal(CropACAT_.active, true)
                .and()
                .equal(CropACAT_.complete, false)
                .build().find();


        if (!activeCropACATs.isEmpty()) {
            return Observable.just(new Data<>(activeCropACATs.get(0)));
        }


        final List<CropACAT> cropACATS = box.query()
                .equal(CropACAT_.clientID, clientId)
                .and()
                .equal(CropACAT_.acatApplicationID, acatApplicationId)
                .and()
                .equal(CropACAT_.complete, false)
                .build()
                .find();

        if (cropACATS.isEmpty()) return Observable.just(new Data<>(null));

        final CropACAT cropACAT = cropACATS.get(0);
        cropACAT.active = true;
        put(cropACAT);

        return Observable.just(new Data<>(cropACAT));
    }
}

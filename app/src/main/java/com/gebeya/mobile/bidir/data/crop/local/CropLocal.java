package com.gebeya.mobile.bidir.data.crop.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.crop.Crop;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.crop.Crop_;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Local Concrete ACAT Crop source.
 */

public class CropLocal extends BaseLocalSource implements CropLocalSource {

    private final Box<Crop> box;

    public CropLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Crop.class);
    }

    @Override
    public Observable<Crop> putOne(@NonNull Crop crop, @NonNull String acatId) {
        box.query()
                .equal(Crop_.acatId, acatId)
                .build().remove();
        box.put(crop);
        return Observable.just(crop);
    }

    @Override
    public Observable<List<Crop>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<Crop> getOne(@NonNull String acatId) {
        final List<Crop> crops = box.query()
                .equal(Crop_.acatId, acatId)
                .build()
                .find();
        return Observable.just(crops.get(0));
    }

    @Override
    public Observable<Crop> put(@NonNull Crop crop) {
        box.query()
                .equal(Crop_._id, crop._id)
                .build()
                .remove();

        box.put(crop);
        return Observable.just(crop);
    }

    @Override
    public int size() {
        return (int) box.count();
    }
}

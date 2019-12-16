package com.gebeya.mobile.bidir.data.gpslocation.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */

public class GPSLocationLocal extends BaseLocalSource implements GPSLocationLocalSource {
    private final Box<GPSLocation> box;

    public GPSLocationLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(GPSLocation.class);
    }

    @Override
    public Observable<Data<GPSLocation>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<GPSLocation>> get(int position) {
        final List<GPSLocation> gpsLocations = box.getAll();
        return Observable.just(new Data<>(gpsLocations.isEmpty() ? null : gpsLocations.get(position)));
    }

    @Override
    public Observable<Data<GPSLocation>> get(@NonNull final String id) {
        return null;
    }

    @Override
    public Observable<List<GPSLocation>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<GPSLocation> put(@NonNull GPSLocation item) {
        box.query().equal(GPSLocation_.acatApplicationID, item.acatApplicationID)
                .and()
                .equal(GPSLocation_.cropACATID, item.cropACATID)
                .build()
                .remove();
        box.put(item);
        return Observable.just(item);
    }



    @Override
    public Observable<List<GPSLocation>> putAll(@NonNull List<GPSLocation> items) {
        box.removeAll();
        box.put(items);
        return Observable.just(items);
    }

    @Override
    public Completable putAll(@NonNull List<GPSLocation> locations, @NonNull String cropACATId, @NonNull String acatApplicationId) {
        box.query()
                .equal(GPSLocation_.cropACATID, cropACATId)
                .and()
                .equal(GPSLocation_.acatApplicationID, acatApplicationId)
                .build()
                .remove();
        for(GPSLocation location : locations) {
            location.id = 0;
            box.put(location);
        }

        //box.put(locations);
        return Completable.complete();
    }

    @Override
    public Observable<GPSLocation> getSingleCropACATGps(@NonNull String cropACATId) {
        List<GPSLocation> locations = box.query().equal(GPSLocation_.cropACATID, cropACATId)
                .build()
                .find();

        return Observable.just(locations.get(0));
    }

    @Override
    public Observable<List<GPSLocation>> getAllCropACATGPSLocations(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        final List<GPSLocation> locations = box.query()
                .equal(GPSLocation_.cropACATID, cropACATId)
                .and()
                .equal(GPSLocation_.acatApplicationID, acatApplicationId)
                .build()
                .find();
        return Observable.just(locations);
    }
}
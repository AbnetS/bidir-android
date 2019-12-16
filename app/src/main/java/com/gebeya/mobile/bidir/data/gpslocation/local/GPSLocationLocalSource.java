package com.gebeya.mobile.bidir.data.gpslocation.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;

import java.util.List;

import javax.annotation.Nonnegative;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */

public interface GPSLocationLocalSource extends ReadableSource<GPSLocation>, WritableSource<GPSLocation> {
    Observable<GPSLocation> getSingleCropACATGps(@NonNull String cropACATId);
    Observable<List<GPSLocation>> getAllCropACATGPSLocations(@NonNull String cropACATId, @NonNull String acatApplicationId);
    Completable putAll(@NonNull List<GPSLocation> locations, @NonNull String cropACATId, @NonNull String acatApplicationId);
}

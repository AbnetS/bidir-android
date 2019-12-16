package com.gebeya.mobile.bidir.data.cropacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.CropACATRequest;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableRange;

/**
 * Created by Samuel K. on 6/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface CropACATRemoteSource {

    Observable<CropACATResponse> updateCropACATInstance(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull CropACATRequest request);

    Observable<CropACATResponse> updateProgressStatus(@NonNull String cropACATId, @NonNull String acaApplicationId, @NonNull String status);

    Observable<CropACATResponse> updateApiStatus(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull String status);

    Observable<CropACATResponse> registerGPS(@NonNull CropACATRequest request);

    Observable<CropACATResponse> getCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId);

}

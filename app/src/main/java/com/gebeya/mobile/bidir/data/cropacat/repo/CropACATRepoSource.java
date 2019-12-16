package com.gebeya.mobile.bidir.data.cropacat.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.cropacat.CropACAT;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 6/8/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface CropACATRepoSource {
    Completable updateCropACATInstance(@NonNull String cropACATId, @NonNull String acatApplicationId);

    Observable<CropACAT> updateProgress(@NonNull String cropACATId, @NonNull String acatApplicationId);

    Observable<Boolean> changeCropACATStatus(@NonNull String cropACATId, @NonNull String acatApplicationId, @NonNull String status);

    Observable<Boolean> submitCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId);

    Observable<Boolean> approveCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId);

    Observable<Boolean> declineCropACAT(@NonNull String cropACATId, @NonNull String acatApplicationId);


}

package com.gebeya.mobile.bidir.data.cropacat.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.impl.rx.Data;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */

public interface CropACATLocalSource extends ReadableSource<CropACAT>, WritableSource<CropACAT> {

    Observable<List<CropACAT>> getClientACATCrop(@NonNull String clientId);

    Observable<Data<CropACAT>> getClientCropItem(@NonNull String clientId, @NonNull String cropId);

    Observable<Data<CropACAT>> getACATCrop(@NonNull String cropId, @NonNull String acatApplicationId);

    Observable<List<CropACAT>> getCropACATByApp(@NonNull String acatApplicationId);

    /**
     * Attempts to return a possibly active crop ACAT that is currently being worked on, belonging
     * to the given client.
     * <p>
     * If the client has no active crop ACAT, an attempt to mark and return one as active is done.
     *
     * @param clientId Client ID to use as the search.
     * @return CropACAT that could be active.
     */
    Observable<Data<CropACAT>> getActiveClientCropACAT(@NonNull String clientId, @NonNull String cropACATId);

    Observable<CropACAT> markForUpload(@NonNull CropACAT cropACAT);

    Observable<CropACAT> putNew(@NonNull CropACAT cropACAT);
}
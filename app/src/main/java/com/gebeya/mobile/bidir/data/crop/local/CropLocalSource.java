package com.gebeya.mobile.bidir.data.crop.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteOne;
import com.gebeya.mobile.bidir.data.crop.Crop;

import java.util.List;

import io.reactivex.Observable;


/**
 * Interface representing a local source of an ACAT Crop.
 */

public interface CropLocalSource extends WriteOne<Crop> {

    /**
     * Save {@link Crop} object, for a specific ACAT.
     * @param crop crop item to be saved.
     * @param acatId ACAT Id in which this crop belongs to.
     * @return true if crop is saved, false otherwise.
     */
    Observable<Crop> putOne(@NonNull Crop crop, @NonNull String acatId);

    /**
     * Get a list of all the {@link Crop} objects belonging to different ACATs.
     * @return list of all the Crops saved.
     */
    Observable<List<Crop>> getAll();

    /**
     * Get a Crop of a specific ACAT.
     * @param acatId ACAT Id which act as a filter.
     * @return
     */
    Observable<Crop> getOne(@NonNull String acatId);

    /**
     * Get the size of Crops available.
     *
     * @return size of the crops.
     */
    int size();

}

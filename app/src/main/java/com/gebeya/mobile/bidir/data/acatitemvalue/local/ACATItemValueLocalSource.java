package com.gebeya.mobile.bidir.data.acatitemvalue.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.impl.rx.Data;

import java.util.List;

import javax.annotation.Nonnegative;

import io.reactivex.Observable;

/**
 * Contract class for a local source for {@link com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource} objects.
 */

public interface ACATItemValueLocalSource extends ReadableSource<ACATItemValue>, WritableSource<ACATItemValue> {

    /**
     * Get the given ACAT Item value object belonging to the given ACAT item ID, of the given type.
     *
     * @param acatItemId ACAT Item ID to which the value belongs to.
     * @param type       The type of the value (ESTIMATED or ACTUAL).
     * @return Observable item of the found value, if any.
     */
    Observable<Data<ACATItemValue>> getByType(@NonNull String acatItemId, @NonNull String type);

    /**
     * Get a list of all the ACAT Item values belonging to the given ACAT Item ID, regardless of the type.
     *
     * @param acatItemId ACAT Item ID to which the values should belong to.
     * @return Observable list of items of the found values, if any.
     */
    Observable<List<ACATItemValue>> getAllByACATItemId(@NonNull String acatItemId);

    /**
     * Put ACAT Item values for the newly created ACAT Item.
     * @param acatItemValue ACAT Item value to be saved.
     * @return ACAT Item Value.
     */
    Observable<ACATItemValue> putNewACATItemValue(@NonNull ACATItemValue acatItemValue);

    Observable<ACATItemValue> updateACATItemRefId(@NonNull ACATItemValue acatItemValue, String acatItemId);
}
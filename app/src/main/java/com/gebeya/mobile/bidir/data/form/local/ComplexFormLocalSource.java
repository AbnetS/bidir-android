package com.gebeya.mobile.bidir.data.form.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadMany;
import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteOne;
import com.gebeya.mobile.bidir.data.form.ComplexForm;
import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Observable;

/**
 * Contract interface definition for local sources to work on {@link ComplexForm}
 */
public interface ComplexFormLocalSource extends WriteOne<ComplexForm>, ReadableSource<ComplexForm> {

    /**
     * Get a complex form by the given type.
     *
     * @param type String type to use as the lookup.
     * @return ComplexForm found, inside an observable.
     */
    Observable<Data<ComplexForm>> getByType(@NonNull String type);
}
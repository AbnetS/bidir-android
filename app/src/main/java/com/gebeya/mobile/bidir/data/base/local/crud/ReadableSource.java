package com.gebeya.mobile.bidir.data.base.local.crud;

import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadMany;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadOne;

/**
 * Represents read operations for single and many T items.
 */
public interface ReadableSource<T> extends ReadOne<T>, ReadMany<T> {

}

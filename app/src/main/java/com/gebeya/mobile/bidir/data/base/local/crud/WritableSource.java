package com.gebeya.mobile.bidir.data.base.local.crud;

import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteMany;
import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteOne;

/**
 * Represents write operations for single and many T items.
 */
public interface WritableSource<T> extends WriteOne<T>, WriteMany<T> {

}
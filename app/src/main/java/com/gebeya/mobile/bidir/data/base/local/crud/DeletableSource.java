package com.gebeya.mobile.bidir.data.base.local.crud;

import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteMany;
import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteOne;

/**
 * Represents delete operations for single and many T items source.
 */
public interface DeletableSource extends DeleteOne, DeleteMany {
}
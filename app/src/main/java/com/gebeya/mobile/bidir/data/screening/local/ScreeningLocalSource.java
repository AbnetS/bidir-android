package com.gebeya.mobile.bidir.data.screening.local;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.screening.Screening;

/**
 * Contract for Screening objects local source
 */
public interface ScreeningLocalSource extends
        WritableSource<Screening>,
        ReadableSource<Screening>,
        DeleteOne,
        ReadSize {

}
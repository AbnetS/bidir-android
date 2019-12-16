package com.gebeya.mobile.bidir.data.groupedacat.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.DeletableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Observable;

public interface GroupedACATLocalSource extends
        ReadableSource<GroupedACAT>,
        WritableSource<GroupedACAT>,
        DeletableSource,
        ReadSize {

    Observable<Data<GroupedACAT>> getByGroupId(@NonNull String groupId);

}

package com.gebeya.mobile.bidir.data.groupedcomplexscreening.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.DeletableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Observable;

public interface GroupedComplexScreeningLocalSource extends
        ReadableSource<GroupedComplexScreening>,
        WritableSource<GroupedComplexScreening>,
        DeletableSource,
        ReadSize {

    Observable<Data<GroupedComplexScreening>> getByGroupId(@NonNull String groupId);
}

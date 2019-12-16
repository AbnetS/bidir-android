package com.gebeya.mobile.bidir.data.groupedcomplexloan.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.DeletableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Observable;

public interface GroupedComplexLoanLocalSource extends
        ReadableSource<GroupedComplexLoan>,
        WritableSource<GroupedComplexLoan>,
        DeletableSource,
        ReadSize{

    Observable<Data<GroupedComplexLoan>> getByGroupId(@NonNull String groupId);
}

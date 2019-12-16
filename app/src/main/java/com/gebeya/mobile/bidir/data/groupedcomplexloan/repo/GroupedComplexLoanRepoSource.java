package com.gebeya.mobile.bidir.data.groupedcomplexloan.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;

import io.reactivex.Completable;
import io.reactivex.Observable;


public interface GroupedComplexLoanRepoSource extends
        FetchMany<GroupedComplexLoan>,
        FetchOne<GroupedComplexLoan>,
        ReadSize {

    Observable<GroupedComplexLoan> getById(@NonNull String groupId);

    Observable<GroupedComplexLoan> submitGroup(@NonNull String groupId);

    Observable<GroupedComplexLoan> approveGroup(@NonNull String groupId);

    Observable<GroupedComplexLoan> updateStatus(@NonNull String groupId);

    Observable<GroupedComplexLoan> create(@NonNull String groupId);

}

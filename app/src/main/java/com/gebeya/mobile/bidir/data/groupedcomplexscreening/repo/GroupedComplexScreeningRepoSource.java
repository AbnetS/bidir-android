package com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreeningResponse;

import java.util.List;

import io.reactivex.Observable;

public interface GroupedComplexScreeningRepoSource extends
        FetchMany<GroupedComplexScreening>,
        FetchOne<GroupedComplexScreening>,
        ReadSize{

    Observable<GroupedComplexScreening> getById(@NonNull String groupId);

    Observable<GroupedComplexScreening> submitGroup(@NonNull String groupId);

    Observable<GroupedComplexScreening> approveGroup(@NonNull String groupId);

    Observable<GroupedComplexScreening> updateStatus(@NonNull String groupId);

    Observable<GroupedComplexScreening> createNewLoanCycle(@NonNull String groupId, @NonNull String loanAmount);
    
}

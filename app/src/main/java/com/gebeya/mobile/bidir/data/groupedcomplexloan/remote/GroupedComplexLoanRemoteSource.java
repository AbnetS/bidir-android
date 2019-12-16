package com.gebeya.mobile.bidir.data.groupedcomplexloan.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComlexLoanResponse;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

public interface GroupedComplexLoanRemoteSource {

    Observable<GroupedComlexLoanResponse> download(@NonNull String groupId);

    Observable<List<GroupedComplexLoan>> getAll();

    Observable<GroupedComplexLoan> submitGroupLoan(@NonNull String groupId);

    Observable<GroupedComplexLoan> approveGroupLoan(@NonNull String groupId);

    Observable<GroupedComplexLoan> updateStatus(@NonNull String groupId);

    Observable<GroupedComplexLoan> create(@NonNull String groupId);

    GroupedComlexLoanResponse parseResponse(@NonNull JsonObject object) throws Exception;
}

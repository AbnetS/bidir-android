package com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningRequest;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreeningResponse;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreeningResponse;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;


public interface GroupedComplexScreeningRemoteSource {

    Observable<List<GroupedComplexScreening>> getAll();

    Observable<GroupedComplexScreening> download(@NonNull String groupId);

    Observable<GroupedComplexScreening> submitGroupScreening(@NonNull String groupId);

    Observable<GroupedComplexScreening> approveGroupScreening(@NonNull String groupId);

    Observable<GroupedComplexScreening> updateStatus(@NonNull String groupId);

    Observable<GroupedComplexScreeningResponse> createNewLoanCycle(@NonNull String groupId, @NonNull String loanAmount);

    GroupedComplexScreeningResponse parseResponse(@NonNull JsonObject object) throws Exception;

}

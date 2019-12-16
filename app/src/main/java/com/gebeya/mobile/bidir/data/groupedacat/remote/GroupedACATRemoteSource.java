package com.gebeya.mobile.bidir.data.groupedacat.remote;


import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface GroupedACATRemoteSource {

    Observable<List<GroupedACAT>> getAll();

    Observable<GroupedACAT> download(@NonNull String groupId);

    Observable<GroupedACAT> submitGroupACAT(@NonNull String groupId);

    Observable<GroupedACAT> approveGroupACAT(@NonNull String groupId);

    Observable<GroupedACAT> updateStatus(@NonNull String groupId);

    Observable<GroupedACAT> create(@NonNull String groupId);

    Observable<ACATApplicationResponse> initializeMemberACAT(@NonNull String groupId, @NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs);
}

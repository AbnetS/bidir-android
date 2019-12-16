package com.gebeya.mobile.bidir.data.groupedacat.repo;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;


public interface GroupedACATRepoSource extends
        FetchMany<GroupedACAT>,
        FetchOne<GroupedACAT>,
        ReadSize {

    Observable<GroupedACAT> getById(@NonNull String groupId);

    Observable<GroupedACAT> submitGroup(@NonNull String groupId);

    Observable<GroupedACAT> approveGroup(@NonNull String groupId);

    Observable<GroupedACAT> updateStatus(@NonNull String groupId);

    Observable<GroupedACAT> create(@NonNull String groupId);

    Completable initializeMember(@NonNull String groupId, @NonNull Client client, @NonNull LoanProduct loanProduct, @NonNull List<String> cropACATs);

}

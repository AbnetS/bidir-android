package com.gebeya.mobile.bidir.data.acatapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationSync;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.impl.rx.Data;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ACATApplicationSyncLocalSource extends ReadableSource<ACATApplicationSync>, WritableSource<ACATApplicationSync> {
    List<ACATApplicationSync> getAllModifiedACATApplication();
    Observable<Boolean> hasUnSyncedData();
    Observable<ACATApplicationSync> markForUpload(@NonNull String id);
    Observable<Data<ACATApplicationSync>> getByClientId(@NonNull String clientId);
    Completable remove(@NonNull String clientId);
    Completable removeAll(List<String> membersId);
}

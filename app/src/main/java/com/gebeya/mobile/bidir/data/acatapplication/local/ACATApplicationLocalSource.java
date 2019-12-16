package com.gebeya.mobile.bidir.data.acatapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by abuti on 5/17/2018.
 */

public interface ACATApplicationLocalSource extends ReadableSource<ACATApplication>, WritableSource<ACATApplication> {

    Observable<Data<ACATApplication>> getACATByClient(@NonNull String clientId);

    Observable<Data<ACATApplication>> getLoanAuthorizedClient(@NonNull String clientId);

    Observable<ACATApplication> markForUpload(@NonNull ACATApplication acatApplication);

    Completable remove(@NonNull String clientId);

    Completable updateWithLocalIds(List<ACATApplication> remoteACATApplications);

}

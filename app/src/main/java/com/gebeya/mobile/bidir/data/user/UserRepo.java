package com.gebeya.mobile.bidir.data.user;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.data.user.remote.UserRemoteSource;
import com.gebeya.mobile.bidir.impl.util.location.preference.PreferenceHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Implementation for the {@link UserRepoSource} contract.
 */
public class UserRepo implements UserRepoSource {

    @Inject UserLocalSource local;
    @Inject UserRemoteSource remote;
    @Inject PreferenceHelper pref;
    @Inject PermissionLocalSource permissionLocal;

    public UserRepo() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Override
    public Observable<User> login(@NonNull String username, @NonNull String password) {
        return remote
                .login(username, password)
                .flatMap(response -> {
                    local.put(response.user);
                    permissionLocal.putAll(response.permissions);
                    return Observable.just(response.user);
                });
    }

    @Override
    public Completable logout() {
        pref.clearSharedPreference();
        return local.removeAll();
    }
}
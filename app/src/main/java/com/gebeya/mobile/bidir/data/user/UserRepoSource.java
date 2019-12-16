package com.gebeya.mobile.bidir.data.user;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Repository contract for the User.
 */
public interface UserRepoSource {
    /**
     * Login a user.
     *
     * @param username username value.
     * @param password password value.
     * @return Observable representing the logged in user.
     */
    Observable<User> login(@NonNull String username, @NonNull String password);

    /**
     * Logout a user. The logout process only destroys all the local data, and nothing on the
     * API level.
     *
     * @return Completable representing the logout operation.
     */
    Completable logout();
}
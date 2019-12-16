package com.gebeya.mobile.bidir.data.user.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.user.User;
import com.google.gson.JsonObject;

import io.reactivex.Observable;

/**
 * Interface definition for the User API connectivity.
 */
public interface UserRemoteSource {
    /**
     * Login a user.
     *
     * @param username username value
     * @param password password value
     * @return Observable object containing the parsed {@link User} and list of permissions,
     * in a {@link UserResponse} object.
     */
    Observable<UserResponse> login(@NonNull String username, @NonNull String password);

    /**
     * Parse a given JsonObject object into a {@link UserResponse}.
     *
     * @param object JsonObject argument to parseResponse from.
     * @return UserResponse parsed
     * @throws Exception thrown, if parsing failed
     */
    UserResponse parse(@NonNull JsonObject object) throws Exception;

    /**
     * Parse a given JsonObject into a {@link Permission} object, needed for various operations.
     *
     * @param object JsonObject argument to parseResponse from.
     * @return parsed Permission object.
     * @throws Exception thrown, if parsing failed.
     */
    Permission parsePermission(@NonNull JsonObject object) throws Exception;
}
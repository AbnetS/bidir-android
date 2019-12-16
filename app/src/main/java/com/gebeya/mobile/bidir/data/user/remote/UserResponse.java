package com.gebeya.mobile.bidir.data.user.remote;

import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.user.User;

import java.util.List;

/**
 * Contains the pageResponse from the API, containing both the parsed
 * {@link com.gebeya.mobile.bidir.data.user.User} and the list of {@link Permission} objects.
 * <p>
 * samkura47@gmail.com
 */
public class UserResponse {
    public User user;
    public List<Permission> permissions;
}
package com.gebeya.mobile.bidir.data.user.remote;

import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.ApiResponses;
import com.gebeya.mobile.bidir.data.base.remote.ResponseLoader;
import com.google.gson.JsonObject;

/**
 * Helper class for providing JSON user API responses back to the testing component
 */
public final class UserApiResponses {

    public static Throwable getLoginResponseInvalidUsername() {
        return ApiResponses.createError(ApiErrors.LOGIN_PART_INVALID_USERNAME_PASSWORD);
    }

    public static Throwable getLoginResponseInvalidPassword() {
        return ApiResponses.createError(ApiErrors.LOGIN_PART_INVALID_PASSWORD);
    }

    public static JsonObject getLoginResponseSuccess() throws Exception {
        return ResponseLoader.openObject("user/user-login-success.json");
    }

    public static JsonObject getLoginResponseInvalid() throws Exception {
        return ResponseLoader.openObject("user/user-login-invalid.json");
    }

    public static JsonObject getLoginResponseInvalidPermissionEntity() throws Exception {
        return ResponseLoader.openObject("user/permission/permission-invalid-entity.json");
    }

    public static JsonObject getLoginResponseInvalidPermissionOperation() throws Exception {
        return ResponseLoader.openObject("user/permission/permission-invalid-operation.json");
    }
}

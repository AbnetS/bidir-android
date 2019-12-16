package com.gebeya.mobile.bidir.data.user.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link UserRemoteSource} interface contract.
 */
public class UserRemote extends BaseRemoteSource<UserService> implements UserRemoteSource {

    @Inject PermissionHelper permissionHelper;

    public UserRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.AUTH, UserService.class);
    }

    @Override
    public Observable<UserResponse> login(final @NonNull String username, final @NonNull String password) {
        final Map<String, String> request = new HashMap<>();

        request.put("username", username);
        request.put("password", password);

        return build().call(service.login(request)).map(this::parse);
    }

    @Override
    public UserResponse parse(@NonNull JsonObject object) throws Exception {
        try {
            UserResponse response = new UserResponse();

            User user = new User();
            user.token = object.get("token").getAsString();

            JsonObject userObject = object.getAsJsonObject("user");
            user._id = userObject.get("_id").getAsString();
            user.username = userObject.get("username").getAsString();
            user.status = userObject.get("status").getAsString();

            String branchId;

            JsonObject accountObject = userObject.getAsJsonObject("account");
            JsonObject defaultBranchObject = accountObject.getAsJsonObject("default_branch");
            branchId = defaultBranchObject.get("_id").getAsString();

            JsonObject role = accountObject.getAsJsonObject("role");
            JsonArray permissionArray = role.getAsJsonArray("permissions");

            int size = permissionArray.size();
            List<Permission> permissionList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Permission permission = parsePermission(permissionArray.get(i).getAsJsonObject());
                if (permission != null) {
                    permissionList.add(permission);
                }
            }
            response.permissions = permissionList;

            user.branchId = branchId;
            response.user = user;

            return response;
        } catch (Exception e) {
            throw new Exception("Error parsing UserResponse: " + e.toString());
        }
    }

    @Override
    public Permission parsePermission(@NonNull JsonObject object) throws Exception {
        try {
            Permission permission = new Permission();

            permission._id = object.get("_id").getAsString();

            final String apiEntity = object.get("entity").getAsString();
            final String localEntity = permissionHelper.getLocalEntity(apiEntity);
            if (localEntity.equals(PermissionHelper.UNKNOWN_ENTITY)) {
                throw new Exception("Unknown API Entity: " + apiEntity);
            }
            permission.entityModule = localEntity;

            final String apiOperation = object.get("operation").getAsString();
            final String localOperation = permissionHelper.getLocalOperation(apiOperation);
            if (localOperation.equals(PermissionHelper.UNKNOWN_OPERATION)) {
                throw new Exception("Unknown API Operation: " + apiOperation);
            }
            permission.operation = localOperation;

            return permission;
        } catch (Exception e) {
            throw new Exception("Error parsing Permission: " + e.toString());
        }
    }
}
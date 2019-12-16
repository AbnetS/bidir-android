package com.gebeya.mobile.bidir.data.permission.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadMany;
import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteMany;
import com.gebeya.mobile.bidir.data.permission.Permission;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract interface for a local storage for the {@link com.gebeya.mobile.bidir.data.permission.Permission}
 * <p>
 * samkura47@gmail.com
 */
public interface PermissionLocalSource extends WriteMany<Permission>, ReadMany<Permission> {

    /**
     * Determines if the given operation under the given entity exists.
     *
     * @param entity    Entity group to look inside
     * @param operation Operation in entity to look for
     * @return Observable indicating if the user has the operation or not
     */
    Observable<Boolean> hasPermission(@NonNull String entity, @NonNull String operation);

    /**
     * Get a list of all the permission with the given entity.
     *
     * @param entity Entity to use as the filter for the permission searching.
     * @return Observable list of the found permissions.
     */
    Observable<List<Permission>> getByEntity(@NonNull String entity);
}

package com.gebeya.mobile.bidir.data.permission.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.Permission_;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implementation of the {@link PermissionLocalSource} contract interface
 * <p>
 * samkura47@gmail.com
 */
public class PermissionLocal extends BaseLocalSource implements  PermissionLocalSource {

    private final Box<Permission> box;

    public PermissionLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Permission.class);
    }

    @Override
    public Observable<List<Permission>> getAll() {
        final List<Permission> permissions = box.getAll();
        return Observable.just(permissions);
    }

    @Override
    public Observable<List<Permission>> getByEntity(@NonNull String entity) {
        final List<Permission> permissions = box.find(Permission_.entityModule, entity);
        return Observable.just(permissions);
    }

    @Override
    public Observable<List<Permission>> putAll(@NonNull List<Permission> permissions) {
        box.removeAll();
        box.put(permissions);
        return Observable.just(permissions);
    }

    @Override
    public Observable<Boolean> hasPermission(@NonNull String entity, @NonNull String operation) {
        long count = box.query()
                .equal(Permission_.entityModule, entity)
                .and()
                .equal(Permission_.operation, operation)
                .build()
                .count();

        return Observable.just(count != 0);
    }
}
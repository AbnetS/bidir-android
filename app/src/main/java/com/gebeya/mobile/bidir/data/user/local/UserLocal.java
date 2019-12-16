package com.gebeya.mobile.bidir.data.user.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.user.User_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Local concrete implementation for managing a User
 */
public class UserLocal extends BaseLocalSource implements UserLocalSource {

    private final Box<User> box;

    @Inject
    public UserLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(User.class);
    }

    @Override
    public Observable<Data<User>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<User>> get(@NonNull String id) {
        final User user = box.query().equal(User_._id, id).build().findFirst();
        return Observable.just(new Data<>(user));
    }

    @Override
    public Observable<Data<User>> get(int position) {
        final List<User> users = box.getAll();
        return Observable.just(new Data<>(users.isEmpty() ? null : users.get(position)));
    }

    @Override
    public Observable<User> put(@NonNull User item) {
        box.removeAll();
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Completable removeAll() {
        final Collection<Class> classes = store.getAllEntityClasses();

        for (Class c : classes) {
            store.boxFor(c).removeAll();
        }
        return Completable.complete();
    }
}
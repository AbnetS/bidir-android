package com.gebeya.mobile.bidir.data.pagination.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.pagination.Page;
import com.gebeya.mobile.bidir.data.pagination.Page_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

import io.objectbox.Box;
import io.reactivex.Completable;

/**
 * Concrete implementation for the {@link PageLocalSource} local source interface.
 */
public class PageLocal extends BaseLocalSource implements PageLocalSource {

    private final Box<Page> box;

    @Inject
    public PageLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Page.class);
    }

    @Override
    public Data<Page> getPageByType(@NonNull Service type) {
        final Page page = box.query().equal(Page_.type, String.valueOf(type)).build().findFirst();
        return new Data<>(page);
    }

    @Override
    public Completable update(@NonNull Page item) {
        box.query().equal(Page_.type, String.valueOf(item.type)).build().remove();
        box.put(item);
        return Completable.complete();
    }
}

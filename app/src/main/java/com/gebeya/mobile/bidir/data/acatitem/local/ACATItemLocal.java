package com.gebeya.mobile.bidir.data.acatitem.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem_;
import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT_;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Concrete implenentation for the {@link ACATItemLocalSource} interface.
 */

public class ACATItemLocal extends BaseLocalSource implements ACATItemLocalSource {
    private final Box<ACATItem> box;

    public ACATItemLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(ACATItem.class);
    }

    @Override
    public Observable<Data<ACATItem>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<ACATItem>> get(int position) {
        final List<ACATItem> acatItems = box.getAll();
        return Observable.just(new Data<>(acatItems.isEmpty() ? null : acatItems.get(position)));
    }

    @Override
    public Observable<Data<ACATItem>> get(@NonNull String id) {
        final List<ACATItem> acatItems = box.find(ACATItem_._id, id);
        return Observable.just(new Data<>(acatItems.isEmpty() ? null : acatItems.get(0)));
    }

    @Override
    public Observable<List<ACATItem>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<ACATItem> markForUpload(@NonNull ACATItem acatItem){
        acatItem.modified = true;
        acatItem.uploaded = false;
        acatItem.updatedAt = new DateTime();

        put(acatItem);
        //box.put(acatItem);

        return Observable.just(acatItem);
    }
    @Override
    public Observable<ACATItem> put(@NonNull ACATItem acatItem) {

        box.query().equal(ACATItem_._id, acatItem._id)
                .build()
                .remove();

        acatItem.id = 0;
        box.put(acatItem);
        return Observable.just(acatItem);
    }



    @Override
    public Observable<ACATItem> putNewACATItem(@NonNull ACATItem item) {
//        box.query().equal(ACATItem_.id, item.id).build().remove();
        box.put(item);
        return Observable.just(item);
    }

    @Override
    public Observable<ACATItem> updateACATItemId(@NonNull ACATItem item, String acatItemId) {
        ACATItem fetched = box.get(item.id);
        box.remove(fetched.id); //remove the item
        fetched._id = acatItemId; //Update the id with the API Id
        fetched.pendingCreation = false;

        box.put(fetched);
        return Observable.just(fetched);
    }

    @Override
    public Observable<List<ACATItem>> putAll(@NonNull List<ACATItem> acatItems) {
        box.removeAll();
        box.put(acatItems);
        return Observable.just(acatItems);
    }

    @Override
    public Observable<List<ACATItem>> getItemBySectionId(@NonNull String sectionId) {
        final List<ACATItem> acatItems = box.find(ACATItem_.sectionId, sectionId);
        return Observable.just(acatItems);
    }

    @Override
    public Observable<List<ACATItem>> getGroupedList(@NonNull String costListId, @NonNull String groupedListId) {
        final List<ACATItem> acatItems = box.query()
                .equal(ACATItem_.costListId, costListId)
                .and()
                .equal(ACATItem_.groupedListId, groupedListId)
                .build()
                .find();
        return Observable.just(acatItems);
    }
}

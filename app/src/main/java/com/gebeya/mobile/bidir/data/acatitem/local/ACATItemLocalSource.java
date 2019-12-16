package com.gebeya.mobile.bidir.data.acatitem.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;

import java.util.List;

import io.reactivex.Observable;


/**
 * Contract class for a local source for {@link ACATItemLocal} objects.
 */

public interface ACATItemLocalSource extends ReadableSource<ACATItem>, WritableSource<ACATItem> {
    Observable<List<ACATItem>> getItemBySectionId(@NonNull String sectionId);
    Observable<List<ACATItem>> getGroupedList(@NonNull String costListId, @NonNull String groupedListId);

    Observable<ACATItem> markForUpload(@NonNull ACATItem acatItem);

    Observable<ACATItem> putNewACATItem(@NonNull ACATItem item);

    Observable<ACATItem> updateACATItemId(@NonNull ACATItem item, String acatItemId);
}

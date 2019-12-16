package com.gebeya.mobile.bidir.data.groupedlist.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedList;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract class for a local source for {@link GroupedList} objects.
 */

public interface GroupedListLocalSource extends ReadableSource<GroupedList>, WritableSource<GroupedList> {

    /**
     * Get a grouped list that belongs to a specific cost list
     * @param parentCostListId Id of the parent cost list
     * @return Observable Grouped List that match the given parameters.
     */
    Observable<List<GroupedList>> getByCostList(@NonNull String parentCostListId);

}

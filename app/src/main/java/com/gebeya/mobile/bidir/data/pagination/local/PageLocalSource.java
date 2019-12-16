package com.gebeya.mobile.bidir.data.pagination.local;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.pagination.Page;
import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Completable;
import io.reactivex.annotations.NonNull;

/**
 * Represents the local page source data and operations to perform on the paginated source.
 */
public interface PageLocalSource {

    /**
     * Returns the given {@link Page} object associated with the given {@link Service} service
     * object.
     *
     * @param type The Service object type to lookup.
     * @return Data that contains the Page object found, if any.
     */
    Data<Page> getPageByType(@NonNull Service type);

    /**
     * Save the given {@link Page} object to the database.
     *
     * @param page Page object to updateTotalPages.
     * @return Completable, indicating that the operation has completed.
     */
    Completable update(@NonNull Page page);
}
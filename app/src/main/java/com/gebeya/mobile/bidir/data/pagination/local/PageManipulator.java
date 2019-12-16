package com.gebeya.mobile.bidir.data.pagination.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.pagination.Page;

/**
 * Interface representing a manipulator for the {@link Page} class. The modifications done on a page
 * are performed by the manipulator class.
 */
public interface PageManipulator {

    /**
     * Creates a new page object. Use this method in order to setup and create a new Page object.
     *
     * The current page of the {@link Page} object is automatically initialized to zero.
     *
     * @param type The Page type.
     * @param totalPages The total number of pages available.
     */
    void create(@NonNull Service type,
                int totalPages);

    /**
     * Update the current {@link Page} object with the new total page count.
     * @param totalPages The new total pages to updateTotalPages with.
     */
    void updateTotalPages(int totalPages);

    /**
     * Returns the {@link Page} instance that the manipulator has been working on.
     *
     * Use this method in order to return the {@link Page} object to use for saving later.
     */
    Page getPage();

    /**
     * Set the {@link Page} instance that the manipulator should work on.
     * @param page Page to update the data with.
     */
    void setPage(@NonNull Page page);

    /**
     * Get the total pages of the current {@link Page} object.
     *
     * @return The total current number of pages.
     */
    int getTotalPages();

    /**
     * Get the current page number belonging to the currently set {@link Page} object.
     * @return number representing the current page.
     */
    int getCurrentPage();

    /**
     * Causes the {@link Page} object to advance to the current page by one.
     */
    void next();

    /**
     * Returns true if there are more pages to advance by, false otherwise.
     */
    boolean hasNext();

    /**
     * Returns true if there is a {@link Page} object already present, false otherwise.
     */
    boolean hasPage();

    /**
     * Causes the {@link Page} object to reset the current page to restart from zero.
     */
    void restart();

    /**
     * Sets the total pages, current page and per page count items to zero.
     */
    void clear();
}
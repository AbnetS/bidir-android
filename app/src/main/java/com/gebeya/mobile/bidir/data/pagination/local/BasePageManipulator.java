package com.gebeya.mobile.bidir.data.pagination.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.pagination.Page;

public class BasePageManipulator implements PageManipulator {

    private Page page;

    @Override
    public void create(@NonNull Service type, int totalPages) {
        final Page newPage = new Page();

        newPage.type = type;
        newPage.totalPages = totalPages;
        newPage.currentPage = 0;

        setPage(newPage);
    }

    @Override
    public void updateTotalPages(int totalPages) {
        page.totalPages = totalPages;
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public void setPage(@NonNull Page page) {
        this.page = page;
    }

    @Override
    public int getTotalPages() {
        return page == null ? 0 : page.totalPages;
    }

    @Override
    public int getCurrentPage() {
        return page == null ? 1 : page.currentPage;
    }

    @Override
    public void next() {
        page.currentPage++;
    }

    @Override
    public boolean hasNext() {
        return page.currentPage < page.totalPages;
    }

    @Override
    public boolean hasPage() {
        return page != null;
    }

    @Override
    public void restart() {
        page.currentPage = 0;
    }

    @Override
    public void clear() {
        page.currentPage = 0;
        page.totalPages = 0;
    }
}

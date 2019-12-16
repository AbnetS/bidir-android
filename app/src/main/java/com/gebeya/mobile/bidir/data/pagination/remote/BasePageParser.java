package com.gebeya.mobile.bidir.data.pagination.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.google.gson.JsonObject;

/**
 * Concrete implementation for the {@link PageParser} interface.
 */
public class BasePageParser implements PageParser {

    @Override
    public PageResponse parse(@NonNull JsonObject object, @NonNull Service type) throws Exception {
        final PageResponse response = new PageResponse();

        response.type = type;
        response.totalPages = object.get("total_pages").getAsInt();
        response.currentPage = object.get("current_page").getAsInt();

        return response;
    }
}

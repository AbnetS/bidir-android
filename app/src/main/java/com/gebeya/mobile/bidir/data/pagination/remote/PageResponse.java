package com.gebeya.mobile.bidir.data.pagination.remote;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;

/**
 * Represents the pagination data parsed from the API endpoint that supports pagination
 */
public class PageResponse {
    public Service type;
    public int totalPages;
    public int currentPage;
}

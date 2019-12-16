package com.gebeya.mobile.bidir.data.acatform.remote;

import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;

import java.util.List;

/**
 * Wrapper class to represent both {@link ACATFormResponse} along with their paginated
 * information inside a {@link PageResponse} object.
 */
public class PaginatedACATFormResponse {
    public List<ACATFormResponse> responses;
    public PageResponse pageResponse;
}

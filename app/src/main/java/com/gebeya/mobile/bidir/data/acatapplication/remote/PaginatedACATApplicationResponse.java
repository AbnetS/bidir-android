package com.gebeya.mobile.bidir.data.acatapplication.remote;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;

import java.util.List;

/**
 * Wrapper class to represent a list of {@link ACATApplicationResponse} objects along with their
 * paginated information, represented using the {@link PageResponse} object.
 */
public class PaginatedACATApplicationResponse {
    public List<ACATApplicationResponse> applicationResponses;
    public PageResponse pageResponse;
}

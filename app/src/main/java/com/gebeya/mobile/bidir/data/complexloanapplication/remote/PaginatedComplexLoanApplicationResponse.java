package com.gebeya.mobile.bidir.data.complexloanapplication.remote;

import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;

/***
 * Wrapper class to represent both a {@link ComplexLoanApplicationResponse} along with the
 * paginated data represented by the {@link PageResponse} object.
 */
public class PaginatedComplexLoanApplicationResponse {
    public ComplexLoanApplicationResponse loanApplicationResponse;
    public PageResponse pageResponse;
}

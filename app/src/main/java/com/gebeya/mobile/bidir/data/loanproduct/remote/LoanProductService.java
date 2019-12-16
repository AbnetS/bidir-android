package com.gebeya.mobile.bidir.data.loanproduct.remote;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Retrofit service for a single Loan Product
 *
 */

public interface LoanProductService {

    @GET(Constants.PAGINATE_URL)
    Observable<JsonObject> getAll();
}

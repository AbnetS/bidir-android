package com.gebeya.mobile.bidir.data.loanproduct.remote;

import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormResponse;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProductResponse;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by abuti on 5/7/2018.
 */

public interface LoanProductRemoteSource {
    /**
     * Retrieve a list of all loan products from the API
     *
     * @return List of {@link com.gebeya.mobile.bidir.data.loanproduct.LoanProductResponse} objects.
     */
    Observable<List<LoanProductResponse>> getAll();

    /**
     * parses deductibles and cost of loan lists from the API
     * @param array JsonArray to parseACATApplication pageResponse list of deductibles/Cost of Loan from.
     * @param category identifies whether the item is deductible or cost of loan
     *
     * @return List of {@link LoanProductItem } objects.
     */
    List<LoanProductItem> parseLoanProdItemList(@NonNull JsonArray array, @NonNull String category) throws Exception;


}

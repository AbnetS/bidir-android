package com.gebeya.mobile.bidir.data.loanproductitem.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/7/2018.
 */

public interface LoanProductItemParser {

    String CATEGORY_COST_OF_LOAN = "COST_OF_LOAN";
    String CATEGORY_DEDUCTIBLES = "DEDUCTIBLES";

    /**
     * Parse the given JsonObject and return a {@link LoanProductItem} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @param category the category that the loan product item belongs to
     * @return Parsed Loan Product Item object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    LoanProductItem parse(@NonNull JsonObject object, @NonNull String category) throws Exception;
}

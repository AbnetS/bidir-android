package com.gebeya.mobile.bidir.data.loanproduct.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/7/2018.
 */

public interface LoanProductParser {
    /**
     * Parse the given JsonObject and return a {@link LoanProduct} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @return Parsed Loan Product object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    LoanProduct parse(@NonNull JsonObject object) throws Exception;

    /**
     * Get list of Deductibles/Cost of Loan IDs from the given Json Array.
     *
     * @param array JsonArray to parseACATApplication pageResponse list of eductibles/Cost of Loan IDs from.
     * @return List of parsed eductibles/Cost of Loan IDs.
     */
    List<String> getLoanProdItemIds(@NonNull JsonArray array);
}

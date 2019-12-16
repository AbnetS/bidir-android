package com.gebeya.mobile.bidir.data.loanproductitem.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.google.gson.JsonObject;

/**
 * Concrete implementation for the {@link LoanProductItemParser} interface.
 */
public class BaseLoanProductItemParser implements LoanProductItemParser {

    @Override
    public LoanProductItem parse(@NonNull JsonObject object, @NonNull String category) throws Exception {
        try {
            final LoanProductItem loanProductItem = new LoanProductItem();

            loanProductItem._id = object.get("_id").getAsString();
            loanProductItem.item = object.get("item").getAsString();
            loanProductItem.fixedAmount = object.get("fixed_amount").getAsDouble();
            loanProductItem.percent = object.get("percent").getAsDouble();
            loanProductItem.category = category;

            return loanProductItem;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Deductibles/Cost of Loan Item: " + e.getMessage());
        }
    }
}

package com.gebeya.mobile.bidir.data.loanproduct.remote;

import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by abuti on 5/7/2018.
 */

public class BaseLoanProductParser implements LoanProductParser {

    @Override
    public LoanProduct parse(@NonNull JsonObject object) throws Exception {
        try {
            final LoanProduct loanProduct = new LoanProduct();

            loanProduct._id = object.get("_id").getAsString();
            loanProduct.name = object.get("name").getAsString();
            loanProduct.currency = object.get("currency").getAsString();
            loanProduct.maxLoanAmount = object.get("maximum_loan_amount").getAsDouble();
            loanProduct.purpose = object.get("purpose").getAsString();

            loanProduct.deductibleIDs = getLoanProdItemIds(object.get("deductibles").getAsJsonArray());
            loanProduct.costOfLoanIDs = getLoanProdItemIds(object.get("cost_of_loan").getAsJsonArray());

            return loanProduct;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing LoanProduct: " + e.getMessage());
        }

    }

    @Override
    public List<String> getLoanProdItemIds(@NonNull JsonArray array) {
        final List<String> ids = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final String id = object.get("_id").getAsString();
            ids.add(id);
        }
        return ids;
    }
}

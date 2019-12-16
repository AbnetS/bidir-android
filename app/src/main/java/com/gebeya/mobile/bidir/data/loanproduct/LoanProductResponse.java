package com.gebeya.mobile.bidir.data.loanproduct;

/**
 * Created by abuti on 5/7/2018.
 */

import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;

import java.util.List;

public class LoanProductResponse {
    public LoanProduct loanProduct;
    public List<LoanProductItem> deductibles;
    public List<LoanProductItem> costOfLoan;

}

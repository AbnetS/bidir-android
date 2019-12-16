package com.gebeya.mobile.bidir.data.loanproduct.local;

/**
 * Created by abuti on 5/7/2018.
 */


import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;


/**
 * Contract class for a local source for {@link LoanProduct} objects.
 */

public interface LoanProductLocalSource extends ReadableSource<LoanProduct>, WritableSource<LoanProduct> {

}

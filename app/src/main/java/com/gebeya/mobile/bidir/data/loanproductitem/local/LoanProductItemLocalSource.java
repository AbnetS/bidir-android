package com.gebeya.mobile.bidir.data.loanproductitem.local;

/**
 * Created by abuti on 5/7/2018.
 */

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;

/**
 * Contract class for managing local source for {@link LoanProductItem} objects.
 */

public interface LoanProductItemLocalSource extends ReadableSource<LoanProductItem>, WritableSource<LoanProductItem>{

}

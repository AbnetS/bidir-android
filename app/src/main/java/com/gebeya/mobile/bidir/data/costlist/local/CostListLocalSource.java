package com.gebeya.mobile.bidir.data.costlist.local;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.costlist.CostList;

/**
 * Created by abuti on 5/11/2018.
 */

public interface CostListLocalSource extends ReadableSource<CostList>, WritableSource<CostList> {
    //TODO: Add a method that returns cost list by section
}



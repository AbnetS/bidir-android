package com.gebeya.mobile.bidir.data.costlist2.linearcostlist;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for linear cost list.
 */

@Entity
public class LinearCostList {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String referenceId;

    @Index
    public String cashFlowType;

    public double totalPrice;
    public double unitPrice;
    public double value;
}

package com.gebeya.mobile.bidir.data.acatitemvalue;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for values  (such as unitprice, total price) of ACAT item.
 */

@Entity
public class ACATItemValue {
    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String acatItemId; //to relate the values to an ACAT Item

    public String type; //determines whether the values are estimated or actual

    public double value;
    public double unitPrice;
    public double totalPrice;
}

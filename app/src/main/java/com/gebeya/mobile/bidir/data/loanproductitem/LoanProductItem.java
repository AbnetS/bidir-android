package com.gebeya.mobile.bidir.data.loanproductitem;


import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for representing a Loan Product Component (either a deductibe or cost of loan).
 *
 * Created by abuti on 5/7/2018.
 */
@Entity
public class LoanProductItem {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String category;

    public String item;
    public double fixedAmount;
    public double percent;


    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;


    //Another way to implement this class would be...
    //Instead of the following
    //*****public double fixedAmount;
    //*****public double percent;
    //We can also do the following:
    //public double amount;
    //public String type;//This could be enum consisting of either Fixed or Percent to indicate what the type of amount is
}

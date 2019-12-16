package com.gebeya.mobile.bidir.data.loanproduct;

/**
 * Created by abuti on 5/7/2018.
 */

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for representing a Loan Product.
 */

@Entity
public class LoanProduct {
    @Id
    public long id;

    @Index
    public String _id;

    public String name;
    public double maxLoanAmount;
    public String currency;
    public String purpose;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> costOfLoanIDs;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> deductibleIDs;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

}


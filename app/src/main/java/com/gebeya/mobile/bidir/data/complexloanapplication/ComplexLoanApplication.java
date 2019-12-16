package com.gebeya.mobile.bidir.data.complexloanapplication;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

/**
 * ObjectBox complex loan application implementation.
 */
@Entity
public class ComplexLoanApplication {

    @Id
    public long id;

    @Index
    public String _id;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    @Index
    public String clientId;

    @Index
    public String createdBy;

    public String branch;
    public String status;
    public String disclaimer;
    public String remark;
    public boolean hasSections;
    public String layout;

    public boolean forGroup;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    public ToOne<GroupedComplexLoan> groupedComplexLoan;

    public ComplexLoanApplication() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }
}
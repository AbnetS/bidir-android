package com.gebeya.mobile.bidir.data.acatapplication;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

/**
 * Created by abuti on 5/17/2018.
 */

@Entity
public class ACATApplication {
    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String clientID;

    @Index
    public String branchID;

    @Index
    public String createdByID;

    @Index
    public String status;

    public double estimatedTotalCost;
    public double estimatedTotalRevenue;
    public double estimatedNetIncome;

    public double actualTotalCost;
    public double actualTotalRevenue;
    public double actualNetIncome;

    public String loanProductID;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public ToOne<GroupedACAT> groupedACAT;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

}

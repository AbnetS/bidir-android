package com.gebeya.mobile.bidir.data.cropacat;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by abuti on 5/17/2018.
 */

@Entity
public class CropACAT {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String clientID;

    @Index
    public String acatApplicationID;

    @Index
    public String createdBy;

    @Index
    public String cropID;

    @Index
    public String status;
    public String title;

    public String cropName;
    public double latitude;
    public double longitude;

    public double estimatedTotalCost;
    public double estimatedTotalRevenue;
    public double estimatedNetIncome;

    public double actualTotalCost;
    public double actualTotalRevenue;
    public double actualNetIncome;

    public String firstExpenseMonth;
    public Boolean accessToNonFinServices;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> NonFinServices;

    public String croppingArea;

    //GPS needs another implementation
    public Boolean isGPSPolygon;

    //public double singleLatitude;
    //public double singleLongitude;

    public String costSectionId;
    public String revenueSectionId;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public boolean active;
    public boolean complete;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    public boolean loanGranted;

    public CropACAT() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }
}
package com.gebeya.mobile.bidir.data.acatform;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Class for represeting a single ACAT form
 */

@Entity
public class ACATForm {

    @Id
    public long id;

    @Index
    public String _id;

    public String type;
    public String layout;

    public String cropName;

    public String title;
    public String createdBy;

    // TODO: Figure out implementation for estimated and actual attribute.
    public double estimatedTotalCost;
    public double estimatedTotalRevenue;
    public double estimatedNetIncome;

    public double actualTotalCost;
    public double actualNetIncome;

    public String firstExpenseMonth;
    public Boolean accessToNonFinServices;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> NonFinServices;

    public String croppingArea;

    public String costSectionId;
    public String revenueSectionId;
    
    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;



}

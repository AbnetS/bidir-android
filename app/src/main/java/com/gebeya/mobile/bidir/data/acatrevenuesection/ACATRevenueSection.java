package com.gebeya.mobile.bidir.data.acatrevenuesection;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for representing the revenue section of ACAT Form.
 */
@Entity
public class ACATRevenueSection {
    @Id
    public long id;

    @Index
    public String _id;

    public String title;
    public int number;

    public double estimatedProbRevenue;
    public double estimatedMinRevenue;
    public double estimatedMaxRevenue;
    public double actualRevenue;

    public double estimatedSubTotal;
    public double actualSubTotal;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> subSectionIDs;

    @Index
    public String parentSectionID; //applicable when it is a subsection

    @Index
    public String yieldID;

    @Index
    public String yieldConsumptionID;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    public ACATRevenueSection() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }

}

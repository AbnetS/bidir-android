package com.gebeya.mobile.bidir.data.acatcostsection;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by abuti on 5/11/2018.
 */

@Entity
public class ACATCostSection {
    @Id
    public long id;

    @Index
    public String _id;

//    @Index
//    public String referenceId;
//
//    @Index
//    public String referenceType;

    public String title;
    public int number;

    public double estimatedSubTotal;
    public double actualSubTotal;

    public String variety;
    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> seedSources;

    public String costListID;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> subSectionIDs;

    public String parentSectionID; //applicable when it is a subsection

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    public ACATCostSection() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }
}

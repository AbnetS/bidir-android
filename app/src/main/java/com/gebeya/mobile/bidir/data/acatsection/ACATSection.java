package com.gebeya.mobile.bidir.data.acatsection;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for representing a single section of ACAT Form.
 */

@Entity
public class ACATSection {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String referenceId;

    @Index
    public String referenceType;

    public double estimatedSubTotal;
    public double acheivedSubTotal;

    public String estimatedCashFlowId;
    public String achievedCashFlowId;

    public int number;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> subSectionIDs;
    public String title;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;
}

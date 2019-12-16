package com.gebeya.mobile.bidir.data.costlist;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Model class for representing a cost list found in acat cost sections.
 */

@Entity
public class CostList {
    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String parentSectionID;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> linearListIDs; //array of acat items

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> groupedListIDs; //array of groups containing acat items

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;


}

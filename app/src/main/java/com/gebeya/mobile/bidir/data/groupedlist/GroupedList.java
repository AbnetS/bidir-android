package com.gebeya.mobile.bidir.data.groupedlist;

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
public class GroupedList {
    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String parentCostListId;

    public String title;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> itemIDs; //array of acat items

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;



}

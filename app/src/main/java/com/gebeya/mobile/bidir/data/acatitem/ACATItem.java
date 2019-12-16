package com.gebeya.mobile.bidir.data.acatitem;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for ACAT item whether it is cost item or revenue item.
 */

@Entity
public class ACATItem {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String referenceId; //might be removed later

    @Index
    public String category; //determines whether the acat Item belongs to cost section or revenue section

    @Index
    public String sectionId; //Section Id that the acat item belongs to

    @Index
    public String costListId; //parent cost list Id if the acat item belongs to a cost list in cost section

    @Index
    public String groupedListId; // Grouped List Id if the acat item belongs to a grouped list

    public String item;
    public String unit;
    public String remark;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    public ACATItem() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }

}

package com.gebeya.mobile.bidir.data.acatapplication;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

@Entity
public class ACATApplicationSync {

    @Id
    public long id;

    @Index
    public String acatAppId;

    @Index
    public String clientId;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> cropACATIds;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> costSectionIds;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> revenueSectionIds;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> acatItemIds;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> yieldConsumptionIds;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public ACATApplicationSync() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }

}

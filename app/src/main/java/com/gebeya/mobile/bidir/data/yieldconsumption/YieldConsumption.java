package com.gebeya.mobile.bidir.data.yieldconsumption;


import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by abuti on 5/15/2018.
 */

@Entity
public class YieldConsumption {
    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String parentSectionID;

    @Index
    public String remark;

    public double estimatedOwnCons;
    public double estimatedSeedReserve;
    public double estimatedForMarket;

    public double actualOwnCons;
    public double actualSeedReserve;
    public double actualForMarket;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

}

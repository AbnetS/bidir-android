package com.gebeya.mobile.bidir.data.gpslocation;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by abuti on 5/17/2018.
 */
@Entity
public class GPSLocation {
    @Id
    public long id;

    public String acatApplicationID;
    public String cropACATID;

    public double latitude;
    public double longitude;
}
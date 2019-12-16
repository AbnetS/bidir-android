package com.gebeya.mobile.bidir.data.screening;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Class for representing the screening model.
 */
@Entity
public class Screening implements Serializable {

    @Id
    public long id;

    @Index
    public String _id;
    public String clientId;
    public String status;
    public String title;
}
package com.gebeya.mobile.bidir.data.task;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;

import org.joda.time.DateTime;

import java.io.Serializable;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Task model implementation
 */
@Entity
public class Task implements Serializable {

    @Id
    public long id;

    @Index
    public String _id;
    public String description;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public String type;

    @Index
    public String referenceId;

    public String referenceType;
    public String createdBy;
    public String comment;
    public String status;
    public String userId;
}
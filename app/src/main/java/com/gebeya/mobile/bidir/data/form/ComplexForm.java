package com.gebeya.mobile.bidir.data.form;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model to represent a complex form.
 */
@Entity
public class ComplexForm {

    @Id
    public long id;

    @Index
    public String _id;

    public String type;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    @Index
    public String createdBy;

    public boolean hasSections;
    public String layout;

    public String purpose;
    public String title;
    public String subtitle;
}
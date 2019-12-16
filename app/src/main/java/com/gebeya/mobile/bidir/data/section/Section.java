package com.gebeya.mobile.bidir.data.section;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for representing a single Section of a screening/loan application.
 */
@Entity
public class Section {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String referenceId;

    @Index
    public String referenceType;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> questionIds;

    public int number;

    public String title;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;
}

package com.gebeya.mobile.bidir.data.complexquestion;

import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for a single complex question (belonging either to a ComplexScreening or
 * ComplexLoanApplication).
 */
@Entity
public class ComplexQuestion {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String referenceId;

    @Index
    public String referenceType;

    public String text;

    public boolean hasPrerequisites;
    public boolean show;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> values;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> subQuestions;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> options;

    public String measurementUnit;
    public String validationFactor;
    public boolean required;
    public String type;
    public String remark;

    @Index
    public int number;
}
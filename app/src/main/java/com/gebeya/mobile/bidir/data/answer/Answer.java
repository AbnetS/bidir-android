package com.gebeya.mobile.bidir.data.answer;

import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import java.util.ArrayList;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Class for representing a single answer
 */
@Entity
public class Answer {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String referenceId;

    @Index
    public String referenceType;

    public int number;
    public String message;
    public int type;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public ArrayList<String> choices;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public ArrayList<String> answers;
    public String remark;
    public boolean required;

    public boolean selected;

    public Answer() {
        number = 1;
        message = "";
        type = QAHelper.TYPE_INPUT;
        choices = new ArrayList<>();
        answers = new ArrayList<>();
        remark = "";
        required = false;
        selected = false;
    }
}
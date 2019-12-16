package com.gebeya.mobile.bidir.data.question;

import com.gebeya.mobile.bidir.data.base.remote.QuestionAnswerHelper;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import java.util.ArrayList;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Simple model for representing the question
 */
@Entity
public class Question {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String formId;

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

    public Question() {
        number = 1;
        message = "";
        type = QuestionAnswerHelper.TYPE_INPUT;
        choices = new ArrayList<>();
        answers = new ArrayList<>();
        remark = "";
        required = false;
        selected = false;
    }
}
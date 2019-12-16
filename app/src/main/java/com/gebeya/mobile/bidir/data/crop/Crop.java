package com.gebeya.mobile.bidir.data.crop;

import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import java.util.ArrayList;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Simple model for representing a Crop.
 */
@Entity
public class Crop {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String acatId;

    public boolean hasACAT;
    public String category;
    public String name;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public ArrayList<String> choices;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public ArrayList<String> answers;

    public boolean selected;

    public Crop() {
        hasACAT = false;
        category = "";
        name = "";
        choices = new ArrayList<>();
        answers = new ArrayList<>();
        selected = false;
    }
}

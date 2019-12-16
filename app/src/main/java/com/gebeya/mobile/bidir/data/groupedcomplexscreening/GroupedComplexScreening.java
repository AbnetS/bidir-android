package com.gebeya.mobile.bidir.data.groupedcomplexscreening;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

@Entity
public class GroupedComplexScreening {

    @Id
    public long id;

    @Index
    public String _id;
    @Index
    public String groupId;

    public String status;

    @Backlink(to = "groupedComplexScreening")
    public ToMany<ComplexScreening> complexScreenings;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> screeningIds;
    
    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> membersId;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

}

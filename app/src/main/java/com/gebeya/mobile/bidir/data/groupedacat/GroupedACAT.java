package com.gebeya.mobile.bidir.data.groupedacat;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

@Entity
public class GroupedACAT {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String groupId;

    public String status;

    @Backlink(to = "groupedACAT")
    public ToMany<ACATApplication> acatApplications;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> membersId;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;
}

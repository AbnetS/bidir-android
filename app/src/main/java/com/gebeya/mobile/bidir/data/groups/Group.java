package com.gebeya.mobile.bidir.data.groups;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexquestion.local.StringListConverter;

import org.joda.time.DateTime;

import java.io.Serializable;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

/**
 * Group model used to represent group clients, used by ObjectBox and Retrofit
 */
@Entity
public class Group implements Serializable{

    @Id
    public long id;

    @Index
    public String _id;
    public String groupName;
    public String leaderId;
    public int membersCount;
    public String groupStatus;
    public double totalLoanAmount;
    public double totalGrantedAmount;
    public double totalPaidAmount;

    public String createdBy;

    @Backlink(to = "group")
    public ToMany<Client> clients;

    @Convert(converter = StringListConverter.class, dbType = String.class)
    public List<String> membersId;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime createdAt;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

}

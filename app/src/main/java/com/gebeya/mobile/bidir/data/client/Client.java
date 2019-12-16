package com.gebeya.mobile.bidir.data.client;

import com.gebeya.mobile.bidir.data.base.local.DateTimeConverter;
import com.gebeya.mobile.bidir.data.groups.Group;

import org.joda.time.DateTime;

import java.io.Serializable;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

/**
 * Client model used both by ObjectBox nd Retrofit
 */
@Entity
public class Client implements Serializable {

    @Id
    public long id;

    @Index
    public String _id;
    public String firstName;
    public String lastName;
    public String surname;
    public String gender;
    public String idNumber;
    public String idCardUrl;
    public String photoUrl;
    public int loanCycleCount;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime dateOfBirth;

    public String maritalStatus;
    public String woreda;
    public String kebele;
    public String houseNumber;
    public String phoneNumber;
    public String status;
    public String createdBy;

    @Convert(converter = DateTimeConverter.class, dbType = String.class)
    public DateTime updatedAt;

    public int householdMemberCount;

    public double latitude;
    public double longitude;

    public String spouseFirstName;
    public String spouseLastName;
    public String spouseSurname;
    public String spouseIdNumber;

    public boolean modified;
    public boolean uploaded;
    public boolean pendingCreation;

    public ToOne<Group> group;

    public boolean forGroup;

    public Client() {
        modified = false;
        uploaded = true;
        pendingCreation = false;
    }
}
package com.gebeya.mobile.bidir.data.client.remote;

import java.io.File;

/**
 * Simple model to hold registration data. Data in here will eventually be passed to the
 * {@link com.gebeya.mobile.bidir.data.client.Client} model
 */
public class RegisterDto {

    // Personal Details section
    public String firstName;
    public File photoFile;
    public String lastName;
    public String surname;
    public String gender;

    public int day;
    public int month;
    public int year;

    public String createdBy;

    // group identifier
    public boolean for_group=false;

    // location information
    public double latitude;
    public double longitude;

    // Family Status section
    public String maritalStatus;
    public String householdMemberCount;

    // Address section
    public File idCardFile;
    public String idNumber;
    public String woreda;
    public String kebele;
    public String houseNumber;
    public String phoneNumber;

    // Spouse Details section
    public String spouseIdNumber;
    public String spouseFirstName;
    public String spouseLastName;
    public String spouseSurname;
}
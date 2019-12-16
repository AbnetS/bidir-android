package com.gebeya.mobile.bidir.ui.register;

import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;

import java.io.File;

/**
 * Represents the registration data of a single client
 */
public class RegisterData {

    private static RegisterData data;

    public static RegisterData getInstance() {
        if (data == null) {
            data = new RegisterData();
        }
        return data;
    }

    // Personal details
    public File personalPictureFile;
    public String name;
    public String fatherName;
    public String grandfatherName;
    public String gender;
    public int day;
    public int month;
    public int year;

    // Marital status
    public MaritalStatus maritalStatus;
    public int householdMemberCount;

    // Address
    public File idCardPictureFile;
    public String idNumber;
    public String woreda;
    public String kebele;
    public String houseNumber;
    public String phoneNumber;

    // Spouse details
    public String spouseIdNumber;
    public String spouseName;
    public String spouseFatherName;
    public String spouseGrandfatherName;

    // location information
    public double latitude;
    public double longitude;

    private RegisterData() {
        reset();
    }

    public void reset() {
        personalPictureFile = null;
        name = "";
        fatherName = "";
        grandfatherName = "";
        gender = ClientParser.GENDER_MALE;
        day = 0;
        month = 0;
        year = 0;

        maritalStatus = MaritalStatus.SINGLE;
        householdMemberCount = 0;

        idCardPictureFile = null;
        idNumber = "";
        woreda = "";
        kebele = "";
        houseNumber = "";
        phoneNumber = "";

        spouseIdNumber = "";
        spouseName = "";
        spouseFatherName = "";
        spouseGrandfatherName = "";

        latitude = 0;
        longitude = 0;
    }
}
package com.gebeya.mobile.bidir.data.base.remote;

/**
 * Common API error values
 */
public final class ApiErrors {
    /**
     * No Internet connection found error
     */
    public static final String COMMON_ERROR_NO_INTERNET_CONNECTION = "No Internet connection found";
    public static final String NO_ROUTE_TO_HOST = "No route to host";


    public static final String LOGIN_PART_INVALID_USERNAME_PASSWORD = "credentials does not exist";
    public static final String LOGIN_PART_INVALID_PASSWORD = "is incorrect";
    public static final String LOGIN_ERROR_INVALID_USERNAME_PASSWORD = "Invalid username or password";
    public static final String LOGIN_ERROR_GENERIC = "There was an error trying to login";

    public static final String REGISTER_PART_ALREADY_EXISTS = "with those details already exists";
    public static final String REGISTER_ERROR_CLIENT_ALREADY_EXISTS = "Client with the same phone number or National ID already exists";
    public static final String REGISTER_ERROR_GENERIC = "There was an error during registration";

    public static final String UPDATE_ERROR_GENERIC = "There was an error during the client saveLoanProposal";

    public static final String SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR = "There was an error trying to saveLoanProposal screening questions";
    public static final String SCREENING_QUESTIONS_LOAD_GENERIC_ERROR = "There was an error trying to load screening information";
    public static final String LOAN_APPLICATION_QUESTIONS_UPDATE_GENERIC_ERROR = "There was an error trying to saveLoanProposal loan application questions";
    public static final String PROFILE_UPDATE_GENERIC_ERROR = "There was an error trying to saveLoanProposal user profile";
    public static final String ACAT_FORM_LOAD_GENERIC_ERROR = "There was an error trying to load Crop-ACAT information.";
    public static final String ACAT_FORM_UPDATE_GENERIC_ERROR = "There was an error trying to saveLoanProposal Crop-ACAT information";
    public static final String CLIENT_ACAT_UPDATE_GENERIC_ERROR = "There was an error trying to saveLoanProposal Client ACAT information.";
    public static final String CLIENT_ACAT_LOADING_GENERIC_ERROR = "There was an error trying to load Client ACAT information.";
    public static final String LOAN_APPROVED_UPDATE_GENERIC_ERROR = "There was an error trying to saveLoanProposal loan amount.";
    public static final String SCREENING_CREATION_GENERIC_ERROR = "There was an error trying to create screening.";
    public static final String CONNECTION_ABORT_ERROR = "Software caused connection abort";
    public static final String GROUP_CREATION_GENERIC_ERROR = "There was an error trying to create the Group";
    public static final String GROUP_APPLICATION_UPDATE_GENERIC_ERROR = "There was an error trying to update group application";
    public static final String GROUP_APPLICATION_LOADING_GENERIC_ERROR = "There was an error trying to load Group application.";
    public static final String CLIENT_UDPATE_GENERIC_MESSAGE = "There was an error trying to update client status.";
}
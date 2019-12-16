package com.gebeya.mobile.bidir.impl.util;

public final class Constants {
    public static final long SPLASH_DELAY = 1000;
    public static final long ANIMATION_DURATION = 750;
    public static final String LOGO_URL = "http://bidir-api-bucket.storage.googleapis.com/assets/BUUSAA_GONOFAA_MICROFINANCE_c813d36b844f.png";
    public static final int SCREENING_CHOICE_COLUMN_COUNT = 2;
    public static final int LOAN_APPLICATION_CHOICE_COLUMN_COUNT = 3;
    public static final int PRELIMINARY_INFORMATION_CHOICE_COLUMN_COUNT = 2;
    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final String PHONE_NUMBER_SUFFIX = "09";
    public static final int MAX_CLIENT_REGISTRATION_AGE = 60;

    private static final int PER_PAGE = 1000;
    private static final int PAGE = 1;

    public static final String PAGINATE_BASE = "paginate";
    public static final String PAGINATE_URL = "paginate?page=" + PAGE + "&per_page=" + PER_PAGE;
    public static final String PAGINATE_URL_SOURCE = PAGINATE_URL + "&source=app";

    public static final String DATE_DISPLAY_FORMAT = "%02d/%02d/%d";
    public static final String DATE_REGISTER_FORMAT = "%04d-%02d-%02dT00:00:00.000Z";

    public static final String URL_FORMAT = "%s://%s/%s/"; // TODO: Revert this later.

    public static final String REF_TYPE_SCREENING = "SCREENING";
    public static final String REF_TYPE_LOAN_APPLICATION = "LOAN_APPLICATION";
    public static final String REF_TYPE_FORM_SCREENING = "FORM_SCREENING";
    public static final String REF_TYPE_FORM_LOAN_APPLICATION = "LOAN_APPLICATION";
    public static final String REF_TYPE_ACAT = "ACAT";
    public static final String REF_TYPE_ACAT_SECTION = "ACAT_SECTION";
    public static final String REF_TYPE_ACAT_SUBSECTION = "ACAT_SUBSECTION";
    public static final String REF_TYPE_ACAT_NESTED_SUBSECTION = "ACAT_NESTED_SUBSECTION";

    public static final String GPS_FORMAT = "%.3f, %.3f";
    public static final long GPS_UPDATE_INTERVAL = 1000;
}
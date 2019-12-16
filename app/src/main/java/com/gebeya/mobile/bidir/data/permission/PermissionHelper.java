package com.gebeya.mobile.bidir.data.permission;

import android.support.annotation.NonNull;

/**
 * Interface for a class that is used to help in the conversion of a Question/Answer to pojo/json
 * and vice versa.
 */
public interface PermissionHelper {

    String ENTITY_SCREENING = "SCREENING";
    String API_ENTITY_SCREENING = "SCREENING";
    String ENTITY_FORM = "FORM";
    String API_ENTITY_FORM = "FORM";
    String ENTITY_USER = "USER";
    String API_ENTITY_USER = "USER";
    String ENTITY_CLIENT = "CLIENT";
    String API_ENTITY_CLIENT = "CLIENT";
    String ENTITY_LOAN = "LOAN";
    String API_ENTITY_LOAN = "LOAN";
    String ENTITY_CROP = "CROP";
    String API_ENTITY_CROP = "CROP";
    String ENTITY_ACAT = "ACAT";
    String API_ENTITY_ACAT = "ACAT";
    String ENTITY_ACAT_PROCESSOR = "CLIENT_ACAT";
    String API_ENTITY_ACAT_PROCESSOR = "CLIENT_ACAT";
    String ENTITY_LOAN_PRODUCT = "LOAN_PRODUCT";
    String API_ENTITY_LOAN_PRODUCT = "LOAN_PRODUCT";
    String ENTITY_GROUP_LOAN = "GROUP";
    String API_ENTITY_GROUP_LOAN = "GROUP";
    String UNKNOWN_ENTITY = "unknown_entity";

    String OPERATION_CREATE = "CREATE";
    String API_OPERATION_CREATE = "CREATE";
    String OPERATION_VIEW = "VIEW";
    String API_OPERATION_VIEW = "VIEW";
    String OPERATION_VIEW_ALL = "VIEW_ALL";
    String API_OPERATION_VIEW_ALL = "VIEW_ALL";
    String OPERATION_READ = "READ";
    String API_OPERATION_READ = "READ";
    String OPERATION_UPDATE = "UPDATE";
    String API_OPERATION_UPDATE = "UPDATE";
    String OPERATION_AUTHORIZE = "AUTHORIZE";
    String API_OPERATION_AUTHORIZE = "AUTHORIZE";
    String OPERATION_DECLINE = "DECLINE";
    String API_OPERATION_DECLINE = "DECLINE";
    String OPERATION_ARCHIVE = "ARCHIVE";
    String API_OPERATION_ARCHIVE = "ARCHIVE";
    String UNKNOWN_OPERATION = "unknown_operation";


    /**
     * Return the API entity version for the local.
     */
    String getApiEntity(@NonNull String local);

    /**
     * Return the local entity version for the API.
     */
    String getLocalEntity(@NonNull String api);

    /**
     * Return the API operation version for the local.
     */
    String getApiOperation(@NonNull String local);

    /**
     * Return the local operation version for the API.
     */
    String getLocalOperation(@NonNull String api);
}
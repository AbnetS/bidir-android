package com.gebeya.mobile.bidir.data.permission;

import android.support.annotation.NonNull;

/**
 * Concrete class for the {@link PermissionHelper} interface
 */
public class BasePermissionHelper implements PermissionHelper {

    @Override
    public String getApiEntity(@NonNull String local) {
        switch (local) {
            case API_ENTITY_SCREENING:
                return ENTITY_SCREENING;
            case API_ENTITY_FORM:
                return ENTITY_FORM;
            case API_ENTITY_USER:
                return ENTITY_USER;
            case API_ENTITY_CLIENT:
                return ENTITY_CLIENT;
            case API_ENTITY_LOAN:
                return ENTITY_LOAN;
            case API_ENTITY_CROP:
                return ENTITY_CROP;
            case API_ENTITY_ACAT:
                return ENTITY_ACAT;
            case API_ENTITY_ACAT_PROCESSOR:
                return ENTITY_ACAT_PROCESSOR;
            case API_ENTITY_LOAN_PRODUCT:
                return ENTITY_LOAN_PRODUCT;
            case API_ENTITY_GROUP_LOAN:
                return ENTITY_GROUP_LOAN;
        }
        return UNKNOWN_ENTITY;
    }

    @Override
    public String getLocalEntity(@NonNull String api) {
        switch (api) {
            case ENTITY_SCREENING:
                return API_ENTITY_SCREENING;
            case ENTITY_FORM:
                return API_ENTITY_FORM;
            case ENTITY_USER:
                return API_ENTITY_USER;
            case ENTITY_CLIENT:
                return API_ENTITY_CLIENT;
            case ENTITY_LOAN:
                return API_ENTITY_LOAN;
            case ENTITY_CROP:
                return API_ENTITY_CROP;
            case ENTITY_ACAT:
                return API_ENTITY_ACAT;
            case ENTITY_ACAT_PROCESSOR:
                return API_ENTITY_ACAT_PROCESSOR;
            case ENTITY_LOAN_PRODUCT:
                return API_ENTITY_LOAN_PRODUCT;
            case ENTITY_GROUP_LOAN:
                return API_ENTITY_GROUP_LOAN;
        }
        return UNKNOWN_ENTITY;
    }

    @Override
    public String getApiOperation(@NonNull String local) {
        switch (local) {
            case API_OPERATION_CREATE:
                return OPERATION_CREATE;
            case API_OPERATION_VIEW:
                return OPERATION_VIEW;
            case API_OPERATION_READ:
                return OPERATION_READ;
            case API_OPERATION_VIEW_ALL:
                return OPERATION_VIEW_ALL;
            case API_OPERATION_UPDATE:
                return OPERATION_UPDATE;
            case API_OPERATION_AUTHORIZE:
                return OPERATION_AUTHORIZE;
            case API_OPERATION_DECLINE:
                return OPERATION_DECLINE;
            case API_OPERATION_ARCHIVE:
                return OPERATION_ARCHIVE;
        }
        return UNKNOWN_OPERATION;
    }

    @Override
    public String getLocalOperation(@NonNull String api) {
        switch (api) {
            case OPERATION_CREATE:
                return API_OPERATION_CREATE;
            case OPERATION_VIEW:
                return API_OPERATION_VIEW;
            case OPERATION_VIEW_ALL:
                return API_OPERATION_VIEW_ALL;
            case OPERATION_READ:
                return API_OPERATION_READ;
            case OPERATION_UPDATE:
                return API_OPERATION_UPDATE;
            case OPERATION_AUTHORIZE:
                return API_OPERATION_AUTHORIZE;
            case OPERATION_DECLINE:
                return API_OPERATION_DECLINE;
            case OPERATION_ARCHIVE:
                return API_OPERATION_ARCHIVE;
        }
        return UNKNOWN_OPERATION;
    }
}
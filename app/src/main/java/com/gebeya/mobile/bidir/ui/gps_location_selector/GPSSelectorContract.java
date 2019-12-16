package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.impl.util.location.mvp.LocationApiView;

/**
 * Contract interface for the GPS selector, used for ACAT modules.
 */
public interface GPSSelectorContract {

    /**
     * View interface for the GPS selector.
     */
    interface View extends BaseView<Presenter>, LocationApiView {
        void setTitle(@NonNull String title);
        void toggleNoLocationsLabel(boolean show);

        // GPS
        boolean hasGpsPermission();
        boolean needsGpsPermissionExplanation();
        void showGpsPermissionExplanation();
        void requestGpsPermission();
        void showGpsPermissionDeniedError();
        void requestGpsEnable();
        void openGpsSettings();
        void updateGpsLocation(@NonNull String location);
        void loadLocations();
        void refreshLocations();
        void scrollToPosition(int position);
        void refreshLocation(int position);
        void refreshRemovedLocation(int position);
        void toggleUpdate(boolean show);
        void setCounter(int count);
        void showSaveSuccessMessage();
        void setLocationType(boolean multiple);
        boolean hasAlreadyShownPlayServicesPrompt();
    }

    /**
     * Presenter interface for the GPS selector.
     */
    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull LocationItemView holder, int position);
        void onLocationSelected(int position);
        void onRemoveLocationPressed(int position);
        void onUpdateLocationPressed();
        int getLocationCount();
        void onAddNewLocationPressed();
        void onSaveLocationsPressed();
        void onLocationTypeChanged(boolean multiple);

        // GPS
        void onBackPressed();
        void onGpsPermissionRequestReturned(boolean granted);
        void startGps();
        void onGpsPermissionExplanationReturned(boolean granted);
        void onGpsEnableRequestReturned(boolean granted);
    }

    /**
     * Interface to represent a single location item.
     */
    interface LocationItemView {
        void toggleSelected(boolean selected);
        void setPosition(@NonNull String position);
        void setLocation(@NonNull String location);
    }
}
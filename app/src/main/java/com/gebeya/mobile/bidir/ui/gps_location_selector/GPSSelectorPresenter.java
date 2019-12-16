package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;
import com.gebeya.mobile.bidir.data.gpslocation.local.GPSLocationLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.location.GpsManager;
import com.gebeya.mobile.bidir.impl.util.location.GpsStateCallback;
import com.gebeya.mobile.bidir.impl.util.location.android.DefaultGpsManager;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesApi;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesGpsManager;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * GPS Selector presenter implementation.
 */
public class GPSSelectorPresenter implements GPSSelectorContract.Presenter, GpsStateCallback {

    private GPSSelectorContract.View view;

    @Inject CropACATLocalSource cropAcatLocal;
    @Inject GPSLocationLocalSource gpsLocationLocal;
    @Inject PlayServicesApi playServicesApi;

    @Inject DefaultGpsManager defaultGpsManager;
    @Inject PlayServicesGpsManager playServicesGpsManager;
    private GpsManager gpsManager;

    @Inject SchedulersProvider schedulers;

    private final String cropACATId;
    private final String acatApplicationId;

    private CropACAT cropACAT;
    private final List<GPSLocation> gpsLocations;
    private final List<LocationItem> multipleLocationItems;
    private final List<LocationItem> singleLocationItems;
    private final List<LocationItem> locationItems;

    private boolean multiple;
    private int selectedLocationPosition;

    public GPSSelectorPresenter(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        this.cropACATId = cropACATId;
        this.acatApplicationId = acatApplicationId;

        gpsLocations = new ArrayList<>();
        multipleLocationItems = new ArrayList<>();
        singleLocationItems = new ArrayList<>();
        locationItems = new ArrayList<>();
        selectedLocationPosition = -1;

        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        initializeGpsManager();
        gpsManager.addGpsStateCallback(this);

        checkGooglePlayServices();
        startGpsPermission();

        cropAcatLocal.getACATCrop(cropACATId, acatApplicationId)
                .flatMap(data -> {
                    cropACAT = data.get();
                    multiple = cropACAT.isGPSPolygon;
                    return gpsLocationLocal.getAllCropACATGPSLocations(cropACATId, acatApplicationId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    gpsLocations.clear();
                    gpsLocations.addAll(list);

                    loadLocationsData();
                });
    }

    private void initializeGpsManager() {
        final boolean missing = playServicesApi.missingPlayServices();
        gpsManager = missing ? defaultGpsManager : playServicesGpsManager;
    }

    private void checkGooglePlayServices() {
        if (playServicesApi.missingPlayServices()) {
            if (view.hasAlreadyShownPlayServicesPrompt()) return;

            if (playServicesApi.isUserResolvableError()) {
                final int status = playServicesApi.getStatus();
                view.showPlayServicesErrorDialog(status);
            }
        }
    }


    private void loadLocationsData() {
        generateLocationItems();
        view.toggleNoLocationsLabel(locationItems.isEmpty());
        view.loadLocations();
        view.setCounter(locationItems.size());
        view.setLocationType(multiple);
    }

    private void generateLocationItems() {
        locationItems.clear();
        multipleLocationItems.clear();
        singleLocationItems.clear();

        final int length = gpsLocations.size();
        for (int i = 0; i < length; i++) {
            final LocationItem item = new LocationItem(gpsLocations.get(i), i + 1);
            (multiple ? multipleLocationItems : singleLocationItems).add(item);
            locationItems.add(item);
        }
    }

    @Override
    public void onBindRowView(@NonNull GPSSelectorContract.LocationItemView holder, int position) {
        final LocationItem item = locationItems.get(position);
        final String location = String.format(Locale.getDefault(), "%.6f, %.6f",
                item.location.latitude, item.location.longitude
        );
        holder.setLocation(location);
        holder.setPosition(String.valueOf(item.position));
        holder.toggleSelected(position == selectedLocationPosition);
    }

    @Override
    public void onLocationSelected(int position) {
        if (selectedLocationPosition == -1) {
            selectedLocationPosition = position;
        } else {
            selectedLocationPosition = selectedLocationPosition == position ? -1 : position;
        }

        view.toggleUpdate(selectedLocationPosition != -1);
        view.refreshLocations();
    }

    private void refreshLocationPositions() {
        final List<LocationItem> target = multiple ? multipleLocationItems : singleLocationItems;
        final int length = target.size();
        for (int i = 0; i < length; i++) {
            final LocationItem item = target.get(i);
            item.position = i + 1;
        }

        locationItems.clear();
        locationItems.addAll(target);
    }

    @Override
    public void onUpdateLocationPressed() {
        if (selectedLocationPosition == -1) return;

        final LocationItem item = locationItems.get(selectedLocationPosition);
        final double[] position = gpsManager.getPosition();
        item.location.latitude = position[0];
        item.location.longitude = position[1];

        view.refreshLocation(selectedLocationPosition);
    }

    @Override
    public int getLocationCount() {
        return locationItems.size();
    }

    @Override
    public void onAddNewLocationPressed() {
        final LocationItem item = generateNewLocationItem();
        addItem(item);
        view.toggleNoLocationsLabel(false);
        view.refreshLocations();
        view.setCounter(locationItems.size());
        view.scrollToPosition(locationItems.size() - 1);
    }

    private void addItem(@NonNull LocationItem item) {
        locationItems.clear();
        if (multiple) {
            multipleLocationItems.add(item);
            locationItems.addAll(multipleLocationItems);
        } else {
            singleLocationItems.clear();
            singleLocationItems.add(item);
            locationItems.addAll(singleLocationItems);
        }
    }

    private LocationItem generateNewLocationItem() {
        final GPSLocation gpsLocation = generateNewGPSLocation();
        final int size;
        if (multiple) {
            size = multipleLocationItems.size() + 1;
        } else {
            size = 1;
        }
        return new LocationItem(gpsLocation, size);
    }

    private GPSLocation generateNewGPSLocation() {
        final GPSLocation location = new GPSLocation();

        location.acatApplicationID = acatApplicationId;
        location.cropACATID = cropACATId;
        final double[] position = gpsManager.getPosition();
        location.latitude = position[0];
        location.longitude = position[1];

        gpsLocations.add(location);

        return location;
    }

    @Override
    public void onRemoveLocationPressed(int position) {
        final List<LocationItem> target = multiple ? multipleLocationItems : singleLocationItems;
        if (!target.isEmpty()) {
            target.remove(position);
        }

        if (position == selectedLocationPosition || target.isEmpty()) {
            selectedLocationPosition = -1;
            view.toggleUpdate(false);
        }

        refreshLocationPositions();
        view.refreshRemovedLocation(position);

        view.refreshLocations();
        view.toggleNoLocationsLabel(locationItems.isEmpty());
        if (locationItems.isEmpty()) {
            selectedLocationPosition = -1;
            view.toggleUpdate(false);
        }

        view.setCounter(locationItems.size());
    }

    @Override
    @SuppressLint("CheckResult")
    public void onSaveLocationsPressed() {
        final List<GPSLocation> locations = gatherLocations();
        cropACAT.isGPSPolygon = multiple;

        cropACAT.uploaded = false;
        cropACAT.modified = true;
        cropACAT.updatedAt = new DateTime();

        gpsLocationLocal.putAll(locations, cropACATId, acatApplicationId)
                .andThen(cropAcatLocal.put(cropACAT))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(saved -> {
                    view.showSaveSuccessMessage();
                    view.close();
                });
    }

    @Override
    public void onLocationTypeChanged(boolean multiple) {
        this.multiple = multiple;

        final List<LocationItem> target = multiple ? multipleLocationItems : singleLocationItems;
        locationItems.clear();
        locationItems.addAll(target);

        if (selectedLocationPosition != -1) {
            selectedLocationPosition = getNewCurrentPosition(locationItems);
        }

        view.refreshLocations();
        view.setCounter(locationItems.size());
        view.toggleNoLocationsLabel(locationItems.isEmpty());
    }

    private int getNewCurrentPosition(@NonNull List<LocationItem> list) {
        final int size = list.size();
        if (size == 0) return -1;
        return selectedLocationPosition < size ? selectedLocationPosition : size - 1;
    }

    private List<GPSLocation> gatherLocations() {
        final List<GPSLocation> locations = new ArrayList<>();
        final int length = locationItems.size();
        for (int i = 0; i < length; i++) {
            final LocationItem item = locationItems.get(i);
            locations.add(item.location);
        }

        return locations;
    }

    private void startGpsPermission() {
        final boolean hasGpsPermission = view.hasGpsPermission();
        if (!hasGpsPermission) {
            if (view.needsGpsPermissionExplanation()) {
                view.showGpsPermissionExplanation();
            } else {
                view.requestGpsPermission();
            }
        } else {
            startGps();
        }
    }

    @Override
    public void onGpsPermissionExplanationReturned(boolean granted) {
        if (granted) {
            view.requestGpsPermission();
        } else {
            view.showGpsPermissionDeniedError();
        }
    }

    @Override
    public void onGpsPermissionRequestReturned(boolean granted) {
        if (granted) {
            startGps();
        } else {
            view.showGpsPermissionDeniedError();
        }
    }

    @Override
    public void onGpsEnableRequestReturned(boolean granted) {
        if (granted) {
            view.openGpsSettings();
        }
    }

    @Override
    public void onGpsStateChanged(boolean listening) {

    }

    @Override
    public void onGpsLocationUpdated(double latitude, double longitude) {
        final String location = formatLocation(latitude, longitude);
        view.updateGpsLocation(location);
    }

    @Override
    public void startGps() {
        if (!view.hasGpsPermission()) return;

        if (gpsManager.enabled()) {
            view.updateGpsLocation(formatLocation(0, 0));
            gpsManager.start();
        } else {
            view.requestGpsEnable();
        }
    }

    private String formatLocation(double latitude, double longitude) {
        return String.format(Locale.getDefault(), "%.6f, %.6f",
                latitude, longitude
        );
    }

    @Override
    public void onBackPressed() {
        view.close();
    }

    @Override
    public void attachView(GPSSelectorContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        gpsManager.removeGspStateCallback();
        view = null;
    }

    @Override
    public GPSSelectorContract.View getView() {
        return view;
    }
}

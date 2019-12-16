package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.question.QuestionDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.question.QuestionDialogCallback;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * view implementation for the {@link GPSSelectorContract.View} interface as a Fragment.
 */
public class GPSSelectorFragment extends BaseFragment implements GPSSelectorContract.View, QuestionDialogCallback {

    private static final int REQUEST_GPS_PERMISSION = 1000;
    private static final int REQUEST_PLAY_SERVICES = 2000;

    private static final int QUESTION_MODE_REQUEST_GPS_PERMISSION = 100;
    private static final int QUESTION_MODE_REQUEST_GPS_ENABLE = 101;

    private static final String ARG_SHOWN_PLAY_SERVICES_PROMPT = "SHOWN_PLAY_SERVICES_PROMPT";

    private int permissionRequestCode;
    private int[] grantResults;
    private boolean fromGpsSettings;
    private int questionMode;
    private boolean shownPlayServicesPrompt = false;

    private GPSSelectorContract.Presenter presenter;

    private Toolbar toolbar;
    private TextView saveLocationsMenuLabel;
    private TextView counterLabel;
    private RecyclerView recyclerView;
    private LocationItemAdapter adapter;
    private TextView noLocationsLabel;

    private RadioGroup locationTypeRadioGroup;

    private View currentLocationIcon;
    private TextView currentLocationLabel;

    private Button updateButton;

    private QuestionDialog questionDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) return;
        shownPlayServicesPrompt = savedInstanceState.getBoolean(ARG_SHOWN_PLAY_SERVICES_PROMPT, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(ARG_SHOWN_PLAY_SERVICES_PROMPT, true);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gps_selector, container, false);

        toolbar = getView(R.id.gpsSelectorToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackPressed());

        saveLocationsMenuLabel = getTv(R.id.gpsSelectorMenuSaveLocations);
        saveLocationsMenuLabel.setOnClickListener(v -> presenter.onSaveLocationsPressed());
        counterLabel = getTv(R.id.gpsSelectorLocationsCounterLabel);

        recyclerView = getView(R.id.gpsSelectorLocationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParent()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LocationItemAdapter(presenter, inflater);

        noLocationsLabel = getTv(R.id.gpsSelectorNoLocationsLabel);

        locationTypeRadioGroup = getView(R.id.gpsSelectorLocationTypeRadioGroup);
        locationTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) ->
                presenter.onLocationTypeChanged(checkedId == R.id.gpsSelectorLocationTypeMultipleChoice)
        );

        currentLocationIcon = getView(R.id.gpsSelectorCurrentLocationIcon);
        currentLocationLabel = getTv(R.id.gpsSelectorCurrentLocationLabel);

        getBt(R.id.gpsSelectorAddNewButton).setOnClickListener(v -> presenter.onAddNewLocationPressed());

        updateButton = getBt(R.id.gpsSelectorUpdateButton);
        updateButton.setOnClickListener(v -> presenter.onUpdateLocationPressed());

        return root;
    }

    @Override
    public void setTitle(@NonNull String title) {
        final String s = getString(R.string.gps_selector_title, title);
        toolbar.setTitle(s);
    }

    @Override
    public void toggleNoLocationsLabel(boolean show) {
        noLocationsLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean hasGpsPermission() {
        int check = ContextCompat.checkSelfPermission(getParent(), Manifest.permission.ACCESS_FINE_LOCATION);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean needsGpsPermissionExplanation() {
        return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showGpsPermissionExplanation() {
        final String title = getString(R.string.gps_request_explanation_title);
        final String message = getString(R.string.gps_request_explanation_message);
        questionMode = QUESTION_MODE_REQUEST_GPS_PERMISSION;

        questionDialog = QuestionDialog.newInstance(title, message);
        questionDialog.setCallback(this);
        questionDialog.show(getFragmentManager(), null);
    }

    @Override
    public void onQuestionPositiveClicked() {
        onQuestionReturned(true);
    }

    @Override
    public void onQuestionNegativeClicked() {
        onQuestionReturned(false);
    }

    private void onQuestionReturned(boolean enabled) {
        if (questionMode == QUESTION_MODE_REQUEST_GPS_PERMISSION) {
            presenter.onGpsPermissionExplanationReturned(enabled);
        } else {
            presenter.onGpsEnableRequestReturned(enabled);
        }
        questionMode = 0;
    }

    @Override
    public void requestGpsPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_GPS_PERMISSION
        );
    }

    @Override
    public void showGpsPermissionDeniedError() {
        toast(R.string.gps_request_denied_message);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void requestGpsEnable() {
        final String title = getString(R.string.gps_enable_title);
        final String message = getString(R.string.gps_enable_message);
        questionMode = QUESTION_MODE_REQUEST_GPS_ENABLE;

        questionDialog = QuestionDialog.newInstance(title, message);
        questionDialog.setCallback(this);
        questionDialog.show(getFragmentManager(), null);
    }

    @Override
    public void openGpsSettings() {
        fromGpsSettings = true;
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void updateGpsLocation(@NonNull String location) {
        currentLocationLabel.setText(location);
    }

    @Override
    public void loadLocations() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshLocations() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void scrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void refreshLocation(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void refreshRemovedLocation(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void toggleUpdate(boolean show) {
        updateButton.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setCounter(int count) {
        counterLabel.setText(String.valueOf(count));
        counterLabel.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showSaveSuccessMessage() {
        toast(R.string.gps_selector_locations_saved_message);
    }

    @Override
    public void setLocationType(boolean multiple) {
        locationTypeRadioGroup.check(multiple ? R.id.gpsSelectorLocationTypeMultipleChoice : R.id.gpsSelectorLocationTypeSingleChoice);
    }

    @Override
    public boolean hasAlreadyShownPlayServicesPrompt() {
        return shownPlayServicesPrompt;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionRequestCode = requestCode;
        this.grantResults = grantResults;
    }

    @Override
    public void showPlayServicesErrorDialog(int status) {
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final Dialog errorDialog = api.getErrorDialog(getActivity(), status, REQUEST_PLAY_SERVICES);
        errorDialog.show();
        shownPlayServicesPrompt = true;
    }

    @Override
    public void attachPresenter(GPSSelectorContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);

        if (permissionRequestCode == REQUEST_GPS_PERMISSION) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            presenter.onGpsPermissionRequestReturned(granted);
            permissionRequestCode = 0;
        }

        if (fromGpsSettings) {
            presenter.startGps();
            fromGpsSettings = false;
        }

        presenter.start();
    }

    @Override
    public void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void close() {
        getParent().finish();
    }
}

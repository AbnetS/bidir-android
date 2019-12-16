package com.gebeya.mobile.bidir.ui.questions.acat.acatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.service.ServiceSelectorCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.service.ServiceSelectorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.gps_location_selector.GPSSelectorActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel K. on 5/13/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATPreliminaryInfoFragment extends BaseFragment implements
        ACATPreliminaryInfoContract.View,
        ErrorDialogCallback,
        ServiceSelectorCallback {

    public static ACATPreliminaryInfoFragment newInstance() {
        return new ACATPreliminaryInfoFragment();
    }

    private ACATPreliminaryInfoContract.Presenter presenter;

    private Toolbar toolbar;
    private TextView menuSave, gpsLocationLabel;

    private RecyclerView recyclerView;
    private ACATPreliminaryInfoAdapter adapter;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;
    private ServiceSelectorDialog serviceSelectorDialog;

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_preliminary_info, container, false);

        toolbar = getView(R.id.acatPreliminaryInfoToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);

        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        menuSave = getTv(R.id.acatPreliminaryInfoMenuSave);
        menuSave.setOnClickListener(v -> presenter.onSaveClicked());

        recyclerView = getView(R.id.acatPreliminaryInfoRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);
        adapter = new ACATPreliminaryInfoAdapter(presenter);

        return root;
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void toggleSaveButton(boolean show) {
        menuSave.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void openCostEstimationActivity(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATCostEstimationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCostEstimationActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void openCropListActivity(@NonNull String clientId, @NonNull String clientACATId, @Nullable String groupId) {
        final Intent intent = new Intent(getActivity(), ACATCropItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ACAT_ID, clientACATId);
        intent.putExtra(ACATCropItemActivity.ARG_GROUP_ID, groupId);
        startActivity(intent);
    }

    @Override
    public void openNonFinancialServiceDialog(int position, @NonNull List<String> services) {
        serviceSelectorDialog = ServiceSelectorDialog.newInstance(position, new ArrayList<>(services));
        serviceSelectorDialog.setCallback(ACATPreliminaryInfoFragment.this);
        serviceSelectorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void refreshItem(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openGPSSelector(@NonNull String cropACATId, @NonNull String acatApplicationId) {
        final Intent intent = new Intent(getContext(), GPSSelectorActivity.class);

        intent.putExtra(GPSSelectorActivity.ARG_CROP_ACAT_ID, cropACATId);
        intent.putExtra(GPSSelectorActivity.ARG_ACAT_APPLICATION_ID, acatApplicationId);

        startActivity(intent);
    }

    @Override
    public void onServicesSelected(@NonNull List<String> services, int position) {
        presenter.onNonFinancialResourceChanged(services, position);
    }

    @Override
    public void onServiceSelectionCanceled(int position) {
        presenter.onNonFinancialResourcesDismissed(position);
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void attachPresenter(ACATPreliminaryInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showCrops() {
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setCroppingArea(@NonNull String croppingArea) {
        // TODO: You might need to change this implementation.
    }


    @Override
    public void setFirstExpenseMonth(@NonNull String firstExpenseMonth) {
        // TODO: Change this too.
    }

    @Override
    public void showMissingCroppingAreaError() {

    }

    @Override
    public void showUpdatingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.acat_preliminary_info_updating_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideUpdatingProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;
        if (message == null) {
            message = getString(R.string.preliminary_info_update_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.questions_error_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideError() {
        if (errorDialog != null) {
            try {
                errorDialog.dismiss();
                errorDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showUpdateSuccessMessage() {
        toast(getString(R.string.questions_update_success_message));
    }

    @Override
    public void showLoadingProgress() {
        if (waitingDialog == null) return;
        waitingDialog = WaitingDialog.newInstance(getString(R.string.preliminary_info_loading_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideLoadingProgress() {

        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachView();
    }

    @Override
    public void close() {
        getParent().finish();
    }
}

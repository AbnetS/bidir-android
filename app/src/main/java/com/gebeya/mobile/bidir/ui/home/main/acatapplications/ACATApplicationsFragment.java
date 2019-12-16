package com.gebeya.mobile.bidir.ui.home.main.acatapplications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATSyncService;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryActivity;
import com.gebeya.mobile.bidir.ui.home.main.groupedacat.GroupedACATActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatapplication.ACATPreliminaryInfoActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat.PreliminaryInformationActivity;

/**
 * Created by Samuel K. on 5/9/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATApplicationsFragment extends BaseFragment implements ACATApplicationsContract.View,
        ErrorDialogCallback {

    private ACATApplicationsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView noACATApplicationLabel;
    private TextView noGroupACATApplicationLabel;
    private ACATApplicationRecyclerViewAdapter adapter;
    private GroupedACATRecyclerViewAdapter groupedAdapter;

    private TextView individualLabel;
    private TextView groupLabel;

    private TextView acatsFilter;
    private PopupMenu individualPopupMenu, groupedPopupMenu;
    private Context wrapper;

    private View syncRoot;
    private View syncIcon;
    private View syncProgress;
    private View syncStatus;

    private RecordLoanDialog recordLoanDialog;
    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    private View downloadProgressContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter(new ACATApplicationsPresenter());

        // Allows loading of Vector images on pre API 21 devices
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_applications, container, false);

        recyclerView = getView(R.id.acatApplicationsRecyclerView);
        noACATApplicationLabel = getTv(R.id.acatApplicationsNoApplicationsLabel);
        noGroupACATApplicationLabel = getTv(R.id.groupAcatApplicationsNoApplicationsLabel);
        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);

        individualLabel = getTv(R.id.loanTypeIndividualLabel);
        groupLabel = getTv(R.id.loanTypeGroupLabel);

        acatsFilter = getTv(R.id.acatsFragmentFilterLabel);
        acatsFilter.setOnClickListener(v -> presenter.onACATsFilterClicked());

        wrapper = new ContextThemeWrapper(getContext(), R.style.popupMenuStyle);
        individualPopupMenu = new PopupMenu(wrapper, acatsFilter);
        individualPopupMenu.inflate(R.menu.acats_filter_menu);

        groupedPopupMenu = new PopupMenu(wrapper, acatsFilter);
        groupedPopupMenu.inflate(R.menu.group_acats_filter_menu);

        individualLabel.setOnClickListener(view -> {
            presenter.onIndividualButtonPressed();
            individualPopupMenu.getMenu().getItem(0).setChecked(true);
        });
        groupLabel.setOnClickListener(view -> {
            presenter.onGroupButtonPressed();
            groupedPopupMenu.getMenu().getItem(0).setChecked(true);
        });

        adapter = new ACATApplicationRecyclerViewAdapter(presenter);
        groupedAdapter = new GroupedACATRecyclerViewAdapter(presenter);

        syncRoot = getView(R.id.syncIndicatorRoot);
        syncIcon = getView(R.id.syncIndicatorIcon);
        syncProgress = getView(R.id.syncIndicatorProgressWheel);
        syncStatus = getView(R.id.syncIndicatorStatusView);
        syncRoot.setOnClickListener(v -> presenter.onSyncPressed());

        downloadProgressContainer = getView(R.id.downloadProgressWheelContainer);
        downloadProgressContainer.setOnClickListener(view -> { });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
        individualPopupMenu.getMenu().getItem(0).setChecked(true);
        groupedPopupMenu.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void attachPresenter(ACATApplicationsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getParent().finish();
    }

    @Override
    public void showACATApplications() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshACATApplications() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void toggleNoACATApplications(boolean show) {
        noACATApplicationLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleNoGroupACATApplications(boolean show) {
        noGroupACATApplicationLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void openACATApplicationInitializer(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), PreliminaryInformationActivity.class);
        intent.putExtra(PreliminaryInformationActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    @Override
    public void openACATPreliminaryScreen(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATPreliminaryInfoActivity.class);
        intent.putExtra(ACATPreliminaryInfoActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    @Override
    public void openCropList(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATCropItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.loan_approved_update_error);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.questions_error_title),
                message,
                extra
        );
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_approved_updating_message));
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
    public void showUpdateSuccessfulMessage() {
        toast(R.string.update_success_message);
    }

    @Override
    public void toggleIndividualButton(boolean isEnabled) {
        if (isEnabled) {
            individualLabel.setTextColor(getResources().getColor(R.color.green));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_selected, 0, 0);
        } else {
            individualLabel.setTextColor(getResources().getColor(R.color.gray_light));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_default, 0, 0);
        }
    }

    @Override
    public void toggleGroupButton(boolean isEnabled) {
        if (isEnabled) {
            groupLabel.setTextColor(getResources().getColor(R.color.green));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_selected, 0, 0);
        } else {
            groupLabel.setTextColor(getResources().getColor(R.color.gray_light));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_default, 0, 0);
        }
    }

    @Override
    public void openGroupedACATScreen(@NonNull Group group, @NonNull String title) {
        final Intent intent = new Intent(getActivity(), GroupedACATActivity.class);
        intent.putExtra(GroupedACATActivity.ARG_GROUP_ID, group._id);
        intent.putExtra(GroupedACATActivity.ARG_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void showGrouped() {
        recyclerView.setAdapter(groupedAdapter);
    }

    @Override
    public void showDownloadProgress() {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> downloadProgressContainer.setVisibility(View.VISIBLE));
        }
    }

    @Override
    public void hideDownloadProgress() {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> downloadProgressContainer.setVisibility(View.GONE));
        }
    }

    @Override
    public void openIndividualFilterMenu() {
        individualPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadACATs();
                    return true;
                case R.id.newACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadNewACATs();
                    return true;
                case R.id.inProgressACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadInprogressACATs();
                    return true;
                case R.id.submittedACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadSubmittedACATs();
                    return true;
                case R.id.resubmittedACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadResubmittedACATs();
                    return true;
                case R.id.approvedACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadApprovedACATs();
                    return true;
                case R.id.declinedACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadDeclinedForReviewACATs();
                    return true;

            }
            return false;
        });

        individualPopupMenu.show();
    }

    @Override
    public void openGroupedFilterMenu() {
        groupedPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedApplications();
                    return true;
                case R.id.newGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedNewACATs();
                    return true;
                case R.id.inProgressGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedInprogressACATs();
                    return true;
                case R.id.submittedGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedSubmittedACATs();
                    return true;
                case R.id.resubmittedGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedResubmittedACATs();
                    return true;
                case R.id.approvedGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedApprovedACATs();
                    return true;
                case R.id.declinedGroupACATsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedDeclinedForReviewACATs();
                    return true;

            }
            return false;
        });

        groupedPopupMenu.show();
    }

    @Override
    public void openPopUpMenu(View view, @NonNull Client client) {
        Context wrapper = new ContextThemeWrapper(getContext(),R.style.popupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.inflate(R.menu.acat_option_menu);

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.summaryMenu:
                    final Intent intent = new Intent(getActivity(), CropSummaryActivity.class);
                    intent.putExtra(CropSummaryActivity.ARG_CLIENT_ID, client._id);
                    startActivity(intent);
                    break;
            }
            return false;
        });

        popup.show();
    }

    @Override
    public void showSyncInProgress() {
        syncIcon.setVisibility(View.GONE);
        syncProgress.setVisibility(View.VISIBLE);
        syncStatus.setVisibility(View.GONE);
    }

    @Override
    public void showSyncIdle(boolean hasUnSyncedData) {
        syncIcon.setVisibility(View.VISIBLE);
        syncProgress.setVisibility(View.GONE);
        syncStatus.setVisibility(hasUnSyncedData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startSyncService() {
        final Context context = getContext();
        if (context == null) return;

        context.startService(new Intent(context, ACATSyncService.class));
    }

    @Override
    public void onErrorDialogDismissed() {

    }
}

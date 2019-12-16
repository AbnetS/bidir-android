package com.gebeya.mobile.bidir.ui.home.main.clients;

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
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.client.ClientSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening.ComplexScreeningSyncService;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.creategroup.CreateGroupCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.creategroup.CreateGroupDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.loanpaid.SetLoanPaidCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.loanpaid.SetLoanPaidDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.newgroupcycle.GroupLoanCycleCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.newgroupcycle.GroupLoanCycleDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanCallBack;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.home.main.groupedclient.GroupedClientActivity;
import com.gebeya.mobile.bidir.ui.profile.ProfileActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.register.RegisterActivity;

import javax.inject.Inject;

/**
 * View implementation of the Clients module
 */
public class ClientsFragment extends BaseFragment implements ClientsContract.View,
        ErrorDialogCallback,
        CreateGroupCallback,
        SetLoanPaidCallback,
        RecordLoanCallBack,
        GroupLoanCycleCallback {

    private ClientsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView noClientsLabel;
    private TextView noGroupsLabel;
    private TextView individualLabel;
    private TextView groupLabel;
    private TextView clientsFilter;

    private IndividualClientsRecyclerViewAdapter individualAdapter;
    private GroupedClientsRecyclerViewAdapter groupAdapter;

    private View clientsAddNewFab;

    private View syncRoot;
    private View syncIcon;
    private View syncProgress;
    private View syncStatus;

    private PopupMenu individualPopupMenu, groupPopupMenu;
    private Context wrapper;

    private CreateGroupDialog groupDialog;
    private SetLoanPaidDialog loanPaidDialog;
    private RecordLoanDialog recordLoanDialog;
    private GroupLoanCycleDialog groupLoanCycleDialog;

    @Inject WaitingDialog waitingDialog;
    @Inject ErrorDialog errorDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter(new ClientsPresenter());

        // Allows loading of Vector images on pre API 21 devices
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_clients, container, false);

        recyclerView = getView(R.id.clientsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);
        individualAdapter = new IndividualClientsRecyclerViewAdapter(presenter);
        groupAdapter = new GroupedClientsRecyclerViewAdapter(presenter);

        getView(R.id.clientsAddNewFab).setOnClickListener(fabClickListener);
        clientsAddNewFab = getView(R.id.clientsAddNewFabContainer);
        clientsAddNewFab.setOnClickListener(fabClickListener);
        noClientsLabel = getTv(R.id.clientsNoClientsLabel);
        noGroupsLabel = getTv(R.id.clientsNoGroupLabel);

        clientsFilter = getTv(R.id.clientsFragmentFilterLabel);
        clientsFilter.setOnClickListener(v -> presenter.onClientsFilterClicked());

        wrapper = new ContextThemeWrapper(getContext(),R.style.popupMenuStyle);
        individualPopupMenu = new PopupMenu(wrapper, clientsFilter);
        individualPopupMenu.inflate(R.menu.clients_filter_menu);

        groupPopupMenu = new PopupMenu(wrapper, clientsFilter);
        groupPopupMenu.inflate(R.menu.group_clients_filter_menu);

        individualLabel = getTv(R.id.loanTypeIndividualLabel);
        groupLabel = getTv(R.id.loanTypeGroupLabel);

//        individualLabel.setVisibility(View.GONE);
//        groupLabel.setVisibility(View.GONE);

        individualLabel.setOnClickListener(view -> {
            presenter.onIndividualButtonPressed();
            individualPopupMenu.getMenu().getItem(0).setChecked(true);
        });
        groupLabel.setOnClickListener(view -> {
            presenter.onGroupButtonPressed();
            groupPopupMenu.getMenu().getItem(0).setChecked(true);
        });

        syncRoot = getView(R.id.syncIndicatorRoot);
        syncIcon = getView(R.id.syncIndicatorIcon);
        syncProgress = getView(R.id.syncIndicatorProgressWheel);
        syncStatus = getView(R.id.syncIndicatorStatusView);
        syncRoot.setOnClickListener(v -> presenter.onSyncPressed());

        return root;
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onAddButtonClicked();
        }
    };

    @Override
    public void openAddClientScreen() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void openAddGroupScreen() {
        groupDialog = CreateGroupDialog.newInstance();
        groupDialog.setCallBack(this);
        groupDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void openConfirmationDialog(@Nullable Client client, @Nullable Group group) {
        loanPaidDialog = SetLoanPaidDialog.newInstance(client, group);
        loanPaidDialog.setCallback(this);
        loanPaidDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingClientProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.client_status_updating_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingGroupProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.group_updating_status_label));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_approved_updating_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showCompleteMessage() {
        toast(R.string.group_creation_success_message);
    }

    @Override
    public void onCreate(@NonNull String groupCode, int memberCount, double loanAmount) {
        presenter.onCreateReturned(groupCode, memberCount, loanAmount);
    }

    @Override
    public void toggleAddClientsButton(boolean show) {
        clientsAddNewFab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void openClient(@NonNull Client client) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.ARG_CLIENT_ID, client._id);
        startActivity(intent);
    }

    @Override
    public void toggleIndividualMenuItems(@NonNull PopupMenu popupMenu, @NonNull Client client) {
        switch (client.status) {
            case ClientParser.LOAN_GRANTED:
                popupMenu.getMenu().findItem(R.id.recordApprovedLoanMenu).setEnabled(false);
                break;
            case ClientParser.LOAN_PAID:
                popupMenu.getMenu().findItem(R.id.recordApprovedLoanMenu).setEnabled(false);
                popupMenu.getMenu().findItem(R.id.monitorMenu).setEnabled(false);
                popupMenu.getMenu().findItem(R.id.loanPaidMenu).setEnabled(false);
                break;
            default:
                popupMenu.getMenu().findItem(R.id.recordApprovedLoanMenu).setEnabled(true);
                popupMenu.getMenu().findItem(R.id.monitorMenu).setEnabled(true);
                popupMenu.getMenu().findItem(R.id.loanPaidMenu).setEnabled(true);
                break;
        }
    }

    @Override
    public void openPopUpMenu(@NonNull TextView view, @NonNull Client client) {
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.inflate(R.menu.client_option_menu);

        toggleIndividualMenuItems(popup, client);


        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.recordApprovedLoanMenu:
                    if (!client.status.equalsIgnoreCase(ClientParser.ACAT_AUTHORIZED)) {
                        toast(R.string.acat_not_authorized);
                        break;
                    }
                    recordLoanDialog = RecordLoanDialog.newInstance(client._id);
                    recordLoanDialog.setCallBack(this);
                    recordLoanDialog.show(getChildFragmentManager(), null);
                    break;
                case R.id.monitorMenu:
                    if (!client.status.equalsIgnoreCase(ClientParser.LOAN_GRANTED)) {
                        toast(R.string.acat_loan_not_recorded);
                        break;
                    }
                    final Intent monitor = new Intent(getActivity(), ACATCropItemActivity.class);
                    monitor.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, client._id);
                    monitor.putExtra(ACATCropItemActivity.IS_MONITORING, true);
                    startActivity(monitor);
                    break;
                case R.id.loanPaidMenu:
                    if (!client.status.equalsIgnoreCase(ClientParser.LOAN_GRANTED)) {
                        toast(R.string.acat_loan_not_recorded);
                        break;
                    }
                    presenter.onLoanPaidClicked(client);
                    break;
                case R.id.newLoanCycleMenu:
                    if (!client.status.equalsIgnoreCase(ClientParser.LOAN_PAID)) {
                        toast(R.string.acat_loan_not_paid_yet_lable);
                        break;
                    }
                    presenter.onNewLoanCycleClicked(client);
                    break;

            }
            return false;
        });
        popup.show();
    }

    @Override
    public void openIndividualFilterMenu() {
        individualPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadIndividualClients();
                    return true;
                case R.id.newClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadNewClients();
                    return true;
                case R.id.screeningClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadScreeningClients();
                    return true;
                case R.id.loanClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadLoanClients();
                    return true;
                case R.id.acatClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadACATClients();
                    return true;
                case R.id.grantedClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGrantedClients();
                    return true;
                case R.id.paidClientsMenu:
                    menuItem.setChecked(true);
                    presenter.loadPaidClients();
                    return true;

            }

            return false;
        });
        individualPopupMenu.show();
    }

    @Override
    public void openGroupedFilterMenu() {
        groupPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allGroupsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedClients();
                    return true;
                case R.id.newGroupsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedNewClients();
                    return true;
                case R.id.screeningGroupsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedScreeningClients();
                    return true;
                case R.id.loanGroupsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedLoanClients();
                    return true;
                case R.id.acatGroupsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedACATClients();
                    return true;
            }
            return false;
        });

        groupPopupMenu.show();
    }

    @Override
    public void toggleGroupMenuItems(@NonNull PopupMenu popupMenu, @NonNull Group group) {
        switch (group.groupStatus) {
            case GroupParser.LOAN_PAID:
                popupMenu.getMenu().findItem(R.id.loanPaidMenu).setEnabled(false);
                break;
            default:
                popupMenu.getMenu().findItem(R.id.loanPaidMenu).setEnabled(true);
                break;
        }
    }

    @Override
    public void openGroupPopUpMenu(@NonNull TextView anchor, @NonNull Group group) {
        Context wrapper = new ContextThemeWrapper(getContext(),R.style.popupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, anchor);
        popup.inflate(R.menu.group_option_menu);

        toggleGroupMenuItems(popup, group);

        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.loanPaidMenu:
                    if (!group.groupStatus.equalsIgnoreCase(GroupParser.LOAN_GRANTED)) {
                        toast(R.string.grouped_acat_applications_fragment_loan_not_granted_label);
                        break;
                    }
                    presenter.onGroupLoanPaidClicked(group);
                    break;
                case R.id.newLoanCycleMenu:
                    if (!group.groupStatus.equalsIgnoreCase(GroupParser.LOAN_PAID)) {
                        toast(R.string.grouped_acat_applications_fragment_loan_not_paid_label);
                        break;
                    }
                    groupLoanCycleDialog = GroupLoanCycleDialog.newInstance(group._id);
                    groupLoanCycleDialog.setCallback(this);
                    groupLoanCycleDialog.show(getChildFragmentManager(), null);
                    break;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public void openGroupedApplicationScreen(@NonNull Group group, @NonNull String title) {
        Intent intent = new Intent(getContext(), GroupedClientActivity.class);
        intent.putExtra(GroupedClientActivity.ARG_GROUP_ID, group._id);
        intent.putExtra(GroupedClientActivity.ARG_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.screening_creating_progress_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showGroupLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.group_progress_label));
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
    public void showTaskSuccessfulMessage() {
        toast(R.string.new_loan_cycle_started);
        toast(R.string.screening_creation_successful_message);
    }

    @Override
    public void showStatusChangedMessage() {
        toast(R.string.client_status_update_success_message);
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.loan_cycle_creation_error);
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

        final Intent intent = new Intent(context, ClientSyncService.class);
        context.startService(intent);
    }

    @Override
    public void startScreeningSyncService() { // this is for new loan cycle to fetch the screening.
        final Context context = getContext();
        if (context == null) return;

        context.startService(new Intent(context, ComplexScreeningSyncService.class));
    }

    @Override
    public void toggleIndividualButton(boolean enabled) {
        if (enabled) {
            individualLabel.setTextColor(getResources().getColor(R.color.green));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_selected, 0, 0);
        } else {
            individualLabel.setTextColor(getResources().getColor(R.color.gray_light));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_default, 0, 0);
        }
    }

    @Override
    public void toggleGroupButton(boolean enabled) {
        if (enabled) {
            groupLabel.setTextColor(getResources().getColor(R.color.green));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_selected, 0, 0);
        } else {
            groupLabel.setTextColor(getResources().getColor(R.color.gray_light));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_default, 0, 0);
        }
    }

    @Override
    public void showClients() {
        recyclerView.setAdapter(individualAdapter);
    }

    @Override
    public void showGrouped() {
        recyclerView.setAdapter(groupAdapter);
    }

    @Override
    public void refreshClients() {
        individualAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshGrouped() {
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    public void toggleNoClientsLabel(boolean show) {
        noClientsLabel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void toggleNoGroupsLabel(boolean show) {
        noGroupsLabel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void attachPresenter(ClientsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
//        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);         // TODO: Test for NullPointerException (the crazy bug on one of the tablets)
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        individualPopupMenu.getMenu().getItem(0).setChecked(true);
        groupPopupMenu.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void close() {
        getParent().finish();
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void onLoanPaidCallBack(@Nullable Client client, @Nullable Group group) {
        if (client != null)
            presenter.onSetLoanPaidReturned(client);
        if (group != null)
            presenter.onGroupSetLoanPaidReturned(group);
    }

    @Override
    public void onRecord(@NonNull String loanApproved, @NonNull String clientId) {
        presenter.onRecordReturned(loanApproved, clientId);
    }

    @Override
    public void onGroupLoanCycleCallback(@NonNull String groupId, @NonNull String loanAmount) {
        presenter.onGroupNewLoanCycleClicked(groupId, loanAmount);
    }
}
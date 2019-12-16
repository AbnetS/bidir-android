package com.gebeya.mobile.bidir.ui.home.main.groupedclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.addgroupmember.AddGroupMember;
import com.gebeya.mobile.bidir.ui.common.dialogs.addgroupmember.AddGroupMemberCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.loanpaid.SetLoanPaidCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.loanpaid.SetLoanPaidDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanCallBack;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.home.main.clientlists.ClientListActivity;
import com.gebeya.mobile.bidir.ui.profile.ProfileActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.register.RegisterActivity;


public class GroupedClientFragment extends BaseFragment implements
        GroupedClientContract.View,
        AddGroupMemberCallback,
        ErrorDialogCallback,
        SetLoanPaidCallback,
        RecordLoanCallBack{

    private GroupedClientContract.Presenter presenter;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private GroupedClientRecyclerViewAdapter adapter;
    private LinearLayoutManager manager;

    private TextView noClientsLabel;
    private View clientsAddNewFab;

    private AddGroupMember addGroupMember;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;
    private RecordLoanDialog recordLoanDialog;

    private SetLoanPaidDialog loanPaidDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_group_application, container, false);

        toolbar = getView(R.id.groupedApplicationToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(view -> presenter.onBackButtonPressed());

        noClientsLabel = getTv(R.id.groupedNoClientsLabel);

        getView(R.id.clientsAddNewFab).setOnClickListener(fabClickListener);
        clientsAddNewFab = getView(R.id.clientsAddNewFabContainer);
        clientsAddNewFab.setOnClickListener(fabClickListener);
        recyclerView = getView(R.id.groupedApplicationRecyclerView);
        manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);

        adapter = new GroupedClientRecyclerViewAdapter(presenter);

        return root;
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onAddButtonClicked();
        }
    };

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
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void showClients() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void toggleNoClientsLabel(boolean show) {
        noClientsLabel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void toggleClientAddButton(boolean show) {
        clientsAddNewFab.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void toggleMenuItems(@NonNull PopupMenu popupMenu, @NonNull Client client, @NonNull Group group) {

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
    public void openPopUpMenu(@NonNull TextView view, @NonNull Client client, @NonNull Group group) {
        Context wrapper = new ContextThemeWrapper(getContext(),R.style.popupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.inflate(R.menu.group_member_option_menu);

        toggleMenuItems(popup, client, group);
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.assignLeaderMenu:
                    presenter.onAssignLeaderClicked(client);
                    break;
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
                    if (!group.groupStatus.equalsIgnoreCase(GroupParser.LOAN_GRANTED)) {
                        toast(R.string.grouped_acat_applications_fragment_loan_not_granted_label);
                        break;
                    }
                    final Intent monitor = new Intent(getActivity(), ACATCropItemActivity.class);
                    monitor.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, client._id);
                    monitor.putExtra(ACATCropItemActivity.IS_MONITORING, true);
                    startActivity(monitor);
                    break;
                case R.id.loanPaidMenu:
                    if (!client.status.equalsIgnoreCase(GroupParser.LOAN_GRANTED)) {
                        toast(R.string.grouped_acat_applications_fragment_loan_not_granted_label);
                        break;
                    }
                    presenter.onLoanPaidClicked(client);
                    break;
            }
            return false;
        });

        popup.show();
    }

    @Override
    public void openAddClientScreen(@NonNull Group group) {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        intent.putExtra(RegisterActivity.ARG_GROUP_ID, group._id);
        startActivity(intent);
    }

    @Override
    public void openClient(@NonNull Client client) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.ARG_CLIENT_ID, client._id);
        startActivity(intent);
    }

    @Override
    public void openAddMemberChoiceScreen() {
        addGroupMember = AddGroupMember.newInstance();
        addGroupMember.setCallback(this);
        addGroupMember.show(getChildFragmentManager(), null);
    }

    @Override
    public void onAdd(boolean isNew) {
        presenter.onAddGroupMemberReturned(isNew);
    }

    @Override
    public void openSelectClientScreen(@NonNull Group group) {
        Intent intent = new Intent(getContext(), ClientListActivity.class);
        intent.putExtra(ClientListActivity.ARG_GROUP_ID, group._id);
        startActivity(intent);
    }

    @Override
    public void openConfirmationDialog(@Nullable Client client) {
        loanPaidDialog = SetLoanPaidDialog.newInstance(client, null);
        loanPaidDialog.setCallback(this);
        loanPaidDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingClientProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.client_status_updating_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_approved_updating_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.clients_loading_progress));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdateProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.clients_updating_progress));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideUpdateProgress() {
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
            message = getString(R.string.clients_generic_error);
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
    public void showUpdateSuccessfulMessage() {
        toast(R.string.screening_update_success_message);
    }

    @Override
    public void attachPresenter(GroupedClientContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void onLoanPaidCallBack(@Nullable Client client, @Nullable Group group) {
        if (client != null)
        presenter.onSetLoanPaidReturned(client);
    }

    @Override
    public void onRecord(@NonNull String loanApproved, @NonNull String clientId) {
        presenter.onRecordReturned(loanApproved, clientId);
    }
}

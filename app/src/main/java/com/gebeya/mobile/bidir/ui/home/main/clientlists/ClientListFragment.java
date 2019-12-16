package com.gebeya.mobile.bidir.ui.home.main.clientlists;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;

public class ClientListFragment extends BaseFragment implements ClientListContract.View,
        ErrorDialogCallback{

    private ClientListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ClientListRecyclerViewAdapter adapter;
    private LinearLayoutManager manager;

    private TextView menuOK;
    private TextView noClientLabel;
    private TextView selectedClientCount;

    private Toolbar toolbar;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_client_list, container, false);

        toolbar = getView(R.id.clientListToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(view -> presenter.onBackButtonPressed());

        menuOK = getTv(R.id.clientListMenuOK);
        menuOK.setOnClickListener(view -> presenter.onOKButtonClicked());

        noClientLabel = getTv(R.id.clientListNoClientsLabel);
        selectedClientCount = getTv(R.id.clientListSelectedMemberCount);

        recyclerView = getView(R.id.clientListRecyclerView);
        adapter = new ClientListRecyclerViewAdapter(presenter);
        manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);

        return root;
    }

    @Override
    public void setToolbarTitle() {
        toolbar.setTitle(getString(R.string.group_client_list_select_title));
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
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void updateSelectedClientCount(int size) {
        final String clientCount = getString(R.string.client_list_selected_count_label, size);
        selectedClientCount.setText(clientCount);
    }

    @Override
    public void refreshClients() {
        adapter.notifyDataSetChanged();
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
            message = getString(R.string.group_member_update_fail_message);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.group_error_title),
                message,
                extra
        );

        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdateSuccessMessage() {
        toast(R.string.group_member_updated_success_message);
    }

    @Override
    public void shoUpdateProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.updating_group_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void attachPresenter(ClientListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void showClients() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void toggleNoClientsLabel(boolean show) {
        noClientLabel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onErrorDialogDismissed() {

    }
}

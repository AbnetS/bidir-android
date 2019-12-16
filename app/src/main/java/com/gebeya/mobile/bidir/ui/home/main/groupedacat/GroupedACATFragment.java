package com.gebeya.mobile.bidir.ui.home.main.groupedacat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanCallBack;
import com.gebeya.mobile.bidir.ui.common.dialogs.recordloan.RecordLoanDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatapplication.ACATPreliminaryInfoActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat.PreliminaryInformationActivity;

import javax.inject.Inject;

public class GroupedACATFragment extends BaseFragment implements
        GroupedACATContract.View,
        ErrorDialogCallback {

    private GroupedACATContract.Presenter presenter;

    private Toolbar toolbar;
    private TextView menuDone;

    private RecyclerView recyclerView;
    private GroupedACATRecyclerViewAdapter adapter;
    private LinearLayoutManager manager;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    private RecordLoanDialog recordLoanDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_group_acats, container, false);

        toolbar = getView(R.id.groupedACATsToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(view -> presenter.onBackButtonPressed());

        menuDone = getTv(R.id.groupACATMenuDone);
        menuDone.setOnClickListener(v -> presenter.onDoneClicked());

        manager = new LinearLayoutManager(getContext());
        recyclerView = getView(R.id.groupedACATRecyclerView);
        recyclerView.setLayoutManager(manager);

        adapter = new GroupedACATRecyclerViewAdapter(presenter);

        return root;
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showACATs() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.acat_application_loading_progress_message));
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
    public void showUpdateProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.acat_application_updating_progress_message));
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
            message = getString(R.string.acat_application_update_error_message);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.acat_application_update_error_title),
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
    public void toggleDoneMenuButton(boolean show) {
        menuDone.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void openACAT(@NonNull String clientId, @Nullable String groupId) {
        final Intent intent = new Intent(getActivity(), ACATPreliminaryInfoActivity.class);
        intent.putExtra(ACATPreliminaryInfoActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATPreliminaryInfoActivity.ARG_GROUP_ID, groupId);
        startActivity(intent);
    }

    @Override
    public void openACATApplicationInitializer(@NonNull String clientId, @Nullable String groupId) {
        final Intent intent = new Intent(getActivity(), PreliminaryInformationActivity.class);
        intent.putExtra(PreliminaryInformationActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(PreliminaryInformationActivity.ARG_GROUP_ID, groupId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void openCropList(@NonNull String clientId, @Nullable String groupId) {
        final Intent intent = new Intent(getActivity(), ACATCropItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATCropItemActivity.ARG_GROUP_ID, groupId);
        startActivity(intent);
    }

    @Override
    public void openPopUpMenu(@NonNull TextView anchor, @NonNull Client client, @NonNull Group group) {
        Context wrapper = new ContextThemeWrapper(getContext(),R.style.popupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, anchor);
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
    public void attachPresenter(GroupedACATContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getActivity().finish();
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
    public void onErrorDialogDismissed() {

    }
}

package com.gebeya.mobile.bidir.ui.home.main.groupedscreenings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.gebeya.mobile.bidir.ui.form.screening.ScreeningActivity;
import com.gebeya.mobile.bidir.ui.home.HomeActivity;
import com.gebeya.mobile.bidir.ui.home.main.MainFragment;

public class GroupedScreeningsFragment extends BaseFragment implements
        GroupedScreeningsContract.View,
        ErrorDialogCallback{

    private GroupedScreeningsContract.Presenter presenter;

    private Toolbar toolbar;

    private TextView menuDone;

    private RecyclerView recyclerView;
    private GroupedScreeningsRecyclerViewAdapter adapter;
    private LinearLayoutManager manager;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_group_screenings, container, false);

        toolbar = getView(R.id.groupedScreeningsToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(view -> presenter.onBackButtonPressed());

        menuDone = getTv(R.id.groupScreeningMenuDone);
        menuDone.setOnClickListener(v -> presenter.onDoneClicked());

        manager = new LinearLayoutManager(getContext());
        recyclerView = getView(R.id.groupedScreeningRecyclerView);
        recyclerView.setLayoutManager(manager);

        adapter = new GroupedScreeningsRecyclerViewAdapter(presenter);


        return root;
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showScreenings() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.screening_loading_progress_message));
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
        waitingDialog = WaitingDialog.newInstance(getString(R.string.screening_updating_progress_message));
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
    public void openScreening(@NonNull String screeningId, @Nullable String groupId) {
        final Intent intent = new Intent(getContext(), ScreeningActivity.class);
        intent.putExtra(ScreeningActivity.ARG_SCREENING_ID, screeningId);
        intent.putExtra(ScreeningActivity.ARG_GROUP_ID, groupId);
        startActivity(intent);
    }

    @Override
    public void showCreatingLoanProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_application_loading_progress_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.questions_error_generic);
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
    public void attachPresenter(GroupedScreeningsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void onErrorDialogDismissed() {

    }
}

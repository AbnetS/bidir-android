package com.gebeya.mobile.bidir.ui.home.main.groupedloans;

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
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.form.loanapplication.LoanApplicationActivity;

public class GroupedLoansFragment extends BaseFragment implements
        GroupedLoansContract.View,
        ErrorDialogCallback {

    private GroupedLoansContract.Presenter presenter;

    private Toolbar toolbar;
    private TextView menuDone;

    private RecyclerView recyclerView;
    private GroupedLoansRecyclerViewAdapter adapter;
    private LinearLayoutManager manager;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_group_loans, container, false);

        toolbar = getView(R.id.groupedLoansToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(view -> presenter.onBackButtonPressed());

        menuDone = getTv(R.id.groupLoanMenuDone);
        menuDone.setOnClickListener(v -> presenter.onDoneClicked());

        manager = new LinearLayoutManager(getContext());
        recyclerView = getView(R.id.groupedLoanRecyclerView);
        recyclerView.setLayoutManager(manager);

        adapter = new GroupedLoansRecyclerViewAdapter(presenter);

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
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_application_loading_progress_message));
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
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_application_updating_progress_message));
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
            message = getString(R.string.loan_application_update_error_message);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.loan_application_update_error_title),
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
    public void openLoan(@NonNull String applicationId, @Nullable String groupId) {
        final Intent intent = new Intent(getContext(), LoanApplicationActivity.class);
        intent.putExtra(LoanApplicationActivity.ARG_APPLICATION_ID, applicationId);
        intent.putExtra(LoanApplicationActivity.ARG_GROUP_ID, groupId);
        startActivity(intent);
    }

    @Override
    public void showCreatingACATProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.group_acat_loading_progress));
        waitingDialog.show(getChildFragmentManager(), null);
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
    public void attachPresenter(GroupedLoansContract.Presenter presenter) {
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

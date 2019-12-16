package com.gebeya.mobile.bidir.ui.summary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.summary_costs.SummaryCostsFragment;
import com.gebeya.mobile.bidir.ui.summary_inputs.SummaryInputsFragment;
import com.gebeya.mobile.bidir.ui.summary_yield.SummaryYieldFragment;

/**
 * View implementation for the summary fragment screen.
 */
public class SummaryFragment extends BaseFragment implements SummaryContract.View,
        ErrorDialogCallback,
        DeclineDialogCallback{

    private SummaryContract.Presenter presenter;

    private Toolbar toolbar;

    private TextView cropNameLabel;
    private TextView cropFirstExpenseMonthLabel;
    private TextView cropCroppingAreaLabel;


    private RecyclerView recyclerView;
    private SummaryMenuAdapter adapter;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;
    private DeclineDialog declineDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_summary, container, false);

        toolbar = getView(R.id.summaryToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        cropNameLabel = getTv(R.id.summaryCropNameLabel);
        cropFirstExpenseMonthLabel = getTv(R.id.summaryCropFirstExpenseMonthLabel);
        cropCroppingAreaLabel = getTv(R.id.summaryCropCroppingAreaLabel);

        recyclerView = getView(R.id.summaryMenuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SummaryMenuAdapter(inflater, presenter);

        return root;
    }


    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void loadMenu() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshMenu() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setCropName(@NonNull String name) {
        cropNameLabel.setText(name);
    }

    @Override
    public void setCropFirstExpenseMonth(@NonNull String month) {
        cropFirstExpenseMonthLabel.setText(month);
    }

    @Override
    public void setCroppingArea(@NonNull String area) {
        cropCroppingAreaLabel.setText(area);
    }

    @Override
    public void loadInputs(@NonNull String clientId, @NonNull String cropACATId) {
        final SummaryInputsFragment fragment = SummaryInputsFragment.newInstance(clientId, cropACATId);
        final FragmentManager manager = getChildFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.summaryInnerFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void loadCosts(@NonNull SummaryMenuItem.Label label, @NonNull String clientId, @NonNull String cropACATId) {
        final SummaryCostsFragment fragment = SummaryCostsFragment.newInstance(label, clientId, cropACATId);
        final FragmentManager manager = getChildFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.summaryInnerFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void loadYields(@NonNull SummaryMenuItem.Label label, @NonNull String clientId, @NonNull String cropACATId) {
        final SummaryYieldFragment fragment = SummaryYieldFragment.newInstance(label, clientId, cropACATId);
        final FragmentManager manager = getChildFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.summaryInnerFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void toggleApproveButton(boolean show) {

    }

    @Override
    public void toggleDeclineButton(boolean show) {

    }

    @Override
    public void showUpdatingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_proposal_updating_state));
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
            message = getString(R.string.acat_questions_error_generic);
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
    public void openDeclineDialog() {
        declineDialog = DeclineDialog.newInstance(false, true);
        declineDialog.setCallback(this);
        declineDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdateSuccessfulMessage() {
        toast(R.string.questions_update_success_message);
    }

    @Override
    public void attachPresenter(SummaryContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void onDecline(@NonNull String remark, boolean isFinal) {
        presenter.onDeclineReturned(remark, isFinal);
    }
}
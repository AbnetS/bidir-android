package com.gebeya.mobile.bidir.ui.crop_summary;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.crop_summary_list.CropSummaryListFragment;
import com.gebeya.mobile.bidir.ui.loan_proposal_summary.LoanProposalSummaryFragment;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryFragment extends BaseFragment implements CropSummaryContract.View,
        ErrorDialogCallback,
        DeclineDialogCallback{

    private CropSummaryContract.Presenter presenter;

    private Toolbar toolbar;

    private TextView loanProposalMenu;

    private RecyclerView recyclerView;
    private CropSummaryMenuAdapter adapter;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;
    private DeclineDialog declineDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_crop_summary, container, false);

        toolbar = getView(R.id.cropSummaryToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        loanProposalMenu = getTv(R.id.cropMenuLoanProposalItemNameLabel);
        loanProposalMenu.setOnClickListener(view -> presenter.onLoanProposalClicked());
        recyclerView = getView(R.id.cropSummaryMenuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CropSummaryMenuAdapter(presenter);


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
    public void attachPresenter(CropSummaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadCrops(@NonNull String clientId, @NonNull String cropId) {
        final CropSummaryListFragment fragment = CropSummaryListFragment.newInstance(clientId, cropId);
        final FragmentManager manager = getChildFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.cropSummaryInnerFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void loadLoanProposal(@NonNull String clientId) {
        final LoanProposalSummaryFragment fragment = LoanProposalSummaryFragment.newInstance(clientId);
        final FragmentManager manager = getChildFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.cropSummaryInnerFragmentContainer, fragment)
                .commit();
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
    public void showLoadingMessage() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_proposal_loading_state));
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
    public void toggleApproveButton(boolean show) {

    }

    @Override
    public void toggleDeclineButton(boolean show) {

    }

    @Override
    public void toggleMenuButton(boolean show) {

    }

    @Override
    public void openDeclineDialog() {
        declineDialog = DeclineDialog.newInstance(false, true);
        declineDialog.setCallback(this);
        declineDialog.show(getChildFragmentManager(), null);
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

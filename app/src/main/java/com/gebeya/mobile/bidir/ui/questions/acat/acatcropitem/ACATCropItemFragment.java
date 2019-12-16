package com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryActivity;

public class ACATCropItemFragment extends BaseFragment implements ACATCropItemContract.View,
        ErrorDialogCallback,
        DeclineDialogCallback{

    private ACATCropItemContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ACATCropItemRecyclerViewAdapter adapter;
    private RelativeLayout layout;

    private TextView optionMenu;
    private TextView statusLabel;
    private TextView menuApprove;
    private TextView menuDecline;

    Toolbar toolbar;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;
    private DeclineDialog declineDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_crop_item, container, false);

        toolbar = getView(R.id.acatCropItemToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        recyclerView = getView(R.id.acatApplicationsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);

        layout = getView(R.id.cropItemListLoanProposalMenu);
        layout.setOnClickListener(view -> presenter.onLoanProposalClicked());

        statusLabel = getTv(R.id.loanProposalItemLayoutStatusLabel);
        optionMenu = getTv(R.id.textViewOptionsMenu);
        optionMenu.setVisibility(presenter.optionMenuVisibility());

        menuApprove = getTv(R.id.clientACATApprove);
        menuApprove.setOnClickListener(view -> presenter.onApproveButtonClicked());
        menuDecline = getTv(R.id.clientACATDecline);
        menuDecline.setOnClickListener(view -> presenter.onDeclineButtonClicked());
        adapter = new ACATCropItemRecyclerViewAdapter(presenter);

        return root;
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
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
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void attachPresenter(ACATCropItemContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getParent().finish();
    }

    @Override
    public void showACATCropItems() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openACATCostEstimation(@NonNull String clientId, @NonNull String cropACATId, boolean monitor) {
        final Intent intent = new Intent(getActivity(), ACATCostEstimationActivity.class);
//        intent.putExtra(ACATCostEstimationActivity.IS_EDITING, true);
        intent.putExtra(ACATCostEstimationActivity.ARG_CROP_ACAT_ID, cropACATId);
        intent.putExtra(ACATCostEstimationActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATCostEstimationActivity.IS_MONITORING, monitor);
        startActivity(intent);
    }

    @Override
    public void openACATSummary(@NonNull String clientId, @NonNull String cropACATId) {
        final Intent intent = new Intent(getActivity(), SummaryActivity.class);
        intent.putExtra(SummaryActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(SummaryActivity.ARG_CROP_ACAT_ID, cropACATId);

        startActivity(intent);
    }

    @Override
    public void toggleLoanProposal(boolean show) {
        layout.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @Override
    public void toggleOptionMenu(boolean show) {
        optionMenu.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleApproveButton(boolean show) {
        if (menuDecline.getVisibility() == View.GONE) {
            menuApprove.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void toggleDeclineButton(boolean show) {
        menuDecline.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @Override
    public void setLoanStatus(@NonNull LoanProposal loanProposal) {
        statusLabel.setText(loanProposal.status);
    }

    @Override
    public void openLoanProposalSummary(@NonNull String clientId, @Nullable Task task) {
        final Intent intent = new Intent(getActivity(), CropSummaryActivity.class);
        intent.putExtra(CropSummaryActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(CropSummaryActivity.ARG_TASK, task);
        startActivity(intent);
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_application_loading_progress_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdatingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.updating_acat_message));
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
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.acat_questions_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.questions_error_loading_title),
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
    public void showUpdatingSuccessMessage() {
        toast(R.string.update_success_message);
    }

    @Override
    public void showDataNotSyncedMessage() {
        toast(R.string.acat_data_not_synced_message);
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
    public void refreshList() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void openPopUpMenu(@NonNull View view, @NonNull String clientId, @NonNull String cropID) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.crop_item_option_menu);

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.summaryMenu:
                    openACATSummary(clientId, cropID);
                    break;
            }
            return false;
        });

        popup.show();
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void onDecline(@NonNull String remark, boolean isFinal) {
        presenter.onDeclineReturned(remark, isFinal);
    }
}

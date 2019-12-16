package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.questions.acat.acatapplication.ACATPreliminaryInfoActivity;


/**
 * Created by Samuel K. on 5/5/2018.
 * <p>
 * samkura47@gmail.com
 */
public class PreliminaryInformationFragment extends BaseFragment implements
        PreliminaryInformationContract.View,
        ErrorDialogCallback {

    public static PreliminaryInformationFragment newInstance() {
        return new PreliminaryInformationFragment();
    }

    private PreliminaryInformationContract.Presenter presenter;

    private Toolbar toolbar;

    private TextView menuNext;

    private TextView selectedCropsLabel;
    private TextView selectedCropsCounterLabel;

    private RecyclerView recyclerView;
    private PreliminaryInformationAdapter adapter;
    private Spinner spinner;
    private LoanProductSpinnerAdapter spinnerAdapter;
    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_preliminary_information, container, false);

        toolbar = getView(R.id.preliminaryInfoToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);

        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        menuNext = getTv(R.id.preliminaryInfoMenuNext);
        menuNext.setOnClickListener(v -> presenter.onNextClicked());

        selectedCropsLabel = getTv(R.id.acatPreliminaryInfoSelectedCropsLabel);
        selectedCropsCounterLabel = getTv(R.id.acatPreliminaryInfoSelectedCropsCounterLabel);

        spinner = root.findViewById(R.id.loanProductSpinner);
        spinnerAdapter = new LoanProductSpinnerAdapter(getParent(), presenter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onLoanProductItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView = getView(R.id.choicesRecyclerView);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(manager);
        adapter = new PreliminaryInformationAdapter(presenter);

        return root;
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
        super.onPause();
        presenter.detachView();
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void attachPresenter(PreliminaryInformationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.preliminary_info_loading_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideProgress() {
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
    public void refreshAnswers() {
        adapter.notifyDataSetChanged();
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAnswers() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoanProducts() {
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void showAnswerMissingError() {
        toast(R.string.preliminary_questions_error_answer_unfilled, Toast.LENGTH_LONG);
    }

    @Override
    public void showInitializingACATProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.initializing_acat_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.preliminary_info_update_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.questions_error_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showLoadingError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.preliminary_info_load_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.questions_error_loading_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideError() {
        if (errorDialog != null) {
            try {
                errorDialog.dismiss();
                errorDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showUpdateSuccessMessage() {
        toast(R.string.questions_update_success_message);
    }

    @Override
    public void openACATPreliminaryInfo(@NonNull String clientId, @Nullable String groupId) {
        final Intent intent = new Intent(getActivity(), ACATPreliminaryInfoActivity.class);
        intent.putExtra(ACATPreliminaryInfoActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATPreliminaryInfoActivity.ARG_GROUP_ID, groupId);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onErrorDialogDismissed() {
        presenter.onErrorDismissed();
    }

    @Override
    public void toggleNextButton(boolean show) {
        menuNext.setEnabled(show);
    }

    @Override
    public void updateSelectedCropsCount(int count) {
        boolean empty = count == 0;
        final int visibility = empty ? View.GONE : View.VISIBLE;
        final int text = empty ? R.string.preliminary_no_crops_selected_label : R.string.preliminary_selected_crops_label;

        selectedCropsCounterLabel.setText(String.valueOf(count));
        selectedCropsCounterLabel.setVisibility(visibility);
        selectedCropsLabel.setText(text);
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
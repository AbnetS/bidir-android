package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.question.Status;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;

import java.util.List;

/**
 * Fragment/View implementation for the loan application questions
 */
public class LoanApplicationQuestionsFragment extends BaseFragment implements
        LoanApplicationQuestionsContract.View,
        ErrorDialogCallback,
        DeclineDialogCallback {

    public static LoanApplicationQuestionsFragment newInstance() {
        return new LoanApplicationQuestionsFragment();
    }

    private LoanApplicationQuestionsContract.Presenter presenter;

    private Toolbar toolbar;
    private LinearLayout indicatorContainer;

    private TextView menuSave, menuSubmit, menuAccept, menuDecline;

    private TextView remark;
    private LinearLayout remarkHolder;

    private RecyclerView recyclerView;
    private View disableShield;
    private LoanApplicationQuestionsAdapter adapter;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_loan_application_questions, container, false);

        toolbar = getView(R.id.questionsToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);

        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        indicatorContainer = getView(R.id.questionsIndicatorContainer);

        menuSave = getTv(R.id.loanAssessmentMenuSave);
        menuSave.setOnClickListener(v -> presenter.onSaveClicked());

        menuSubmit = getTv(R.id.loanAssessmentMenuSubmit);
        menuSubmit.setOnClickListener(v -> presenter.onSubmitClicked());

        menuAccept = getTv(R.id.loanAssessmentMenuApprove);
        menuAccept.setOnClickListener(v -> presenter.onAcceptClicked());

        menuDecline = getTv(R.id.loanAssessmentMenuDecline);
        menuDecline.setOnClickListener(v -> presenter.onDeclineClicked());

        disableShield = getView(R.id.questionsReadOnlyShield);
        disableShield.setOnClickListener(v -> {});

        recyclerView = getView(R.id.questionsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new LoanApplicationQuestionsAdapter(presenter);

        remark = getTv(R.id.questionsRemarkLabel);
        remarkHolder = getView(R.id.questionsRemarkContainer);

        return root;
    }

    @Override
    public void showRemark(@NonNull String comment) {
        remark.setText(comment);
    }

    @Override
    public void hideRemark(boolean hide) {
        remarkHolder.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void toggleDisableShield(boolean show) {
        disableShield.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAnswers() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMandatoryMissingError() {
        toast(R.string.questions_error_mandatory_unfilled, Toast.LENGTH_LONG);
    }

    @Override
    public void showUpdatingScreeningProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.loan_application_updating_progress_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideUpdatingScreeningProgress() {
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
            message = getString(R.string.questions_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.questions_error_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onErrorDialogDismissed() {

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
        toast(R.string.screening_update_success_message);
    }

    @Override
    public void onDecline(@NonNull String remark, boolean isFinal) {
        presenter.onDeclineReturned(remark, isFinal);
    }

    @Override
    public void toggleSaveButton(boolean show) {
        menuSave.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleSubmitButton(boolean show) {
        menuSubmit.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleAcceptButton(boolean show) {
        menuAccept.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleDeclineButton(boolean show) {
        menuDecline.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void openDeclineDialog() {
        DeclineDialog dialog = DeclineDialog.newInstance(false, false);
        dialog.setCallback(this);
        dialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void populateIndicators(List<Answer> answers) {
        indicatorContainer.removeAllViews();
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            Status status = presenter.getAnswerStatus(answer);
            int background = getIndicatorBackground(status);

            TextView indicator = (TextView) getLayoutInflater().inflate(R.layout.question_indicator_layout,
                    indicatorContainer, false
            );
            indicator.setBackgroundResource(background);
            indicator.setTypeface(normal);
            indicator.setText(String.valueOf(answer.number));

            indicator.setOnClickListener(listener);
            indicatorContainer.addView(indicator);
        }
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = indicatorContainer.indexOfChild(v);
            presenter.onAnswerSelected(position);
        }
    };

    @Override
    public void updateIndicators(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            int background = getIndicatorBackground(presenter.getAnswerStatus(answer));
            TextView indicator = (TextView) indicatorContainer.getChildAt(i);
            indicator.setBackgroundResource(background);
        }
    }

    private int getIndicatorBackground(Status status) {
        switch (status) {
            case COMPLETED:
                return R.drawable.question_indicator_answered_background;
            case SELECTED:
                return R.drawable.question_indicator_selected_background;
            case INCOMPLETE_MANDATORY:
                return R.drawable.question_indicator_unanswered_mandatory_background;
            default:
                return R.drawable.question_indicator_unanswered_optional_background;
        }
    }
    @Override
    public void scrollToAnswer(int position) {
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void refreshAnswers() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void attachPresenter(LoanApplicationQuestionsContract.Presenter presenter) {
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
    public void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
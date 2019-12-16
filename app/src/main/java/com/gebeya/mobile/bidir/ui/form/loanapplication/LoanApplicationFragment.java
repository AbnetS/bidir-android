package com.gebeya.mobile.bidir.ui.form.loanapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.decline.DeclineDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionStatus;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionsAdapter;

import java.util.List;

public class LoanApplicationFragment extends BaseFragment implements LoanApplicationContract.View, ErrorDialogCallback, DeclineDialogCallback {

    private LoanApplicationContract.Presenter presenter;

    private Toolbar toolbar;

    private TextView menuSave;
    private TextView menuSubmit;
    private TextView menuApprove;
    private TextView menuDecline;

    private LinearLayout indicatorContainer;

    private View remarkContainer;
    private TextView remarkLabel;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;
    private DeclineDialog declineDialog;

    private RecyclerView recyclerView;
    private View shieldView;
    private LinearLayoutManager manager;
    private QuestionsAdapter adapter;

    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_loan_application, container, false);

        toolbar = getView(R.id.loanApplicationToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackButtonPressed());

        menuSave = getTv(R.id.loanApplicationMenuSave);
        menuSave.setOnClickListener(v -> presenter.onSaveClicked());

        menuSubmit = getTv(R.id.loanApplicationMenuSubmit);
        menuSubmit.setOnClickListener(v -> presenter.onSubmitClicked());

        menuApprove = getTv(R.id.loanApplicationMenuApprove);
        menuApprove.setOnClickListener(v -> presenter.onApproveClicked());

        menuDecline = getTv(R.id.loanApplicationMenuDecline);
        menuDecline.setOnClickListener(v -> presenter.onDeclineClicked());

        indicatorContainer = getView(R.id.loanApplicationIndicatorContainer);
        remarkContainer = getView(R.id.loanApplicationRemarkContainer);
        remarkLabel = getTv(R.id.loanApplicationRemarkLabel);

        recyclerView = getView(R.id.loanApplicationRecyclerView);
        manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);
        adapter = new QuestionsAdapter(inflater, presenter);

        shieldView = getView(R.id.loanApplicationReadOnlyShield);
        this.inflater = inflater;

        return root;
    }

    @Override
    public void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(
                getString(R.string.loan_application_loading_progress_message)
        );
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
    public void showUpdateSuccessfulMessage() {
        toast(R.string.screening_update_success_message);
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
    public void onErrorDialogDismissed() {

    }

    @Override
    public void showMandatoryMissingError() {
        toast(R.string.questions_error_mandatory_unfilled, Toast.LENGTH_LONG);
    }

    @Override
    public void openDeclineDialog() {
        declineDialog = DeclineDialog.newInstance(false, false);
        declineDialog.setCallback(this);
        declineDialog.show(getChildFragmentManager(), null);
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
    public void toggleApproveButton(boolean show) {
        menuApprove.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleDeclineButton(boolean show) {
        menuDecline.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleRemark(boolean show) {
        remarkContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        remarkLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleReadOnlyModeShield(boolean show) {
        shieldView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRemark(@NonNull String remark) {
        remarkLabel.setText(remark);
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void generateQuestionIndicators(@NonNull List<ComplexQuestion> questions) {
        indicatorContainer.removeAllViews();
        final int length = questions.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = questions.get(i);
            final TextView indicator = createIndicator(String.valueOf(question.number + 1));
            indicatorContainer.addView(indicator);
        }
        refreshIndicators(questions);
    }

    @NonNull
    private TextView createIndicator(@NonNull String number) {
        final TextView textView = (TextView) inflater.inflate(R.layout.question_indicator_layout, indicatorContainer, false);
        textView.setText(number);
        textView.setOnClickListener(v -> presenter.onQuestionSelected(indicatorContainer.indexOfChild(v)));
        return textView;
    }

    private void setIndicatorBackground(@NonNull TextView indicator, @NonNull QuestionStatus status) {
        final int background;
        final int textColor = status == QuestionStatus.LOCKED ? R.color.gray : R.color.white;

        switch (status) {
            case SELECTED:
                background = R.drawable.question_indicator_selected_background;
                break;
            case ANSWERED:
                background = R.drawable.question_indicator_answered_background;
                break;
            case UNANSWERED_OPTIONAL:
                background = R.drawable.question_indicator_unanswered_optional_background;
                break;
            case UNANSWERED_MANDATORY:
                background = R.drawable.question_indicator_unanswered_mandatory_background;
                break;
            default:
                background = R.drawable.question_indicator_disabled_background;
        }

        indicator.setTextColor(ContextCompat.getColor(getParent(), textColor));
        indicator.setBackgroundResource(background);
    }

    @Override
    public void loadQuestions() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshQuestion(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void refreshQuestions() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshIndicators(@NonNull List<ComplexQuestion> questions) {
        final int length = questions.size();
        for (int i = 0; i < length; i++) {
            final TextView indicator = (TextView) indicatorContainer.getChildAt(i);
            final QuestionStatus status = presenter.getQuestionStatus(i);
            setIndicatorBackground(indicator, status);
        }
    }

    @Override
    public void scrollToItem(int position) {
        //recyclerView.smoothScrollToPosition(position);
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void attachPresenter(LoanApplicationContract.Presenter presenter) {
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
        getActivity().finish();
    }
}

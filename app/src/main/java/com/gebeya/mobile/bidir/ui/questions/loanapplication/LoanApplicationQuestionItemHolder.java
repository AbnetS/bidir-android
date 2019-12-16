package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionItemView;

/**
 * Question Item Holder
 */
public class LoanApplicationQuestionItemHolder extends RecyclerView.ViewHolder implements QuestionItemView, View.OnClickListener {

    private View itemView;
    private TextView titleLabel;
    private TextView numberLabel;
    private TextView commentLabel;
    private TextView asterisk;

    private LinearLayout choiceContainer;

    private LoanApplicationQuestionsContract.Presenter presenter;
    private LoanApplicationChoiceViewCreator creator;

    public LoanApplicationQuestionItemHolder(View itemView,
                                             LoanApplicationQuestionsContract.Presenter presenter,
                                             LoanApplicationChoiceViewCreator creator) {
        super(itemView);
        this.itemView = itemView;
        this.presenter = presenter;
        this.creator = creator;
        titleLabel = BaseActivity.getTv(R.id.questionItemLayoutMessageLabel, itemView);
        numberLabel = BaseActivity.getTv(R.id.questionItemLayoutNumberLabel, itemView);
        commentLabel = BaseActivity.getTv(R.id.questionItemLayoutCommentLabel, itemView);
        choiceContainer = itemView.findViewById(R.id.questionItemLayoutChoiceContainer);
        asterisk = BaseActivity.getTv(R.id.questionLoanItemLayoutMandatoryIndicator, itemView);

        this.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        presenter.onAnswerSelected(getAdapterPosition());
    }

    @Override
    public void setText(@NonNull String text) {

    }

    @Override
    public void setRequired(boolean required) {

    }

    @Override
    public void setLocked(boolean locked) {

    }

    @Override
    public void setRemark(@NonNull String remark) {

    }

    @Override
    public void toggleSection(boolean visible) {

    }

    @Override
    public void setSection(@NonNull String title) {

    }

    @Override
    public void setNumber(@NonNull String number) {

    }

    @Override
    public void setSelected(boolean selected) {

    }

    /*    @Override
    public void setToolbarTitle(@NonNull String title) {
        titleLabel.setText(title);
    }

    @Override
    public void setNumber(@NonNull String number) {
        numberLabel.setText(number);
    }

    @Override
    public void setComment(@NonNull String comment) {
        commentLabel.setText(comment);
    }

    @Override
    public void setMandatory(boolean show) {
        asterisk.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSelected(boolean selected) {
        int color = selected ? R.color.gray_super_light : R.color.white;
        itemView.setBackgroundColor(itemView.getResources().getColor(color));
    }

    @Override
    public void setChoices(@NonNull List<String> choices, @NonNull List<String> answers, final int type) {
        choiceContainer.removeAllViews();
        creator.setContainer(choiceContainer, presenter);
        final int position = getAdapterPosition();
        switch (type) {
            case QAHelper.TYPE_INPUT:
                creator.generateInputChoice(position, type,
                        answers.isEmpty() ? null : answers.getByType(0)
                );
                break;
            case QAHelper.TYPE_MULTIPLE_CHOICE_SINGLE:
                creator.generateSingleChoice(position, choices, answers.isEmpty() ? "" : answers.getByType(0));
                break;
            case QAHelper.TYPE_MULTIPLE_CHOICE_MULTIPLE:
                creator.generateMultipleChoice(position, type, choices, answers);
                break;
        }
    }*/
}

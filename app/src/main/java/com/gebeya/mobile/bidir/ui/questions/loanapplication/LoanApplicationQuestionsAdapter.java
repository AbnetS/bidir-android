package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;

public class LoanApplicationQuestionsAdapter extends RecyclerView.Adapter<LoanApplicationQuestionItemHolder> {

    private LoanApplicationQuestionsContract.Presenter presenter;
    private LoanApplicationChoiceViewCreator creator;

    public LoanApplicationQuestionsAdapter(LoanApplicationQuestionsContract.Presenter presenter) {
        this.presenter = presenter;
        creator = new LoanApplicationChoiceViewCreator();
    }

    @Override
    public LoanApplicationQuestionItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_application_question_item_layout, parent, false);

        return new LoanApplicationQuestionItemHolder(itemView, presenter, creator);
    }

    @Override
    public void onBindViewHolder(LoanApplicationQuestionItemHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getAnswerCount();
    }
}
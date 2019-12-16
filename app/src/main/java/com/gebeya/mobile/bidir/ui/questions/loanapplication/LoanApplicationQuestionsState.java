package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import com.gebeya.mobile.bidir.impl.util.network.BaseRequestState;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;

/**
 * Concrete implementation for the {@link RequestState} question state.
 */
public class LoanApplicationQuestionsState extends BaseRequestState {

    private static RequestState instance;

    public static RequestState getInstance() {
        if (instance == null) instance = new LoanApplicationQuestionsState();
        return instance;
    }
}
package com.gebeya.mobile.bidir.data.loanapplication.remote;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication;

import java.util.List;

/**
 * Class for representing the loan application pageResponse, including the loan application and answers.
 */
public class LoanApplicationResponse {
    public LoanApplication application;
    public List<Answer> answers;
    public Client client;
}
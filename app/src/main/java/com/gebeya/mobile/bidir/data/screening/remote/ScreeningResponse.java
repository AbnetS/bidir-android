package com.gebeya.mobile.bidir.data.screening.remote;

import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.screening.Screening;

import java.util.List;

/**
 * Class for representing the screening pageResponse, that involves both the screening and answers.
 */
public class ScreeningResponse {
    public Screening screening;
    public List<Answer> answers;
    public Client client;
}

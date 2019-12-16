package com.gebeya.mobile.bidir.data.complexloanapplication;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.section.Section;

import java.util.List;

public class ComplexLoanApplicationResponse {
    public ComplexLoanApplication application;
    public List<ComplexQuestionResponse> questionResponses;
    public List<Section> sections;
}
package com.gebeya.mobile.bidir.data.complexloanapplication;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.section.SectionRequest;

import java.util.List;

/**
 * Request class for loan application uploads.
 */
public class ComplexLoanApplicationRequest {
    public String status;
    public List<SectionRequest> sectionRequests;
    public List<ComplexQuestionRequest> requests;
}
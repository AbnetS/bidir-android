package com.gebeya.mobile.bidir.data.complexscreening;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.section.SectionRequest;

import java.util.List;

public class ComplexScreeningRequest {
    public String status;
    public List<ComplexQuestionRequest> requests;
    public List<SectionRequest> sectionRequests;
}
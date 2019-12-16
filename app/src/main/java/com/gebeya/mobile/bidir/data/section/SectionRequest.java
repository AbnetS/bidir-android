package com.gebeya.mobile.bidir.data.section;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;

import java.util.List;

/**
 * Class for holding the upload section request data.
 */
public class SectionRequest {
    public Section section;
    public List<ComplexQuestionRequest> requests;
}

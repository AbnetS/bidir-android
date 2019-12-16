package com.gebeya.mobile.bidir.data.form;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.section.Section;

import java.util.List;

/**
 * Class for holding the {@link ComplexForm} and its pageResponse components.
 */
public final class ComplexFormResponse {
    public ComplexForm form;
    public List<ComplexQuestionResponse> questionResponses;
    public List<Section> sections;
}
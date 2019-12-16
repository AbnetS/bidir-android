package com.gebeya.mobile.bidir.data.complexscreening;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.section.Section;

import java.util.List;

/**
 * Class for holding a {@link ComplexScreening} and all its contents.
 */
public final class ComplexScreeningResponse {
    public ComplexScreening screening;
    public List<ComplexQuestionResponse> questionResponses;
    public List<Section> sections;
}
package com.gebeya.mobile.bidir.data.complexquestion;

import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;

import java.util.List;

/**
 * Response containing data for a {@link ComplexQuestion} pageResponse object.
 */
public class ComplexQuestionResponse {
    public ComplexQuestion question;
    public List<Prerequisite> prerequisites;
}
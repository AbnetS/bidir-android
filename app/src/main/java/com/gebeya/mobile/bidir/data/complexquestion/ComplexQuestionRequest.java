package com.gebeya.mobile.bidir.data.complexquestion;

import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;

import java.util.List;

/**
 * Class used to hold information for a complex question that needs to be sent to the API.
 */
public class ComplexQuestionRequest {
    public ComplexQuestion question;
    public List<Prerequisite> prerequisites;
    public List<ComplexQuestion> childQuestions;
}

package com.gebeya.mobile.bidir.ui.form.questions.controller;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionStatus;

import java.util.List;

/**
 * Interface for a question controller, responsible for various operations on a
 * {@link com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion}.
 */
public interface QuestionController {

    /**
     * Return the status of the complex question. The status is used on the view to determine what
     * kind of background/visual layout to use for both the question layout and also the indicator
     * background for each question.
     *
     * @param question {@link ComplexQuestion} whose ComplexQuestion status needs looking up.
     * @return QuestionStatus enum representing the status of the given question.
     */
    @NonNull
    QuestionStatus getComplexQuestionStatus(@NonNull ComplexQuestion question);

    /**
     * Determines whether the given {@link ComplexQuestion} has been answered or not.
     *
     * @param question {@link ComplexQuestion} to check.
     * @return true if the complex question has an answer, false otherwise.
     */
    boolean complexQuestionAnswered(@NonNull ComplexQuestion question);

    /**
     * Determines whether the given {@link ComplexQuestion} is disabled or not, given the list of
     * {@link Prerequisite} internal objects.
     *
     * @param question {@link ComplexQuestion} to check.
     * @return true, if the complex question is disabled, false otherwise.
     */
    boolean complexQuestionLocked(@NonNull ComplexQuestion question);

    /**
     * Set the list of data to work with, including the master list of all {@link ComplexQuestion}
     * objects along with the list of all the {@link Prerequisite} objects needed by the complex
     * questions themselves.
     *
     * @param master        Master list of all the complex questions that are found inside the screening.
     * @param prerequisites All the list of prerequisites that are needed by the questions.
     */
    void setData(@NonNull List<ComplexQuestion> master, @NonNull List<Prerequisite> prerequisites);

    /**
     * Remove the list of questions used as the master list.
     */
    void clearData();

    /**
     * Get a list of child questions given the parent question.
     *
     * @param parentQuestion Parent question whose child questions to find and return.
     * @return List of found child questions.
     */
    @NonNull
    List<ComplexQuestion> getChildQuestions(@NonNull ComplexQuestion parentQuestion);

    /**
     * Returns a list of questions that depend on the provided questions, via dependency rules.
     *
     * @param question Question argument whose dependant questions need to be searched for.
     * @return List of dependant questions, or empty list if no results were found.
     */
    List<String> getDependants(@NonNull ComplexQuestion question);

    /**
     * Get the complex question with the given String ID value.
     *
     * @param id String ID value for the complex question to find.
     * @return Complex question found, or throws an Exception.
     */
    @NonNull
    ComplexQuestion getComplexQuestion(@NonNull String id);
}
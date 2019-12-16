package com.gebeya.mobile.bidir.ui.form.questions.values;

import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;

import java.util.List;

/**
 * Interface for objects that initializeACAT the sub-questions located inside the parent question.
 */
public interface QuestionChildViewCreator {

    /**
     * Sets the container that will be used to host the complex questions. This same container
     * also contains the parent question's title text view.
     *
     * @param container Container that will be used to set up the generated questions.
     */
    void setQuestionsContainer(@NonNull LinearLayout container);

    /**
     * Remove views (if any) from the container, skipping the first view (which contains the text
     * for the parent question)
     */
    void clearContainerQuestions();

    /**
     * Create the child question views, based on the given list of questions.
     *
     * @param childQuestions List of child questions to generate and add to the row container.
     * @param parentQuestion Parent question to which the list of child questions belong to.
     * @param parentPosition position of the parent question.
     */
    void createChildQuestionViews(@NonNull ComplexQuestion parentQuestion, @NonNull List<ComplexQuestion> childQuestions, int parentPosition);
}
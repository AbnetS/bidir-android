package com.gebeya.mobile.bidir.ui.form.questions.values;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;

import java.util.List;

/**
 * Interface definition for helper classes used to generate question choice values.
 */
public interface ChoiceViewCreator {

    /**
     * Sets the container that will be used on each item view to populate data.
     *
     * @param container LinearLayout container used to populate the data.
     */
    void setContainer(@NonNull LinearLayout container);

    /**
     * Creates an input view that can be used to manually enter in data.
     *
     * @param position         Position of the item row view.
     * @param validationFactor Validation factor, used to determine what kind of data goes in.
     * @param initialText      The initial text (if any) that should pre-populate the input.
     * @param question         Complex Question argument.
     */
    void createInputChoiceView(final int position, @NonNull String validationFactor, @Nullable String initialText, @NonNull ComplexQuestion question);

    /**
     * Returns an input view type, based on the validation factor. The returned input view type
     * is used to restrict the types of inputs that the input field can take, based on the provided
     * validation factor.
     *
     * @param validationFactor Validation factor used to return the input type.
     * @return Input type, based on the validation factor.
     */
    int getInputViewType(@NonNull String validationFactor);

    /**
     * Creates a single choice (with radio button) view, with the provided choices.
     *
     * @param position          Position of the item row view.
     * @param choices           List of choices to populate with.
     * @param preSelectedChoice Initial choice that is already selected.
     */
    void createSingleChoiceView(final int position, @NonNull List<String> choices, @NonNull String preSelectedChoice, @NonNull ComplexQuestion question);

    /**
     * Creates a multiple choice (with checkboxes) view, with the provided choices.
     *
     * @param position           Position of the item row view.
     * @param choices            List of choices to populate with.
     * @param preSelectedChoices Initial choices that are already selected/checked.
     */
    void createMultipleChoiceView(final int position, @NonNull List<String> choices, @NonNull List<String> preSelectedChoices, @NonNull ComplexQuestion question);
}
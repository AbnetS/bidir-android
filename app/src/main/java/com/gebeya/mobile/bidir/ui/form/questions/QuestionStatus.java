package com.gebeya.mobile.bidir.ui.form.questions;

/**
 * States that any question could contain.
 */
public enum QuestionStatus {

    /**
     * The question is currently selected/active/highlighted
     */
    SELECTED,

    /**
     * The question has been answered in full
     */
    ANSWERED,

    /**
     * The question is optional and has not been answered
     */
    UNANSWERED_OPTIONAL,

    /**
     * The question is required/mandatory and has not been answered
     */
    UNANSWERED_MANDATORY,

    /**
     * The question is locked, due to an unsatisfied prerequisite list of rules
     */
    LOCKED
}
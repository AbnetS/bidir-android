package com.gebeya.mobile.bidir.data.question;

/**
 * Enum representing the status of a given question
 */
public enum Status {
    /**
     * Question is currently selected
     */
    SELECTED,
    /**
     * Question has been answered
     */
    COMPLETED,
    /**
     * Question has not been answered yet (but optional)
     */
    INCOMPLETE_OPTIONAL,
    /**
     * Question has not been answered yet (but mandatory)
     */
    INCOMPLETE_MANDATORY,
    /**
     * Question that has not been answered
     */
    INCOMPLETE
}

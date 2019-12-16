package com.gebeya.apps.framework.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ResultTest {

    private static final String OK_MESSAGE = "Files downloaded successfully";
    private static final String ERROR_MESSAGE = "Files could not be downloaded";
    private static final String OK_EXTRA = "done";

    @Test
    public void testOk() {
        Result result = Result.create(Result.STATUS_OK, OK_MESSAGE, OK_EXTRA);
        assertTrue(result.isOk());
    }

    @Test
    public void testError() {
        Result result = Result.createError(ERROR_MESSAGE);
        assertEquals(result.toString(), ERROR_MESSAGE);
    }

    @Test
    public void testCreateMessage() {
        List<String> messages = new ArrayList<>();
        messages.add(OK_MESSAGE);
        messages.add(ERROR_MESSAGE);
        String expected = String.format(Locale.getDefault(), "%s\n%s\n", OK_MESSAGE, ERROR_MESSAGE);
        String actual = Result.createMessage(messages);
        assertEquals(expected, actual);
    }
}
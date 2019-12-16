package com.gebeya.apps.framework.backed.api.response;

import com.gebeya.apps.framework.backend.api.response.BaseResponseWrapper;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.spy;

public class BaseResponseWrapperTest {

    private static final int TEST_CODE = 200;
    private static final String TEST_BODY = "nobody...get it?";

    private BaseResponseWrapper response;

    @Before
    public void setUp() {
        response = spy(BaseResponseWrapper.class);
        response.setCode(TEST_CODE);
        response.setBody(TEST_BODY);
    }

    @Test
    public void testIsOk() {
        assertTrue(response.isOk());
    }

    @Test
    public void testBody() {
        assertEquals(TEST_BODY, response.getBody());
    }
}
